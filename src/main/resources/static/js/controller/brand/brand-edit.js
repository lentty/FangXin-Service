'use strict';

app.controller('editBrandCtrl', function ($scope,$state, $http ,$stateParams, $modal, $log) {

    var brandId = $stateParams.brandId;
    console.log('brandId: '+ brandId);
    $scope.brand = {};
    var init = function () {
        $http.get('/brand/'+ brandId)
            .success(function (data) {
                if(data.resultCode == 'success'){
                    console.log(data);
                    $scope.brand = data.object;
                }else{
                    if(data.errorInfo == 'NO_LOGIN_USER'){
                        console.log('用户还未登录');
                        $state.go('access.signin');
                    }
                }
            })
            .error(function () {
                console.log('请求失败');
            });
    };
    if(brandId){
        init();
    }
    $scope.upsertCategory = function(brandId, category){
        console.log('upsert category from brand: ' + brandId);
        var modalInstance = $modal.open({
            templateUrl: 'upsertCategory.html',
            controller: 'upsertCategoryModalCtrl',
            size: 'md',
            resolve: {
                brandId: function () {
                    return brandId;
                },
                category: function () {
                    return category;
                }
            }
        });
        modalInstance.result.then(function (flag) {
            if(flag == 1){
                console.log("successful upsert category!");
                init();
            }
        });
    };

    $scope.editBrandPic = function(brandId){
        console.log('edit pic for brandId: ' + brandId);
        var modalInstance = $modal.open({
            templateUrl: 'editBrandPic.html',
            controller: 'editBrandPicModalCtrl',
            size: 'md',
            resolve: {
                brandId: function () {
                    return brandId;
                }
            }
        });
        modalInstance.result.then(function (flag) {
            if(flag == 1){
                console.log("successful upload pic!");
                init();
            }
        });
    };

    $scope.deleteCategory = function(id, name){
        console.log('delete category id: '+ id);
        console.log('delete category name: '+ name);
        var modalInstance = $modal.open({
            templateUrl: 'deleteCateConfirmation.html',
            controller: 'deleteCateConfirmModalCtrl',
            size: 'sm',
            resolve: {
                id: function () {
                    return id;
                },
                name: function () {
                    return name;
                }
            }
        });
        modalInstance.result.then(function (flag) {
            if(flag == 1){
                console.log("successful!");
                init();
            }
        });
    };

    $scope.reset = function () {
        $scope.brand = {};
    };

    $scope.navigateBack = function(){
       history.back();
    };

    $scope.submit = function () {
        console.log($scope.brand);
        $http({
            url: '/brand/upsert',
            method: 'POST',
            data: $scope.brand
        }) .success(function (data) {
                if(data.resultCode == 'success'){
                    var result = 1;
                    $scope.open(result);
                }else{
                    var result = 2;
                   $scope.open(result);
                }
            })
            .error(function () {
                var result = 2;
                $scope.open(result);
            })
    };




    $scope.open = function (result) {
        var modalInstance = $modal.open({
            templateUrl: 'addEnd.html',
            controller: 'ModalInstanceCtrl7',
            size: 'sm',
            resolve:{
                flag: function () {
                    return result;
                }
            }
        });

        modalInstance.result.then(function (flag) {
            if(flag == 1){
            }
            $log.info('Modal dismissed at :' + new Date());
        });
    };
 });