'use strict';

app.controller('AppUserCtrl',
    function ($scope, $resource, $http, $state, $modal) {
        $scope.admins = [];
        $scope.totalItems = 0;
        $scope.orderProp = "lastModifiedDate";
        var getAppUsers = function () {
            $http({
                url: '/user/users/0',
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
            getAppUsers();
        };
        init();
        $scope.getAppUserDetail = function(adminId){
            console.log('get admin user detail');
            var modalInstance = $modal.open({
                templateUrl: 'getAppUserDetail.html',
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
                    getAppUsers();
                }
            });
        };
    });