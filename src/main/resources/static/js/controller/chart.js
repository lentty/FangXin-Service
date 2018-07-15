'use strict';

/* Controllers */

app.controller('FlotChartDemoCtrl', ['$scope','$http','$state', function($scope, $http, $state) {


    $scope.d = [ [1,6.5],[2,6.5],[3,7],[4,8],[5,7.5],[6,7],[7,6.8],[8,7],[9,7.2],[10,7],[11,6.8],[12,7] ];

    $scope.d0_1 = [ [0,7],[1,6.5],[2,12.5],[3,7],[4,9],[5,6],[6,11],[7,6.5],[8,8],[9,7] ];

    $scope.d0_2 = [ [0,4],[1,4.5],[2,7],[3,4.5],[4,3],[5,3.5],[6,6],[7,3],[8,4],[9,3] ];

    $scope.d1_1 = [ [10, 120], [20, 70], [30, 70], [40, 60] ];

    $scope.d1_2 = [ [10, 50],  [20, 60], [30, 90],  [40, 35] ];

    $scope.d1_3 = [ [10, 80],  [20, 40], [30, 30],  [40, 20] ];

    $scope.d2 = [];

    $scope.d5 = [[1,20], [2,25], [3,56], [4,46], [5,60], [6,30], [7,68], [8,100], [9,30], [10,67], [11,25], [12,66]];

    for (var i = 0; i < 20; ++i) {
      $scope.d2.push([i, Math.sin(i)]);
    }   

    $scope.d3 = [ 
      { label: "iPhone5S", data: 40 }, 
      { label: "iPad Mini", data: 10 },
      { label: "iPad Mini Retina", data: 20 },
      { label: "iPhone4S", data: 12 },
      { label: "iPad Air", data: 18 }
    ];

    $scope.refreshData = function(){
      $scope.d0_1 = $scope.d0_2;
    };

    $scope.getRandomData = function() {
      var data = [],
      totalPoints = 10;
      if (data.length > 0)
        data = data.slice(1);
      while (data.length < totalPoints) {
        var prev = data.length > 0 ? data[data.length - 1] : 50,
          y = prev + Math.random() * 10 - 5;
        if (y < 0) {
          y = 0;
        } else if (y > 100) {
          y = 100;
        }
        data.push(y);
      }
      // Zip the generated y values with the x values
      var res = [];
      for (var i = 0; i < data.length; ++i) {
        res.push([i, data[i]])
      }
      // console.log(res);
      res = $scope.d5;//换成自己的数据
      // console.log($scope.d5);
      // console.log(res);
      return res;
    };

    $scope.d4 = $scope.getRandomData();

    $scope.month = [
        [1,'Jan'],
        [2,'Feb'],
        [3,'Mar'],
        [4,'Apr'],
        [5,'May'],
        [6,'Jun'],
        [7,'Jul'],
        [8,'Aug'],
        [9,'Sept'],
        [10,'Oct'],
        [11,'Nov'],
        [12,'Dec']
    ];

    $scope.datas = {};
    $http.get('/homepage')
        .success(function (data) {
            if(data.resultCode == 'success'){
                $scope.datas = data.object;
                console.log($scope.datas);
            }else if(data.resultCode == 'error') {
                console.log('没有登录信息');
                $state.go('access.signin');
            }
        })
        .error(function () {
            console.log('请求失败');
        })

  }]);