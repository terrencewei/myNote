$(function () {

	var myEditormd = editormd("myEditormd", {
		width: "90%",
		height: 720,
		path: "/lib/editormd-1.5.0/lib/",
		tocStartLevel: 1,
		tocContainer: "#custom-toc-container",
		tocDropdown: false,
		tocTitle: "Table of Contents",
		toolbar: true,
		toolbarIcons: function () {
			return ["upload", "|", "gotoDevExamples"];
		},
		toolbarCustomIcons: {
			// toc: '<div class="markdown-body editormd-preview-container" id="custom-toc-container2"></div>'
		},
		toolbarIconsClass: {
			gotoDevExamples: "fa-hand-o-right",  // FontAawsome class
			upload: "fa-cloud-upload"
		},
		lang: {
			toolbar: {
				gotoDevExamples: "查看Editor.md开发者示例",
				upload: "保存到云端"
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
				window.open("/lib/editormd-1.5.0/examples/index.html");
			},
			upload: function (cm, icon, cursor, selection) {
				//TODO:
			}
		},
		onload: function () {
			function setBootstrapCollapse(levelClass) {
				var levelSize = $("." + levelClass).size();
				$.each($("." + levelClass), function (index, level) {
					var collapseTargetClassName = levelClass + "-collapse-" + (index + 1);
					// <a> link
					$(level)
						.wrap("<div class='toc-menu-title'></div>");
					// <a> link's parent <div>
					if ($(level).parent().siblings("ul").size() > 0) {
						$(level).parent()
							.append('<i class="pull-right fa fa-angle-down" style="padding-top: 4px;"></i>');
					}
					// <a> link's siblings <i>
					$(level).parent().children("i")
						.attr("data-toggle", "collapse")
						.attr("data-target", "." + collapseTargetClassName)
						.attr("data-parent", "#custom-toc-container")
					// <a> link's parent <div>'s siblings <ul>
					$(level).parent().siblings("ul").first()
						.addClass("toc-menu-body")
						.addClass(collapseTargetClassName)
						.addClass("collapse");

					// collapse show event
					$("." + collapseTargetClassName).on('show.bs.collapse', function () {
						for (var i = 1; i <= (levelSize + 1); i++) {
							if (i == (index + 1)) {
								continue;
							}
							$("." + levelClass + "-collapse-" + i).collapse("hide");
						}
					});
				})
			}

			function setJqueryUICollapse(levelClass) {
				$("#custom-toc-container")
					.accordion({
						header: "." + levelClass
					})
					.sortable({
						axis: "y",
						handle: "." + levelClass,
						stop: function (event, ui) {
							// IE doesn't register the blur when sorting
							// so trigger focusout handlers to remove .ui-state-focus
							ui.item.children("." + levelClass).triggerHandler("focusout");

							// Refresh accordion to handle new order
							$(this).accordion("refresh");
						}
					});
			}

			// setJqueryUICollapse("toc-level-1");
			setBootstrapCollapse("toc-level-1");
			setBootstrapCollapse("toc-level-2");
			setBootstrapCollapse("toc-level-3");
		}
	});

});