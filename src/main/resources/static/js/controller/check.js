'use strict';

app.controller('CheckCtrl',
    function ($scope, $resource, $http,  $modal, $log) {
    // console.log(DTOptionsBuilder);
    // console.log(DTColumnDefBuilder);
        $scope.rooms = [];

        $scope.orderProp = 'price';



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
                url: '/rooms',
                method: 'POST',
                dataType: 'json',
             //    headers: {
             //        'Content-Type': 'application/x-www-form-urlencoded'
             // },
                data: $scope.pager
            })
                .success(function (data) {
                    console.log(data);
                    $scope.rooms = data.data;
                    $scope.totalItems = data.recordsTotal;
                    angular.forEach($scope.rooms, function (room) {
                        if(room.roomState == 1){
                            room.type = true;//通过
                            room.typeName = '通过';
                        }else{
                            room.type = false;//未通过
                            room.typeName = '未通过';
                        }
                    })
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

    $scope.changeState = function (id, type) {
        console.log(id);
        console.log(type);
        if(type == true){
           var state = 1;
        }else{
            state = 0;
        }
        console.log(state);
        $http({
            url: '/room/edit',//修改房间状态的路径
            method: 'POST',
            data: {
                'roomState': state,
                'id': id
            }
        })
            .success(function (data) {
                if(data.resultCode == 'success'){
                   init();
                }else{
                    console.log('修改失败');
                }
            })
            .error(function () {
               console.log('修改失败');
            })
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
.controller('ModalInstanceCtrl', function ($scope, $modalInstance, message, flag) {
    //这里的数据才是弹出的窗口真正拿到的数据
    $scope.message = message;
    $scope.flag = flag;
    
    $scope.ok = function () {
        $modalInstance.close($scope.message);
        console.log("确定");
    };
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
})
.controller('ModalInstanceCtrl1', function ($scope, $modalInstance, $http, selected, message) {//接入后台删除数据时注册$rootScope
    $scope.message = message;
    $scope.flag = 2;//显示问号
    $scope.selected = selected;//要删除的数据
    $scope.ok = function () {
        console.log($scope.selected);
        delObj();
        console.log($scope.flag);

    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    var delObj = function () {


        if($scope.selected != " "){
            $http({
                method: 'post',
                url: "/rooms/delete",
                data: $scope.selected
            }).success(function (data) {
                if(data.resultCode == 'success'){
                    console.log(data.resultCode);
                    $scope.flag = 1;
                    $modalInstance.close($scope.flag);
                }else{
                    message = "删除失败！";
                    $scope.flag = 2;
                    $modalInstance.close($scope.flag);
                }
            }).error(function () {
                message = "系统异常，删除失败！";
                $scope.flag = 2;
                $modalInstance.close($scope.flag);
            })
        }


    }

});