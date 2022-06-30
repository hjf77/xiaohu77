!function (a) {
    "use strict";
    //  a = jquery
    function b(b, d) {
        this.$el = a(b)
        this.opt = a.extend(!0, {}, c, d)
        this.init(this)
    }

    var c = {};
    b.prototype = {
        preTarget : null,
        init: function (b) {
            // $('li:has(a)').on("click", function(c) {
            //     var d = a(c.target);
            //     d = d.closest(b.$el.data("menu-toggle"));
            //     b.$el.css(b.calcPosition(d)).addClass("show");
            //     c.preventDefault();
            // })

            a('.columnButton').on("click",function(){
                b.$el.removeClass("show")
                b.preTarget = null;
            });
            a(document).on("click", function (c) {
                var d = a(c.target);
                var isClose = false;
                //同一个对象点击2次关闭
                if(d.hasClass("material-icons")){
                    if(d.parent().attr('id') == b.preTarget){
                        d.closest(b.$el)[0] || b.$el.removeClass("show")
                        b.preTarget = null;
                        isClose = true;
                    }else{
                        b.preTarget = d.parent().attr('id');
                    }

                }else if(d.hasClass("toggle")){
                    if(d.attr('id') == b.preTarget){
                        d.closest(b.$el)[0] || b.$el.removeClass("show")
                        b.preTarget = null;
                        isClose = true;
                    }else{
                        b.preTarget = d.attr('id');
                    }
                }
                if(!isClose){
                    if (d.closest(b.$el.data("menu-toggle"))[0]) {
                        d = d.closest(b.$el.data("menu-toggle"));
                        b.$el.css(b.calcPosition(d)).addClass("show");
                        c.preventDefault();
                    } else {
                        d.closest(b.$el)[0] || b.$el.removeClass("show")
                        b.preTarget = null;
                    }
                }
            })
        },
        calcPosition: function (b) {
            var c, d, e;
            return c = a(window),
                d = b.offset(),
                e = {top: d.top + b.outerHeight() / 2},
                d.left > c.width() / 2 ? (this.$el.addClass("menu-right").removeClass("menu-left"), e.right = c.width() - d.left - b.outerWidth() / 2, e.left = "auto") : (this.$el.addClass("menu-left").removeClass("menu-right"), e.left = d.left + b.outerWidth() / 2, e.right = "auto"), e &&
            d.top > c.height() / 2 ? (this.$el.addClass("menu-bottom").removeClass("menu-top"), e.bottom = c.height() - d.top - b.outerHeight() / 2, e.top = "auto") : (this.$el.addClass("menu-top").removeClass("menu-bottom"), e.top = d.top + b.outerHeight() / 2, e.bottom = "auto"), e
        }

    },
        a.fn.popMenu  = function (c) {
            return this.each(function () {
                a.data(this, "popMenu") || a.data(this, "popMenu", new b(this, c))
            })
        }
}(window.jQuery);