'use strict';
app.filter('formatFileSize', function () {
    var fileFormatFilter = function (bytes) {
        if (typeof bytes !== 'number') {
            return '';
        }
        if (bytes >= 1000000000) {
            return (bytes / 1000000000).toFixed(2) + ' GB';
        }
        if (bytes >= 1000000) {
            return (bytes / 1000000).toFixed(2) + ' MB';
        }
        return (bytes / 1000).toFixed(2) + ' KB';
    };
    return fileFormatFilter;
});
app.controller('editProductPhotoCtrl', function ($scope, $rootScope, FileUploader, $modal, $state, $http, $stateParams) {
    var uploader = $scope.uploader = new FileUploader({
        url: '/product/uploadForDetails',
        queueLimit: 6
    });

    var uploader1 = $scope.uploader1 = new FileUploader({
        url: '/product/upload',
        queueLimit: 1
    });
    function DownloadedFileItem(uploader, file){
        this.uploader = uploader;
        this.file = file;
        this.isSuccess = true;
        this.isCancel = false;
        this.isError = false;
        this.isReady = false;
        this.isUploaded = true;
        this.isUploading = false;
        this.progress= 100;
    }
    DownloadedFileItem.prototype.remove = function remove() {
        var that = this;
        $http({
            url: '/product/deletePic/',
            method: 'POST',
            data: {productId: that.file.productId, fileId: that.file.id, fileType: that.file.type, fileLocation: that.file.src}
        }).success(function (data) {
            if (data.resultCode == 'success') {
                that.uploader.removeFromQueue(that);
            } else {
                if (data.status <= 0) {
                    // $scope.errorMsg = '删除失败!';
                }
            }
        }).error(function () {
            // $scope.errorMsg = '系统异常，添加失败!';
        })
    };

    $scope.ok = function(){
       history.back();
    };

    $scope.uploadAll = function(uploader){
        angular.forEach(uploader.queue, function (item) {
            if(!item.isUploading && !item.isUploaded){
                item.upload();
            }
        })
    };

    $scope.removeAll = function(uploader){
        var modalInstance = $modal.open({
            templateUrl: 'deleteAllImagesConfirmation.html',
            controller: 'deleteAllImagesConfirmModalCtrl',
            size: 'sm',
            resolve: {
                uploader: function () {
                    return uploader;
                }
            }
        });
        modalInstance.result.then(function (flag) {
            if(flag == 1){
                console.log("delete all images successful!");
            }
        });
    };

    DownloadedFileItem.prototype._destroy = function _destroy() {
    };

    var productId = $stateParams.productId;
    console.log('editProductPhotoCtrl productId: ' + productId);
    $rootScope.product = {};
    $scope.product = {};
    $scope.p_status = '正常';
    var init = function () {
        $http.get('/product/' + productId)
            .success(function (data) {
                if (data.resultCode == 'success') {
                    $scope.product = data.object;
                    console.log($scope.product);
                    angular.forEach($scope.product.images, function (value, key) {
                        if(value.fileType == 2){
                            var file = {productId: productId, id: value.fileId, src: value.fileLocation, name: value.fileName, size: value.fileSize,
                            type: value.fileType};
                            var fileItem = new DownloadedFileItem(uploader, file);
                            uploader.queue.push(fileItem);
                        }
                        if(uploader1.queue.length == 0 && value.fileType == 1){
                            var file = {productId: productId, id: value.fileId, src: value.fileLocation, name: value.fileName, size: value.fileSize,
                            type: value.fileType};
                            var fileItem = new DownloadedFileItem(uploader1, file);
                            uploader1.queue.push(fileItem);
                        }
                    });
                    if ($scope.product.status == 0) {
                        $scope.p_status = '已下架';
                    } else if ($scope.product.status == 2) {
                        $scope.p_status = '特价';
                    }
                } else if (data.resultCode == 'error') {
                    console.log('没有登录信息');
                    $state.go('access.signin');
                }
            })
            .error(function () {
                console.log('请求失败');
            });
    };
    init();

    // FILTERS

    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    uploader1.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    // CALLBACKS
    uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function(fileItem) {
        if(fileItem.file.size > 131072){
            fileItem.isReady = false;
            fileItem.isError = true;
            $scope.errorMsg = "图片大小超出限制";
        }
        console.info('onAfterAddingFile', fileItem);
    };
    uploader.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function(item) {
        item.formData.push({productId: productId, fileType: 2});
        console.info('onBeforeUploadItem', item);
    };
    uploader.onProgressItem = function(fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader.onProgressAll = function(progress) {
        console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        var data = response.object;
        var file = {productId: data.productId, id: data.fileId, src: data.fileLocation, name: data.fileName, size: data.fileSize, type: data.fileType};
        var newFileItem = new DownloadedFileItem(uploader, file);
        var index = this.getIndexOfItem(fileItem);
        uploader.queue[index] = newFileItem;
        console.info('onSuccessItem', newFileItem, response, status, headers);
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function(fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader.onCompleteItem = function(fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
    };
    uploader.onCompleteAll = function() {
        console.info('onCompleteAll');
    };

    uploader1.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader1.onAfterAddingFile = function(fileItem) {
        if(fileItem.file.size > 131072){
            fileItem.isReady = false;
            fileItem.isError = true;
            $scope.errorMsg = "图片大小超出限制";
        }
        console.info('onAfterAddingFile', fileItem);
    };
    uploader1.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader1.onBeforeUploadItem = function(item) {
        item.formData.push({productId: productId, fileType: 1});
        console.info('onBeforeUploadItem', item);
    };
    uploader1.onProgressItem = function(fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader1.onProgressAll = function(progress) {
        console.info('onProgressAll', progress);
    };
    uploader1.onSuccessItem = function(fileItem, response, status, headers) {
        var data = response.object;
        var file = {productId: data.productId, id: data.fileId, src: data.fileLocation, name: data.fileName, size: data.fileSize, type: data.fileType};
        var newFileItem = new DownloadedFileItem(uploader1, file);
        var index = this.getIndexOfItem(fileItem);
        uploader1.queue[index] = newFileItem;
        console.info('onSuccessItem', newFileItem, response, status, headers);
    };
    uploader1.onErrorItem = function(fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader1.onCancelItem = function(fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader1.onCompleteItem = function(fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
    };
    uploader1.onCompleteAll = function() {
        console.info('onCompleteAll');
    };

});