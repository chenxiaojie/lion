(function ($) {
    $.extend({
        getParameter: function (key) {
            var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
            var result = window.location.search.substr(1).match(reg);
            if (result)
                return unescape(result[2]);
            return null;
        },
        warning: function (tipText, fadeTime) {
            if (!tipText) {
                return;
            }
            var $this = $('#warning');
            if ($this.length == 0) {
                $this = $('<div />').attr('id', 'warning').css({
                    'display': 'none',
                    'position': 'absolute',
                    'width': '320px',
                    'color': '#745218',
                    'background-color': '#FCED9F',
                    'border': '1px solid #F0D397',
                    'box-shadow': '5px 5px 10px #888888',
                    'padding': '15px 15px 15px 35px',
                    'border-radius': '4px',
                    'z-index': '9999'
                }).append(
                    $('<button />').attr({
                        'type': 'button',
                        'class': 'close'
                    }).append(
                        $('<span />').attr('aria-hidden', 'true').text('×').click(function () {
                            $('#warning').hide();
                        })
                    )
                ).append($('<div />').addClass('warningTip').text(tipText)).appendTo('body');
                $(window).unbind('resize').bind('resize', function () {
                    $('#warning').center({
                        top: -150
                    });
                })
            } else {
                $this.find('.warningTip').text(tipText);
            }
            clearTimeout(this.timeout);
            $this.center({
                top: -150
            }).show('slow');
            if (fadeTime) {
                this.timeout = setTimeout(function () {
                    $('#warning').hide('slow');
                }, fadeTime);
            }
            return $this;
        }
    });

    $.fn.extend({
        center: function (options) {
            var $this = $(this);
            if ($this.outerHeight() == 0 || $this.outerWidth() == 0) {
                return $this;
            }
            options = $.extend({
                top: 0,
                left: 0
            }, options);
            var $window = $(window);
            options.top += (($window.height() - $this.outerHeight()) / 2 + $window.scrollTop());
            options.left += (($window.width() - $this.outerWidth()) / 2 + $window.scrollLeft());
            return $this.css({
                'top': options.top,
                'left': options.left
            });
        },
        clearFormData: function () {
            var $this = $(this);
            var reset = $this.find('input:reset');
            if (reset.length == 0) {
                reset = $('<input />').attr('type', 'reset').css('display', 'none');
                $this.append(reset);
            }
            reset.trigger('click');
            return $this;
        }
    });
})(jQuery);