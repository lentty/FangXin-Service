'use strict';


/**
 * config for the router
 */
angular.module('app')
.run([
    '$rootScope', '$state', '$stateParams', '$localStorage',
    function ($rootScope, $state, $stateParams, $localStorage) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        $rootScope.admin = $localStorage.admin;
        console.log($rootScope.admin);
        $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {

            }
        );
        $rootScope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams) {
            event.preventDefault();
            $state.go('access.404');
        });
    }
])
.config([
    '$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider
            .otherwise('/access/signin');
        $stateProvider
            .state('app',{
                abstract: true,
                url: '/app',
                templateUrl: 'tpl/app.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controller/settings.js']);
                        }
                    ]
                }

            })
            .state('app.dashboard', {//主页
                url: '/dashboard',
                templateUrl: 'tpl/dashboard.html',
                resolve: {
                    deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){//oclazyload懒加载：可以在路由切换时方便的加载子模块和依赖资源
                            return $ocLazyLoad.load(['js/controller/chart.js']);
                        }
                    ]
                }
            })
            .state('app.brands',{
                url: '/brands',
                templateUrl: 'tpl/brand/brands.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controller/brand/brands.js']);
                        }
                    ]
                }
            })
            .state('app.brand',{
                url: '/brands/:brandId',
                templateUrl: 'tpl/brand/brand-edit.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controller/brand/brand-edit.js']);
                        }
                    ]
                }
            })
            .state('app.products',{
                url: '/products',
                templateUrl: 'tpl/product/products.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controller/product/products.js']);
                        }
                    ]
                }
            })
            .state('app.product',{
                url: '/products/:productId',
                templateUrl: 'tpl/product/product-photos.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controller/product/product-photos.js', 'css/angularFileUpload/angular.fileupload-ui.css']);
                        }
                    ]
                }
            })
            .state('access',{
                url: '/access',
                template: '<div ui-view class="fade-in-right-big smooth"></div>'
            })
            .state('access.signin',{
                url: '/signin',
                templateUrl: 'tpl/signin.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controller/signin.js']);
                        }
                    ]
                }
            })
            .state('access.forgotpwd',{
                url: '/forgotpwd',
                templateUrl: 'tpl/forgotpwd.html'
            })
            .state('access.404', {
                url: '/404',
                templateUrl: 'tpl/404.jsp'
            })
            .state('app.users', {
                url: '/users',
                template: '<div ui-view></div>'
            })
            .state('app.users.admins', {
                url: '/admins',
                templateUrl: 'tpl/users/admins.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controller/users/admins.js']);
                        }
                    ]
                }
            })
            .state('app.users.app', {
                url: '/app',
                templateUrl: 'tpl/users/appUsers.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controller/users/appUsers.js']);
                        }
                    ]
                }
            })
    }
]);