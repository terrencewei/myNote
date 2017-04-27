var globalAppVar = {
	config: {
		angular: {
			loggingDebug: false,
		},
		zIndex: 999999
	}
};
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
/** ---------------------------------------------------------------------------------- */
/** popup utils */
/** ---------------------------------------------------------------------------------- */
var popupUtils = {
	zIndex: globalAppVar.config.zIndex,
	popup: null,
	show: function (pEditormd) {
		var _this = this;
		var classPrefix = pEditormd.classPrefix;
		var editor = pEditormd.editor;

		pEditormd.lockScreen(true);

		$("html,body").css("overflow-x", "hidden");
		pEditormd.mask.css({
			opacity: "0.3",
			backgroundColor: "#000"
		}).show();
		_this.popup.css("z-index", _this.zIndex).show();

		_this.reset(_this.popup);
		$(window).resize(_this.reset(_this.popup));
	},
	update: function (pPopupContent, pEditormd) {
		var _this = this;
		_this.popup.find("." + pEditormd.classPrefix + "dialog-container").text("").append(pPopupContent);
		_this.reset();
	},
	close: function (pEditormd) {
		$("html,body").css("overflow-x", "");
		pEditormd.editor.children("." + pEditormd.classPrefix + "dialog-info").hide();
		pEditormd.mask.hide();
		pEditormd.lockScreen(false);
	},
	reset: function () {
		var _this = this;
		_this.popup.css({
			top: ($(window).height() - _this.popup.height()) / 2 + "px",
			left: ($(window).width() - _this.popup.width()) / 2 + "px"
		});
	},
	init: function (pEditormd) {
		var _this = this;
		var classPrefix = pEditormd.classPrefix;
		var editor = pEditormd.editor;
		var popup = editor.children("." + classPrefix + "dialog-info");

		// create a new popup
		editor.append([
			"<div class=\"" + classPrefix + "dialog " + classPrefix + "dialog-info\" style=\"\">",
			"<div class=\"" + classPrefix + "dialog-container\">Loading...</div>",
			"<a href=\"javascript:;\" class=\"fa fa-close " + classPrefix + "dialog-close\"></a>",
			"</div>"
		].join("\n"));

		popup = editor.children("." + classPrefix + "dialog-info");

		popup
			.find("." + classPrefix + "dialog-close")
			.bind(window.editormd.mouseOrTouch("click", "touchend"), function () {
				// bind dialog close btn event
				_this.close(pEditormd);
			})
			.css("border", (pEditormd.isIE8) ? "1px solid #ddd" : "")
			.css("z-index", _this.zIndex);

		pEditormd.mask.bind(window.editormd.mouseOrTouch("click", "touchend"), function () {
			_this.close(pEditormd);
		});

		_this.popup = popup;
	}
};
/** ---------------------------------------------------------------------------------- */
/** mask utils
 /** ---------------------------------------------------------------------------------- */
var maskUtils = {
	zIndex: globalAppVar.config.zIndex,
	mask: null,
	show: function (isShow) {
		var _this = this;
		if (isShow) {
			_this.mask.show();
		} else {
			_this.mask.hide();
		}
	},
	init: function () {
		var _this = this;
		var mask = $("<div>")
			.addClass("maskContainer")
			.css("width", "100%")
			.css("height", "100%")
			.css("top", "0")
			.css("left", "0")
			.css("position", "fixed")
			.css("background", "#000")
			.css("z-index", "99998")
			.css("opacity", "0.3")
			.css("background-color", "rgb(0, 0, 0)")
			.append('<table width=\"100%\" height=\"100%\"><tr><td align=\"center\"><i class=\"fa fa-spinner fa-spin fa-3x fa-fw\"></i></td></tr></table>');

		// build mask div
		$("body").children().first().before(mask);

		_this.mask = mask;

		_this.show(false);
	}
};
/** ---------------------------------------------------------------------------------- */
/** editorUtils
 /** ---------------------------------------------------------------------------------- */
var editorUtils = {
	config: {
		containerId: "myEditormd",
		libPath: "/lib/editormd-1.5.0/lib/",
		devExamplePath: "/lib/editormd-1.5.0/examples/index.html"
	},
	shared: {
		data: {
			currentObjKey: null
		},
		fn: {
			save: function (successFn, objKey, objData) {
				// to be implements by other module
			},
			get: function (successFn, objKey) {
				// to be implements by other module
			}
		}
	},
	oss: {
		save: function (thisEditormd) {
			maskUtils.show(true);

			if (editorUtils.shared.data.currentObjKey != null) {
				// save to OSS
				editorUtils.shared.fn.save(function (response) {
					maskUtils.show(false);
					msgUtils.success("\" " + response.data.key + " \" 已保存到云端 (" + ((response.data.fileSize) / 1024).toFixed(2) + "KB)");
				}, editorUtils.shared.data.currentObjKey, thisEditormd.getMarkdown());
			} else {
				msgUtils.warn("你还没有选择任何文件");
				maskUtils.show(false);
			}
		},
		getAll: function (thisEditormd) {

			maskUtils.show(true);

			editorUtils.shared.fn.get(function (response) {
				maskUtils.show(false);

				var popupContent = $('<div class="list-group"></div>')
					.append('<a class="list-group-item active">Click a file to edit</a>');
				// each line content
				$.each(response.data.results, function (index, data) {
					popupContent.append(
						$('<a class="list-group-item">' + data.key + '</a>')
							.on("click", function () {
								editorUtils.shared.fn.get(function (response) {
									popupUtils.close(thisEditormd);
									editorUtils.shared.data.currentObjKey = data.key;
									thisEditormd.cm.setValue(response.data);
								}, data.key);
							})
					);
				});
				popupUtils.update(popupContent, thisEditormd);
				popupUtils.show(thisEditormd);
			});
		}
	},
	init: function () {
		msgUtils.init();

		var myEditormd = editormd(editorUtils.config.containerId, {
			width: "90%",
			height: 720,
			path: editorUtils.config.libPath,
			tocStartLevel: 1,
			tocContainer: TOCUtils.tocContainer,
			tocDropdown: false,
			tocTitle: "目录",
			toolbar: true,
			toolbarIcons: function () {
				return ["ossSave", "ossGetAll", "|", "gotoDevExamples"];
			},
			toolbarIconsClass: {
				// FontAawsome class
				gotoDevExamples: "fa-hand-o-right",
				ossSave: "fa-cloud-upload",
				ossGetAll: "fa-cloud-download"
			},
			lang: {
				toolbar: {
					gotoDevExamples: "查看Editor.md开发者示例",
					ossSave: "保存到云端",
					ossGetAll: "从云端获取所有"
				}
			},
			toolbarHandlers: {
				gotoDevExamples: function (cm, icon, cursor, selection) {
					window.open(editorUtils.config.devExamplePath);
				},
				ossSave: function () {
					editorUtils.oss.save(this);
				},
				ossGetAll: function () {
					editorUtils.oss.getAll(this);
				}
			},
			onload: function () {
				var _this = this;

				TOCUtils.reRenderTOC();

				// editormd content change
				_this.cm.on("change", function (_cm, changeObj) {
					timeout = setTimeout(function () {
						clearTimeout(timeout);
						TOCUtils.reRenderTOC();
						timeout = null;
					}, _this.settings.delay);
				});

				_this.addKeyMap({
					"Ctrl-S": function (cm) {
						editorUtils.oss.save(_this);
					},
					"Ctrl-O": function (cm) {
						editorUtils.oss.getAll(_this);
					}
				});
			}
		});

		popupUtils.init(myEditormd);

		maskUtils.init();
	}
};
