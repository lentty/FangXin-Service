'use strict';

app.controller('SetAdminCtrl',
    function ($rootScope, $scope, $http,$localStorage, $modal, $state) {
        $rootScope.admin = $localStorage.admin;//用户登录后，本地存储用户信息
        console.log($rootScope.admin);
        $scope.open = function (template, ctrl, size) {
            var modalInstance = $modal.open({
                templateUrl: template,
                controller: ctrl,
                size: size
            });
            modalInstance.result.then(function (flag) {
                if(flag == 1){
                    console.log("successful!");
                }
            });
        };
        $scope.logout = function logout() {
            delete $localStorage.admin;
            console.log($scope.admin);//输出0
            console.log($localStorage.admin);//什么都没输出，$localStorage没有admin
            $http({
                url: '/user/logout',
                method: 'GET'
            }).success(function (data) {
                if (data.resultCode == 'success') {
                    console.log(data);
                } else if (data.resultCode == 'no_login_user') {
                    console.log('用户还未登录');
                }
                $state.go('access.signin');
            }).error(function () {
                console.log('请求失败');
            })
        }
    })
    .controller('updateAdminInfoModalCtrl',
        function ($scope, $rootScope, $http, $modalInstance) {
            $scope.admin = {};
            $http.get('/user/info')
                .success(function (data) {
                    console.log(data);
                    $scope.admin = data.object;
                });
            $scope.ok = function () {
                $http({
                    url: '/user/edit',
                    method: 'POST',
                    data: $scope.admin
                }).success(function (data) {
                    if (data.resultCode == 'success') {
                        var flag = 1;
                        $modalInstance.close(flag);
                    } else {
                        $scope.errorMsg = '添加失败!';
                    }
                }).error(function () {
                    $scope.errorMsg = '系统异常，添加失败!';
                })
            };
            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        })
    .controller('updateAdminPwdModalCtrl',
        function ($scope, $rootScope, $http, $modalInstance) {
            $scope.password = '';
            $scope.passwordAgain = '';
            $scope.ok = function () {
                console.log($scope.password);
                console.log($scope.passwordAgain);
                $http({
                    method: 'POST',
                    url: '/user/editPassword/',
                    data: $scope.password
                }).success(function (data) {
                    if (data.resultCode == 'success') {
                        var flag = 1;
                        $modalInstance.close(flag);
                    } else {
                        $scope.errorMsg = '添加失败!';
                    }
                }).error(function () {
                    $scope.errorMsg = '系统异常，添加失败!';
                })
            };
            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        });
