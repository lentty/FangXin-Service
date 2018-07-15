'use strict';

app.controller('BrandCtrl',
    function ($scope, $resource, $http, $state, $modal, $log) {
        $scope.brands = [];
        $scope.start = 0;
        $scope.maxSize = 10;
        $scope.totalItems = 0;
        $scope.currentPage = 1;
        $scope.pager = {
            draw: $scope.currentPage,
            start: $scope.start,
            length: $scope.maxSize
        };
        var init = function () {
            $http({
                url: '/brand/admin',
                method: 'POST',
                dataType: 'json',
                data: $scope.pager
            }).success(function (data) {
                  if(data.resultCode == 'success'){
                      console.log(data);
                      $scope.brands = data.object.data;
                      $scope.totalItems = data.object.totalCount;
                  }else if(data.resultCode == 'no_login_user'){
                      console.log('用户还未登录');
                      $state.go('access.signin');
                  }
                })
                .error(function () {
                    console.log('请求失败');
                })
        };
        init();
        $scope.getData = function () {
            console.log('到这了');
            console.log($scope.currentPage);
            $scope.start = ($scope.currentPage - 1) * 10 + 1;
            console.log($scope.start);
            $scope.pager = {
                draw: $scope.currentPage,
                start: $scope.start,
                length: $scope.maxSize
            };
            console.log($scope.pager);
            init();
        };
        $scope.upsertBrand = function(brand){
            console.log('upsert brand');
            var modalInstance = $modal.open({
                templateUrl: 'upsertBrand.html',
                controller: 'upsertBrandModalCtrl',
                size: 'md',
                resolve: {
                    brand: function () {
                        return brand;
                    }
                }
            });
            modalInstance.result.then(function (flag) {
                if(flag == 1){
                    console.log("successful!");
                    $scope.getData();
                }
            });
        };

        $scope.deleteBrand = function(id, name){
            console.log('delete brand id: '+ id);
            console.log('delete brand name: '+ name);
            var modalInstance = $modal.open({
                templateUrl: 'deleteBrandConfirmation.html',
                controller: 'deleteBrandConfirmModalCtrl',
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
                    $scope.getData();
                }
            });
        };


        $scope.message = '';//弹出框内容
        $scope.flag = 1;//弹出框显示感叹号还是问号

        $scope.selected = []; //复选框被选择的项
        var str = '';
        var f = '';//是否点击了全选，是为a
        $scope.x = false;//默认未选中

        $scope.all = function (c,v) {//全选
            console.log(c);
            if(c==true){
                $scope.x = true;
                for(var i=0;i<v.length;i++){
                    $scope.selected[i] =  v[i].id;
                }
                f = 'a';
            }else{
                $scope.x = false;
                $scope.selected= [];
                f = '';

            }
            console.log(f);
            console.log($scope.selected);
        };

        $scope.select = function (id,x) {//单选或多选
            // if(f == 'a'){//在全选的基础上操作
            //     str = $scope.selected.join(',') ;
            // }
            // if(x == true){//选中
            //     str = str.replace(z + ',', '');//取消选中
            // }
            console.log(f);
            console.log(x);
            if(f == 'a'){//如果是全选状态下
                var index = $scope.selected.indexOf(id);
                $scope.selected.splice(index,1);
            }else{
                if(x == false){//之前就是选中状态
                    var index = $scope.selected.indexOf(id);
                    $scope.selected.splice(index,1);
                }else{//之前是未选中状态
                    $scope.selected.push(id);
                }
            }
            // $scope.selected = (str.substr(0,str.length-1)).split(',');
            console.log($scope.selected);
        };

        //编辑确认框
        $scope.editSelected = function (id, state) {
            var url = '/room/edit';
            var modalInstance = $modal.open({
                templateUrl: 'editRoom.html',
                controller: 'ModalInstanceCtrl10',
                size: 'sm',
                resolve: {
                    id: function () {
                        return id;
                    },
                    url: function () {
                        return url;
                    },
                    state: function () {
                        return state;
                    }
                }

            });

            modalInstance.result.then(function (flag) {
                console.log(flag);
                var modalInstance = $modal.open({
                    templateUrl: 'editEnd.html',
                    controller: 'ModalInstanceCtrl7',
                    size: 'sm',
                    resolve: {
                        flag: function () {
                            return flag
                        }
                    }

                });


                modalInstance.result.then(function (flag) {

                    if(flag === 1){
                        console.log("修改成功");
                        init();
                    }
                    console.log("结束！");
                },function (flag) {
                    if(flag === 1){
                        console.log("修改成功");
                        init();
                    }
                    $log.info('Modal dismissed at :' + new Date());
                });
                // $scope.dtInstance.reloadData();
            },function () {
                $log.info('Modal dismissed at :' + new Date());
            });
        };


        //删除确认框
        $scope.deleteSelected = function () {
            if($scope.selected.length == 0){
                $scope.message = '请选择一条数据！';
                $scope.flag = 1;
                $scope.open('myModalContent.html', 'ModalInstanceCtrl', 'sm');
            }else{
                $scope.message = '是否确定删除？';
                $scope.flag = 2;
                $scope.openDelObj('myModalContent.html', 'ModalInstanceCtrl1', 'sm');
            }
            console.log($scope.flag);
            console.log($scope.message);
        };


        $scope.add = function () {
            //跳到添加页面
        };



        $scope.open = function (template, ctrl, size) {
            var modalInstance = $modal.open({
                templateUrl: template,
                controller: ctrl,
                size: size,
                resolve:{
                    message: function () {
                        return $scope.message
                    },
                    flag: function () {
                        return $scope.flag
                    }
                }
            });

            modalInstance.result.then(function () {

            },function () {
                $log.info('Modal dismissed at :' + new Date());
            });
        };

        $scope.openDelObj = function (template, ctrl, size) {
            var modalInstance = $modal.open({
                templateUrl: template,
                controller: ctrl,
                size: size,
                resolve: {
                    message: function () {
                        return $scope.message
                    },
                    selected: function () {
                        return $scope.selected
                    }
                }
            });

            modalInstance.result.then(function (flag) {

                console.log("什么时候进入这里");

                var modalInstance = $modal.open({
                    templateUrl: 'removeEnd.html',
                    controller: 'ModalInstanceCtrl7',
                    size: 'sm',
                    resolve: {
                        flag: function () {
                            return flag
                        }
                    }

                });


                modalInstance.result.then(function (flag) {

                    if(flag === 1){
                        console.log("删除成功");
                        init();
                    }else {
                        console.log("删除失败！");
                    }
                    console.log("结束！");
                },function (flag) {
                    console.log("取消");

                    if(flag === 1){
                        console.log("删除成功");
                        init();
                    }else {
                        console.log("删除失败！");
                    }
                    $log.info('Modal dismissed at :' + new Date());
                });

            },function () {
                $log.info('Modal dismissed at :' + new Date());
            });
        };

    })