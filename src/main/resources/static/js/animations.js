'use strict';

app.animation('.room',function roomAnimationFactory() {
    return {
        addClass: animateIn,
        removeClass: animateOut
    };

    
    function animateIn(element, className, done) {
        // console.log(className);
        // if (className !== 'selected') return;
        if(className === 'selected'){//向左滑动
            element.css({
                display: 'block',
                // position: 'absolute',
                // top: 50,
                // left: 500
                opacity: 0
            }).animate({
                // left: 50
                opacity: 1
            }, done);
        }else{//向右滑动
            element.css({
                display: 'block',
                // position: 'absolute',
                // top: 50,
                // left: -500
                opacity: 0
            }).animate({
                // left: 50
                opacity: 1
            }, done);
        }



        return function animateInEnd(wasCanceled) {
            if (wasCanceled) element.stop();
        };
    }

    function animateOut(element, className, done) {
        // if (className !== 'selected') return;
        // console.log(className);
        if(className === 'selected'){
            element.css({
                // position: 'absolute',
                // top: 50,
                // left: 50
                opacity: 1
            }).animate({
                // left: -500
                opacity: 0
            }, done);
        }else{
            element.css({
                // position: 'absolute',
                // top: 50,
                // left: 50
                opacity: 1
            }).animate({
                // left: 500
                opacity: 0
            }, done);
        }




        return function animateOutEnd(wasCanceled) {
            if (wasCanceled) element.stop();
        };
    }


});   