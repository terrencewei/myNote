var globalAppVar = {
	config: {
		angular: {
			loggingDebug: true,
		}
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
}
/** ---------------------------------------------------------------------------------- */
/** message utils */
/** ---------------------------------------------------------------------------------- */
var msgUtils = {
	zIndex: 999999,
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
				_this.showMsg(false);
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

		_this.showMsg(false);
	}
}
/** ---------------------------------------------------------------------------------- */
/** popup utils */
/** ---------------------------------------------------------------------------------- */
var popupUtils = {
	zIndex: 999999,
	show: function (pEditormd) {
		var _this = this;
		var classPrefix = pEditormd.classPrefix;
		var editor = pEditormd.editor;
		var popup = editor.children("." + classPrefix + "dialog-info");

		$("html,body").css("overflow-x", "hidden");

		pEditormd.lockScreen(true);


		pEditormd.mask.css({
			opacity: "0.3",
			backgroundColor: "#000"
		}).show();

		if (popup.length < 1) {
			// dialog is not exists, create a new dialog
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
		}

		popup.css("z-index", _this.zIndex).show();

		_this.reset(popup);

		$(window).resize(_this.reset(popup));

		return popup;
	},
	update: function (pPopup, pPopupContent, pEditormd) {
		pPopup.find("." + pEditormd.classPrefix + "dialog-container").text("").append(pPopupContent);
		this.reset(pPopup);
	},
	close: function (pEditormd) {
		$("html,body").css("overflow-x", "");
		pEditormd.editor.children("." + pEditormd.classPrefix + "dialog-info").hide();
		pEditormd.mask.hide();
		pEditormd.lockScreen(false);
	},
	reset: function (pPopup) {
		pPopup.css({
			top: ($(window).height() - pPopup.height()) / 2 + "px",
			left: ($(window).width() - pPopup.width()) / 2 + "px"
		});
	}
}
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
			currentObjKey: null,
		},
		fn: {
			save: null,
			get: null,
		},
	},
	init: function () {
		msgUtils.init();

		editormd(editorUtils.config.containerId, {
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
				/**
				 * @param {Object}      cm         CodeMirror对象
				 * @param {Object}      icon       图标按钮jQuery元素对象
				 * @param {Object}      cursor     CodeMirror的光标对象，可获取光标所在行和位置
				 * @param {String}      selection  编辑器选中的文本
				 */
				gotoDevExamples: function (cm, icon, cursor, selection) {
					window.open(editorUtils.config.devExamplePath);
				},
				ossSave: function (cm, icon, cursor, selection) {
					var _this = this;
					if (editorUtils.shared.data.currentObjKey != null) {
						// save to OSS
						editorUtils.shared.fn.save(function (response) {
							msgUtils.success("\" " + response.data.key + " \" 已保存到云端 (" + ((response.data.fileSize) / 1024).toFixed(2) + "KB)");
						}, editorUtils.shared.data.currentObjKey, _this.getMarkdown());
					} else {
						msgUtils.warn("你还没有选择任何文件");
					}
				},
				ossGetAll: function () {
					var _this = this;

					var popup = popupUtils.show(_this);

					editorUtils.shared.fn.get(function (response) {
						var popupContent = $('<div class="list-group"></div>')
							.append('<a class="list-group-item active">Click a file to edit</a>');
						// each line content
						$.each(response.data.results, function (index, data) {
							popupContent.append(
								$('<a class="list-group-item">' + data.key + '</a>')
									.on("click", function () {
										editorUtils.shared.fn.get(function (response) {
											popupUtils.close(_this);
											editorUtils.shared.data.currentObjKey = data.key;
											_this.cm.setValue(response.data);
										}, data.key);
									})
							);
						});
						popupUtils.update(popup, popupContent, _this);
					});
				}
			},
			onload: function () {
				var _this = this;
				TOCUtils.reRenderTOC();
				_this.cm.on("change", function (_cm, changeObj) {
					timeout = setTimeout(function () {
						clearTimeout(timeout);
						TOCUtils.reRenderTOC();
						timeout = null;
					}, _this.settings.delay);
				});
			}
		});
	}
};
