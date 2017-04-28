/** ---------------------------------------------------------------------------------- */
/** TOC utils */
/** ---------------------------------------------------------------------------------- */
var TOCUtils = {
	tocContainer: "#custom-toc-container",
	tocMenuContainer: ".markdown-toc-list",
	tocMenuTitleClass: "toc-menu-title",
	tocMenuTitleButtonClass: "toc-menu-title-btn",
	tocMenuTitleButtonInactiveClass: "fa-caret-down",
	tocMenuTitleContentClass: "toc-menu-title-content",
	tocMenuBodyClass: "toc-menu-body",
	reRenderTOC: function () {
		for (var i = 1; i <= 10; i++) {
			TOCUtils.toBootstrapCollapse("toc-level-" + i);
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
				.addClass(TOCUtils.tocMenuTitleContentClass)
				.wrap("<div class=\"" + TOCUtils.tocMenuTitleClass + " " + TOCUtils.tocMenuTitleClass + "-" + levelClass + "\"></div>");
			// <a> link's parent <div>
			if ($(level).parent().siblings("ul").size() > 0) {
				$(level).before("<i class=\"" + TOCUtils.tocMenuTitleButtonClass + " pull-left fa fa-lg " + TOCUtils.tocMenuTitleButtonInactiveClass + " \"></i>");
			} else {
				$(level).before("<i class='pull-left fa fa-lg fa-caret-right' style='padding: 5px 0 0 5px'></i>");
			}
			// <a> link's siblings <i>
			$(level).parent()
				.attr("data-toggle", "collapse")
				.attr("data-target", "." + collapseTargetClassName)
				.attr("data-parent", TOCUtils.tocContainer);

			// <a> link's parent <div>'s siblings <ul>
			$(level).parent().siblings("ul").first()
				.addClass(TOCUtils.tocMenuBodyClass)
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
				$(this).siblings("." + TOCUtils.tocMenuTitleClass).children("." + TOCUtils.tocMenuTitleButtonClass)
					.css("transform", "rotate(180deg)")
					.css("padding", "0 5px 5px 0");

			});

			// collapse menu hide event
			$("." + collapseTargetClassName).on('hide.bs.collapse', function () {
				// change parent menu's icon
				$(this).siblings("." + TOCUtils.tocMenuTitleClass).children("." + TOCUtils.tocMenuTitleButtonClass)
					.css("transform", "")
					.css("padding", "5px 0 0 5px");
			});
		})
	},
	// this jquery ui method is useless for now
	toJqueryuiCollapse: function (levelClass) {
		$(TOCUtils.tocMenuContainer)
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
};