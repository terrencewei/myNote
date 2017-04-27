var globalAppVar = {};
globalAppVar.user = {};
globalAppVar.pageUrl = {};
globalAppVar.apiUrl = {};
globalAppVar.constant = {};
globalAppVar.config = {
	angular: {
		loggingDebug: true,
	}
};

var myTocUtils = {
	tocContainer: "#custom-toc-container",
	tocMenuContainer: ".markdown-toc-list",
	tocMenuTitleClass: "toc-menu-title",
	tocMenuTitleButtonClass: "toc-menu-title-btn",
	tocMenuTitleButtonInactiveClass: "fa-caret-down",
	tocMenuTitleContentClass: "toc-menu-title-content",
	tocMenuBodyClass: "toc-menu-body",
	reRenderTOC: function () {
		for (var i = 1; i <= 10; i++) {
			myTocUtils.toBootstrapCollapse("toc-level-" + i);
		}
	},
	toBootstrapCollapse: function (levelClass) {
		var $levelClass = $("." + levelClass);
		var levelSize = $levelClass.size();
		if (levelSize <= 0) {
			return;
		}
		$.each($levelClass, function (index, level) {
			var indexNum = (index + 1);
			var collapseTargetClassName = levelClass + "-collapse-" + indexNum;
			// <a> link
			$(level)
				.addClass(myTocUtils.tocMenuTitleContentClass)
				.wrap("<div class=\"" + myTocUtils.tocMenuTitleClass + " " + myTocUtils.tocMenuTitleClass + "-" + levelClass + "\"></div>");
			// <a> link's parent <div>
			if ($(level).parent().siblings("ul").size() > 0) {
				$(level).before("<i class=\"" + myTocUtils.tocMenuTitleButtonClass + " pull-left fa fa-lg " + myTocUtils.tocMenuTitleButtonInactiveClass + " \"></i>");
			} else {
				$(level).before("<i class='pull-left fa fa-lg fa-caret-right' style='padding: 5px 0 0 5px'></i>");
			}
			// <a> link's siblings <i>
			$(level).parent()
				.attr("data-toggle", "collapse")
				.attr("data-target", "." + collapseTargetClassName)
				.attr("data-parent", myTocUtils.tocContainer);

			// <a> link's parent <div>'s siblings <ul>
			$(level).parent().siblings("ul").first()
				.addClass(myTocUtils.tocMenuBodyClass)
				.addClass(collapseTargetClassName)
				.addClass("collapse");

			// collapse menu show event
			$("." + collapseTargetClassName).on('show.bs.collapse', function () {
				// close any other collapse menu
				for (var i = 1; i <= (levelSize + 1); i++) {
					if (i == indexNum) {
						continue;
					}
					$("." + levelClass + "-collapse-" + i).collapse("hide");
				}
				// change parent menu's icon
				$(this).siblings("." + myTocUtils.tocMenuTitleClass).children("." + myTocUtils.tocMenuTitleButtonClass)
					.css("transform", "rotate(180deg)")
					.css("padding", "0 5px 5px 0");

			});

			// collapse menu hide event
			$("." + collapseTargetClassName).on('hide.bs.collapse', function () {
				// change parent menu's icon
				$(this).siblings("." + myTocUtils.tocMenuTitleClass).children("." + myTocUtils.tocMenuTitleButtonClass)
					.css("transform", "")
					.css("padding", "5px 0 0 5px");
			});
		})
	},
	// this jquery ui method is useless for now
	toJqueryuiCollapse: function (levelClass) {
		$(myTocUtils.tocMenuContainer)
			.accordion({
				header: "> li > a",
				collapsible: true,
				heightStyle: "content",
				icons: {
					"header": "ui-icon-triangle-1-e",
					"activeHeader": "ui-icon-triangle-1-s"
				}
			})
	}
}

var msgUtils = {
	success: function (msg) {
		this.showMsg(true, msg, "success");
	},
	info: function (msg) {
		this.showMsg(true, msg, "info");
	},
	warn: function (msg) {
		this.showMsg(true, msg, "warning");
	},
	error: function (msg) {
		this.showMsg(true, msg, "danger");
	},
	showMsg: function (isShow, msg, type) {
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
		var msgTips = $("<div>")
			.addClass("msgTips")
			.css("position", "relative")
			.css("z-index", 999);
		var msgContent = $("<span>").addClass("msg");
		var msgCloseBtn = $("<button>")
			.addClass("close")
			.addClass("pull-left")
			.attr("type", "button")
			.on("click", function () {
				this.showMsg(false);
			}.bind(this));
		var msgCloseBtnIcon = $("<i>")
			.addClass("fa")
			.addClass("fa-times")
			.attr("aria-hidden", true);

		$("body").children().first().before(
			msgTips
				.append(msgContent)
				.append(
					msgCloseBtn
						.append(msgCloseBtnIcon)
				)
		);

		this.showMsg(false);
	}
}