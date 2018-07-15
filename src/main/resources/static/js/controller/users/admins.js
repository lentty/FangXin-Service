'use strict';

app.controller('AdminUserCtrl',
    function ($scope, $resource, $http, $state, $modal) {
        $scope.admins = [];
        $scope.totalItems = 0;
        $scope.orderProp = "lastModifiedDate";
        var getAdminUsers = function () {
            $http({
                url: '/user/users/1',
                method: 'GET'
            }).success(function (data) {
                if (data.resultCode == 'success') {
                    console.log(data);
                    $scope.admins = data.object.data;
                    $scope.totalItems = data.object.totalCount;
                } else if (data.resultCode == 'no_login_user') {
                    console.log('用户还未登录');
                    $state.go('access.signin');
                }
            }).error(function () {
                    console.log('请求失败');
                })
        };
        var init = function () {
            getAdminUsers();
        };
        init();
        $scope.getAdminDetail = function(adminId){
            console.log('get admin user detail');
            var modalInstance = $modal.open({
                templateUrl: 'getAdminDetail.html',
                controller: 'getAdminDetailModalCtrl',
                size: 'lg',
                resolve: {
                    adminId: function () {
                        return adminId;
                    }
                }
            });
            modalInstance.result.then(function (flag) {
                if(flag == 1){
                    console.log("successful!");
                    getAdminUsers();
                }
            });
        };
        $scope.editAdmin = function(adminId){
            console.log('edit admin');
            var modalInstance = $modal.open({
                templateUrl: 'editAdmin.html',
                controller: 'editAdminModalCtrl',
                size: 'md',
                resolve: {
                    adminId: function () {
                        return adminId;
                    }
                }
            });
            modalInstance.result.then(function (flag) {
                if(flag == 1){
                    console.log("successful!");
                    getAdminUsers();
                }
            });
        };

        $scope.deleteAdmin = function(id, username){
            console.log('delete admin id: '+ id);
            console.log('delete admin user name: '+ username);
            var modalInstance = $modal.open({
                templateUrl: 'deleteAdminConfirmation.html',
                controller: 'deleteAdminConfirmModalCtrl',
                size: 'sm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    username: function () {
                        return username;
                    }
                }
            });
            modalInstance.result.then(function (flag) {
                if(flag == 1){
                    console.log("successful!");
                    getAdminUsers();
                }
            });
        };
    });