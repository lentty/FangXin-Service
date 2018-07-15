//config

var app =
    angular.module('app')
    .config(['$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
        function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide) {

            //lazy controller, directive and service
            app.controller = $controllerProvider.register;
            app.directive  = $compileProvider.directive;
            app.filter     = $filterProvider.register;
            app.factory    = $provide.factory;
            app.service    = $provide.service;
            app.constant   = $provide.constant;
            app.value      = $provide.value;

            // $httpProvider.responseInterceptors.push('securityInterceptor');
        }

    ]);
// .provider('securityInterceptor', function () {
//     this.$get = function ($location, $q) {
//         return function (promise) {
//             return promise.then(null, function (response) {
//                 if(response.status === 403 || response.status === 401){
//                     $location.path('/unauthoried');
//                 }
//                 return $q.reject(response);
//             })
//         }
//     }
// })