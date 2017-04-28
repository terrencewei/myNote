/** ---------------------------------------------------------------------------------- */
/** message utils */
/** ---------------------------------------------------------------------------------- */
var msgUtils = {
	zIndex: globalAppVar.config.zIndex,
	success: function (msg) {
		this.show(true, msg, "success");
	},
	info: function (msg) {
		this.show(true, msg, "info");
	},
	warn: function (msg) {
		this.show(true, msg, "warning");
	},
	error: function (msg) {
		this.show(true, msg, "danger");
	},
	show: function (isShow, msg, type) {
		this.clear();
		if (isShow) {
			$(".msgTips")
				.addClass("alert")
				.addClass("alert-" + type)
				.fadeIn()
				.children(".msg").text(msg);
			setTimeout(function () {
				$(".msgTips").fadeOut();
			}, 2000);
		} else {
			$(".msgTips").hide();
		}
	},
	clear: function () {
		$(".msgTips")
			.removeClass("alert")
			.removeClass("alert-success")
			.removeClass("alert-info")
			.removeClass("alert-warning")
			.removeClass("alert-danger")
			.children(".msg").text("");
	},
	init: function () {
		var _this = this;
		var msgTips = $("<div>")
			.addClass("msgTips")
			.css("position", "relative")
			.css("z-index", _this.zIndex);

		var msgContent = $("<span>").addClass("msg");

		var msgCloseBtn = $("<button>")
			.addClass("close")
			.addClass("pull-left")
			.attr("type", "button")
			.on("click", function () {
				_this.show(false);
			});

		// do not use jQuery style so it can be easily copied from http://fontawesome.io/icon
		var msgCloseBtnIcon = $('<i class="fa fa-times" aria-hidden="true"></i>');

		// build msg div
		$("body").children().first().before(
			msgTips
				.append(msgContent)
				.append(
					msgCloseBtn
						.append(msgCloseBtnIcon)
				)
		);

		_this.show(false);
	}
};