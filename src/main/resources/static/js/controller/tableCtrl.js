'use strict';

app.controller('ModalInstanceCtrl4', function ($scope, $http, $modalInstance, $window) {//服务端模式要加dtInstance
    $scope.user = {
        sex: '男'
    };
    $scope.flag = 2;
    // $scope.dtInstance = dtInstance;
    $scope.ok = function () {
        console.log($scope.user);
        // $http({
        //     method: 'post',
        //     url: '',
        //     data: $scope.user
        // }).success(function (resp) {
        //     if(resp.status === 'success'){
        //         $window.alert("添加成功！");
        //         dtInstance.reloadData();
        //     }else{
        //         $window.alert("添加失败！");
        //     }
        // }).error(function () {
        //     $window.alert("系统异常，添加失败！");
        // });

        $modalInstance.close($scope.flag);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
        console.log("发生了什么？");
    };
})
    .controller('ModalInstanceCtrl5', function ($scope, $http, $modalInstance, url1, url2) {

        $scope.flag = 2;
        console.log(url1);
        console.log(url2);
        $http.get(url1)
            .success(function (data) {
                $scope.user = data;
                console.log($scope.user);
            })
            .error(function () {
                $modalInstance.close();
            });

        $scope.ok = function () {
            // console.log(id);
            console.log($scope.user);

            $http({
                method: 'post',
                url: url2,
                data: $scope.user
            }).success(function (data) {
                if(data.resultCode === 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else {
                    flag = 2;
                    $modalInstance.close(flag);
                }
            }).error(function () {
                var flag = 2;
                $modalInstance.close(flag);
            });

        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('ModalInstanceCtrl6', function ($scope, $modalInstance, $http, selected, message, url) {
        console.log(url);
        $scope.message = message;
        $scope.selected = selected;//要删除的数据
        $scope.flag = 2;//显示问号
        $scope.ok = function () {
            console.log($scope.selected);
            delObj();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

        var delObj = function () {


            if($scope.selected != " "){
                $http({
                    method: 'post',
                    url: url,
                    data: $scope.selected
                }).success(function (data) {
                    if(data.resultCode == 'success'){
                        message = "删除成功！";
                        var flag = 1;
                        $modalInstance.close(flag);
                    }else{
                        message = "删除失败！";
                        flag = 2;
                        $modalInstance.close(flag);
                    }
                }).error(function () {
                    var flag = 2;
                    $modalInstance.close(flag);
                })
            }


        }
    })
    .controller('ModalInstanceCtrl7', function ($scope, $modalInstance, flag) {
        $scope.message = '';
        $scope.flag = flag;
        if($scope.flag === 1) {
            $scope.message = "操作成功！";
        }else {
            $scope.message = "操作失败！";
        }

        $scope.ok = function () {
            console.log($scope.message);
            console.log($scope.flag);
            $modalInstance.close($scope.flag);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss($scope.flag);
        };
    })
    .controller('ModalInstanceCtrl8', function ($scope, $http, $modalInstance, id) {
        $scope.id = id;//要改变管理区域的区域管理员的id
        $scope.provinceId = 'aaa';
        $scope.cityId = 'bbb';
        $scope.areas = [];
        $scope.smallAreas = [];
        $scope.areaId = '';
        $http.get('/area/provinces')
            .success(function (data) {
                if(data.resultCode == 'success') {
                    $scope.areas = data.object.sonAreaList;
                    console.log($scope.areas);
                }else{
                    console.log('请求失败');
                }
            })
            .error(function () {
                console.log('请求失败');
            });

        $scope.loadCity = function (id) {
            console.log(id);
            $http.get('/area/city/'+id)
                .success(function (data) {
                    if(data.resultCode == 'success'){
                        $scope.smallAreas = data.object;
                        console.log($scope.smallAreas);
                    }else{
                        console.log('请求失败');
                        $scope.smallAreas = [];
                        $scope.cityId = 'bbb';
                    }
                })
                .error(function () {
                    console.log('请求失败');
                })
        };

        $scope.ok = function () {
            if($scope.provinceId == 'aaa' && $scope.cityId == 'bbb'){
                $scope.errorMessage = '请选择地区';
                return;
            }else if($scope.provinceId != 'aaa' && $scope.cityId == 'bbb'){
                $scope.errorMessage = '';
                $scope.areaId = $scope.provinceId;
            }else if($scope.provinceId != 'aaa' && $scope.cityId != 'bbb'){
                $scope.errorMessage = '';
                $scope.areaId = $scope.cityId;
            }else{
                $scope.errorMessage = '';
            }
            console.log($scope.areaId);
             var ids = [$scope.id];
             var areasId = [$scope.areaId];
            console.log(ids);
            console.log(areasId);
            $http({
                url: '/admin/areaUpdate',//区域管理员修改负责区域
                method: 'POST',
                data: {
                    'ids': ids,
                    'areasId' : areasId
                }
            })
                .success(function (data) {
                    if(data.resultCode == 'success'){
                        var flag = 1;
                        $modalInstance.close(flag);
                    }else{
                        flag = 2;
                        $modalInstance.close(flag);
                    }
                })
                .error(function () {
                    var flag = 2;
                    $modalInstance.close(flag);
                })
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('ModalInstanceCtrl9', function ($scope, $modalInstance, url) {
        $scope.url = url;
        console.log($scope.url);

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('ModalInstanceCtrl10', function ($scope,$http, $modalInstance ,id, url, state) {
        console.log(url);
        console.log(id);

        $scope.state = state;

        $scope.ok = function () {
            console.log($scope.state);
            $http({
                url: url,//修改房间状态的路径
                method: 'POST',
                data: {
                    'roomState': $scope.state,
                    'id': id
                }
            })
                .success(function (data) {
                    if(data.resultCode == 'success'){
                        var flag = 1;
                        $modalInstance.close(flag);
                    }else{
                        flag = 2;
                        $modalInstance.close(flag);
                    }
                })
                .error(function () {
                    var flag = 2;
                    $modalInstance.close(flag);
                })

        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('upsertBrandModalCtrl', function ($scope, $http, $modalInstance, brand){
        console.log('modal brand: '+ $scope.brand);
        if (brand) {
            $scope.brand = brand;
        } else {
            $scope.brand = {};
            $scope.brand.status = true;
        }
        $scope.errorMsg = null;
        $scope.ok = function () {
            $http({
                url: '/brand/upsert',
                method: 'POST',
                data: $scope.brand
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else{
                    if(data.status == -1){
                        $scope.errorMsg = '品牌已经存在!';
                    }
                }
            }).error(function () {
                $scope.errorMsg = '系统异常，添加失败!';
            })
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('deleteBrandConfirmModalCtrl', function ($scope, $http, $modalInstance,id, name){
        console.log('id in deleteBrandConfirmModal: '+ id);
        console.log('name in deleteBrandConfirmModal: '+ name);
        $scope.name = name;
        $scope.ok = function () {
            $http({
                url: '/brand/delete/'+ id,
                method: 'DELETE'
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else{
                    if(data.status <= 0){
                        $scope.errorMsg = '删除失败!';
                    }
                }
            }).error(function () {
                $scope.errorMsg = '系统异常，添加失败!';
            })
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('upsertCategoryModalCtrl', function ($scope, $http, $modalInstance, category, brandId){
        if (category) {
            $scope.category = category;
        } else {
            $scope.category = {};
            $scope.category.brandId = brandId;
        }
        $scope.errorMsg = null;
        $scope.ok = function () {
            $http({
                url: '/category/upsert',
                method: 'POST',
                data: $scope.category
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else{
                    if(data.status == -1){
                        $scope.errorMsg = '分类已经存在!';
                    }
                }
            }).error(function () {
                $scope.errorMsg = '系统异常，添加失败!';
            })
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('editCatePicModalCtrl', function ($scope, Upload, $http, $state, $modalInstance, cateId){
        $scope.errorMsg = null;
        $scope.successMsg = null;
        $scope.uploadPic = function(file) {
            Upload.upload({
                url: '/category/upload',
                data: {cateId: cateId, file: file}
            }).then(function (response) {
                console.log('response data: '+ response.data);
                if(response.data.resultCode == 'success'){
                    $scope.successMsg = '上传成功';
                }else{
                    if(response.data.errorInfo == 'NO_LOGIN_USER'){
                        console.log('用户还未登录');
                        $state.go('access.signin');
                    }else{
                        $scope.errorMsg='上传失败, 状态：' + response.data.status;
                    }
                }
            },function(response){
                $scope.errorMsg = '系统异常，上传失败!';
            },function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
            });
        }
        $scope.ok = function () {
            if($scope.successMsg){
                var flag = 1;
                $modalInstance.close(flag);
            }else{
                var flag = 0;
                $modalInstance.close(flag);
            }
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('deleteCateConfirmModalCtrl', function ($scope, $http, $modalInstance,id, name){
        console.log('id in deleteCateConfirmModalCtrl: '+ id);
        console.log('name in deleteCateConfirmModalCtrl: '+ name);
        $scope.name = name;
        $scope.ok = function () {
            $http({
                url: '/category/delete/'+ id,
                method: 'DELETE'
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else{
                    if(data.status <= 0){
                        $scope.errorMsg = '删除失败!';
                    }
                }
            }).error(function () {
                $scope.errorMsg = '系统异常，添加失败!';
            })
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('getProductDetailModalCtrl', function ($scope, $rootScope, $http, $modalInstance, productId){
        console.log('modal productId: '+ productId);
        $scope.product = {};
        $scope.p_status = '正常';
        var init = function () {
            $http.get('/product/' + productId)
                .success(function (data) {
                    if(data.resultCode == 'success'){
                        $scope.product = data.object;
                        if($scope.product.status == 0){
                            $scope.p_status = '已下架';
                        }else if($scope.product.status == 2){
                            $scope.p_status = '特价';
                        }
                    }else if(data.resultCode == 'error') {
                        console.log('没有登录信息');
                        $state.go('access.signin');
                    }
                })
                .error(function () {
                    console.log('请求失败');
                });
        };
        init();
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('editProductModalCtrl', function ($scope, $rootScope, $http, $modalInstance, productId){
        console.log('modal productId: '+ productId);
        $scope.errorMsg = null;
        $scope.brandList = [];
        $rootScope.brandList = [];
        var init = function () {
            $http.get('/brand/list')
                .success(function (data) {
                        $scope.brandList = data;
                        $rootScope.brandList = data;
                })
                .error(function () {
                    console.log('请求失败');
                    $scope.errorMsg = '请求失败';
                });
        };
        init();
        $scope.product = {};
        $scope.product.details = [];
        if (productId) {
            $http.get('/product/' + productId)
                .success(function (data) {
                    if(data.resultCode == 'success'){
                        $scope.product = data.object;
                        angular.forEach($rootScope.brandList, function (brand) {
                            if(brand.id == $scope.product.brandId){
                                $scope.brand = brand;
                                var cateList = brand.cateList;
                                if(cateList){
                                    angular.forEach(cateList, function (category) {
                                        if(category.id == $scope.product.catId){
                                            $scope.category = category;
                                        }
                                    });
                                }
                            }
                        });
                    }else if(data.resultCode == 'error') {
                        console.log('没有登录信息');
                        $state.go('access.signin');
                    }
                })
                .error(function () {
                    console.log('请求失败');
                });
        } else {
            $scope.product.status = 1;
        }
        $scope.addAttribute = function($index){
            $scope.product.details.splice($index+1, 0, {attriKey:"", attriValue:""});
        }
        $scope.deleteAttribute = function($index){
            $scope.product.details.splice($index, 1);
        }
        $scope.ok = function () {
            $scope.product.brandId = $scope.brand.id;
            $scope.product.catId = $scope.category.id;
            $http({
                url: '/product/edit',
                method: 'POST',
                data: $scope.product
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else{
                    if(data.status == -1){
                        $scope.errorMsg = '产品名字已经存在!';
                    }
                }
            }).error(function () {
                $scope.errorMsg = '系统异常，添加失败!';
            })
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('deleteProductConfirmModalCtrl', function ($scope, $http, $modalInstance,id, name){
        console.log('id in deleteProductConfirmModalCtrl: '+ id);
        console.log('name in deleteProductConfirmModalCtrl: '+ name);
        $scope.name = name;
        $scope.ok = function () {
            $http({
                url: '/product/delete/'+ id,
                method: 'DELETE'
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else{
                    if(data.status <= 0){
                        $scope.errorMsg = '删除失败!';
                    }
                }
            }).error(function () {
                $scope.errorMsg = '系统异常，添加失败!';
            })
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('deleteAllImagesConfirmModalCtrl', function ($scope, $http, $modalInstance, uploader){
        $scope.ok = function(){
            angular.forEach(uploader.queue, function (item) {
                if(item.file.src){
                    item.remove();
                }else{
                    uploader.removeFromQueue(item);
                }
            })
            $modalInstance.close(1);
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('editAdminModalCtrl', function ($scope, $http, $modalInstance, adminId){
        console.log('modal adminId: '+ adminId);
        $scope.errorMsg = null;
        $scope.admin = {};
        if (adminId) {
            $http.get('/user/' + adminId)
                .success(function (data) {
                    if(data.resultCode == 'success'){
                        $scope.admin = data.object;
                    }else if(data.resultCode == 'error') {
                        console.log('没有登录信息');
                        $state.go('access.signin');
                    }
                })
                .error(function () {
                    console.log('请求失败');
                });
        } else {
            $scope.admin.status = 1;
        }
        $scope.ok = function () {
            $scope.admin.type = 1;
            $http({
                url: '/user/edit',
                method: 'POST',
                data: $scope.admin
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else{
                    if(data.status == -1){
                        $scope.errorMsg = '用户名已经存在!';
                    }
                }
            }).error(function () {
                $scope.errorMsg = '系统异常，添加失败!';
            })
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('getAdminDetailModalCtrl', function ($scope, $rootScope, $http, $modalInstance, adminId){
        console.log('modal adminId: '+ adminId);
        $scope.admin = {};
        var init = function () {
            $http.get('/user/' + adminId)
                .success(function (data) {
                    if(data.resultCode == 'success'){
                        $scope.admin = data.object;
                    }else if(data.resultCode == 'error') {
                        console.log('没有登录信息');
                        $state.go('access.signin');
                    }
                })
                .error(function () {
                    console.log('请求失败');
                });
        };
        init();
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    })
    .controller('deleteAdminConfirmModalCtrl', function ($scope, $http, $modalInstance, id, username){
        $scope.username = username;
        $scope.ok = function () {
            $http({
                url: '/user/delete/'+ id,
                method: 'DELETE'
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    var flag = 1;
                    $modalInstance.close(flag);
                }else{
                    if(data.status <= 0){
                        $scope.errorMsg = '删除失败!';
                    }
                }
            }).error(function () {
                $scope.errorMsg = '系统异常，删除失败!';
            })
        };
        $scope.cancel = function () {
            $modalInstance.dismiss();
        };
    });



