'use strict';

angular.module('app')
    .controller('AppCtrl',['$scope', '$localStorage', '$window', '$rootScope', '$state',
        function ($scope, $localStorage, $window, $rootScope, $state) {
            $rootScope.userRoleType = 1;
            if($localStorage.admin){
                $rootScope.userRoleType = $localStorage.admin.type;
            }
            console.log('userRoleType: '+ $rootScope.userRoleType);
            $scope.app = {
                // for chart colors
                color: {
                    primary: '#7266ba',
                    info:    '#23b7e5',
                    success: '#27c24c',
                    warning: '#fad733',
                    danger:  '#f05050',
                    light:   '#e8eff0',
                    dark:    '#3a3f51',
                    black:   '#1c2b36'
                },
                settings: {
                    themeID: 1,
                    navbarHeaderColor: 'bg-black',
                    navbarCollapseColor: 'bg-white-only',
                    asideColor: 'bg-black',
                    headerFixed: true,
                    asideFixed: false,
                    asideFolded: false,
                    asideDock: false,
                    container: false
                }
            };

            // save settings to local storage
            if ( angular.isDefined($localStorage.settings) ) {
                $scope.app.settings = $localStorage.settings;
            } else {
                $localStorage.settings = $scope.app.settings;
            }
            $scope.$watch('app.settings', function(){
                if( $scope.app.settings.asideDock  &&  $scope.app.settings.asideFixed ){
                    // aside dock and fixed must set the header fixed.
                    $scope.app.settings.headerFixed = true;
                }
                if( !$scope.app.settings.asideFixed){
                    $scope.app.settings.asideFixed = true;
                }
                // save to local storage
                $localStorage.settings = $scope.app.settings;
            }, true);





        }


]);