'use strict';


angular.module('ui.load',[])
.service('uiLoad',['$document', '$q', '$timeout', function ($document, $q, $timeout) {
    // $q是angular的一种内置服务，它可以使你异步地执行函数，并且当函数执行完成时它使用函数的返回值（或异常）
    var loaded = [];
    var promise = false;
    var deferred = $q.defer();//注册一个延迟对象实例

    this.load = function (srcs) {
        srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
        var  self = this;
        if(!promise){
            promise = deferred.promise;//promise返回当前任务的完成结果
        }
        angular.forEach(srcs, function (src) {
            promise = promise.then(function () {
                return src.indexOf('.css') >=0 ? self.loadCSS(src) : self.loadScript(src);
            });
        });
        deferred.resolve();
        return promise;
    };

    /**
     * Dynamically loads the given script
     */
    this.loadScript = function (src) {
        if(loaded[src]) return loaded[src].promise;

        var deferred = $q.defer();
        var script = $document[0].createElement('script');//$document[0]和原生JS的document等效
        script.src = src;
        script.onload = function (e) {
            $timeout(function () {
                deferred.resolve(e);
            });
        };
        script.onerror = function (e) {
            $timeout(function () {
                deferred.reject(e);
            });
        };
        $document[0].body.appendChild(script);
        loaded[src] = deferred;
        return deferred.promise;
    };

    /**
     * Dynamically loads the given css
     */
    this.loadCSS = function (href) {
        if(loaded[href]) return loaded[href].promise;

        var deferred = $q.defer();
        var link = $document[0].createElement('link');
        link.rel = 'stylesheet';
        link.style = 'text/css';
        link.href = href;
        link.onload = function (e) {
            $timeout(function () {
                deferred.resolve(e);
            });
        };
        link.onerror = function (e) {
            $timeout(function () {
                deferred.reject(e);
            });
        };
        $document[0].body.appendChild(link);
        loaded[href] = deferred;
        return deferred.promise;
    }
}]);