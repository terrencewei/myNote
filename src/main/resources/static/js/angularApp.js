var myApp = angular.module('myApp', [])
	.config(function ($logProvider) {
		$logProvider.debugEnabled(globalAppVar.config.angular.loggingDebug);
	})
	.controller('editorController', ['$scope', 'httpService',
		function ($scope, httpService) {

			var myTocUtils = {
				tocContainer: "#custom-toc-container",
				tocMenuContainer: ".markdown-toc-list",
				tocMenuTitleClass: "toc-menu-title",
				tocMenuTitleButtonClass: "toc-menu-title-btn",
				tocMenuTitleButtonInactiveClass: "fa-caret-down",
				tocMenuTitleContentClass: "toc-menu-title-content",
				tocMenuBodyClass: "toc-menu-body",
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
						// $(level).parent().children("i")
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

			var myEditorUtils = {
				editorContainerId: "myEditormd",
				editormdLibPath: "/lib/editormd-1.5.0/lib/",
				editormdDevExamplePath: "/lib/editormd-1.5.0/examples/index.html",
			}

			$scope.myEditormd = editormd(myEditorUtils.editorContainerId, {
				width: "90%",
				height: 720,
				path: myEditorUtils.editormdLibPath,
				tocStartLevel: 1,
				tocContainer: myTocUtils.tocContainer,
				tocDropdown: false,
				tocTitle: "Table of Contents",
				toolbar: true,
				toolbarIcons: function () {
					return ["ossSave", "|", "gotoDevExamples"];
				},
				toolbarIconsClass: {
					gotoDevExamples: "fa-hand-o-right",  // FontAawsome class
					ossSave: "fa-cloud-upload"
				},
				lang: {
					toolbar: {
						gotoDevExamples: "查看Editor.md开发者示例",
						ossSave: "保存到云端"
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
						window.open(myEditorUtils.editormdDevExamplePath);
					},
					ossSave: function (cm, icon, cursor, selection) {
						//TODO: file key should comes from OSS server
						httpService.post("/oss/save", {"key": "test.md", "data": $scope.myEditormd.getMarkdown()},
							function (response) {
								console.log("oss save success!");
							},
							function (response) {
								console.log("oss save error!");
							},
							function (response) {
								console.log("oss save exception occurs!");
							});
					}
				},
				onload: function () {

					$(myTocUtils.tocContainer).before('<h1>目录</h1>');

					for (var i = 1; i <= 10; i++) {
						myTocUtils.toBootstrapCollapse("toc-level-" + i);
					}
				}
			});
		}
	])
	.service('httpService', ['$http', '$log', function ($http, $log) {
		this.post = function (apiurl, apidata,
		                      successcallback,
		                      errcallback,
		                      expcallback) {
			$log.debug("send post request apiurl:" + apiurl + ", apidata:" + JSON.stringify(apidata));
			$http.post(apiurl, apidata).then(
				function (response) {
					$log.debug("receive post success response:" + JSON.stringify(response));
					successcallback(response);
				},
				function (response) {
					$log.debug("receive post error response:" + JSON.stringify(response));
					errcallback(response);
				}
			).catch(
				function (response) {
					$log.debug("receive post exception response:" + JSON.stringify(response));
					expcallback(response);
				}
			);
		};
	}]);