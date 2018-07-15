'use strict';

//signin controller
app.controller('SigninFormCtrl',['$scope', '$http', '$state', '$localStorage', '$rootScope',
    function ($scope, $http, $state, $localStorage, $rootScope) {
    $scope.user = {};
    // $rootScope.$storage = $localStorage.$default({
    //     admin: {}
    // });
    $scope.authError = null;
    $scope.login = function () {
        $http({
            url: '/admin/login',
            method: 'POST',
            data: $scope.user
        }).success(function (data) {
            if(data.resultCode === 'success'){
               // $rootScope.$storage.admin = data.object;
                console.log(data.object);
                $localStorage.admin = data.object;
                $rootScope.userRoleType = $localStorage.admin.type;
               // console.log($rootScope.admin);
                $state.go('app.dashboard');
            }else{
                $scope.authError = '登录名或密码错误!';
            }
        }).error(function () {
            $scope.authError = '登录失败';
        })
    };
}]);