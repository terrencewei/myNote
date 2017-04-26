var myApp = angular.module('myApp', [])
	.config(function ($logProvider) {
		$logProvider.debugEnabled(globalAppVar.config.angular.loggingDebug);
	})
	.controller('editorController', ['$scope', 'httpService', 'ossService', '$log',
		function ($scope, httpService, ossService, $log) {

			// $scope obj definition
			$scope.msg = {};
			$scope.myEditormd = {};

			// init logic
			$(".msgTips").hide();

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
				showOSSGetAllDialog: function (thisEditormd) {
					var classPrefix = thisEditormd.classPrefix;
					var editor = thisEditormd.editor;
					var dialog = editor.children("." + classPrefix + "dialog-info");
					var dialogMask = thisEditormd.mask;
					$("html,body").css("overflow-x", "hidden");
					thisEditormd.lockScreen(true);
					dialogMask.css({
						opacity: "0.3",
						backgroundColor: "#000"
					}).show();
					if (dialog.length < 1) {
						editor.append([
							"<div class=\"" + classPrefix + "dialog " + classPrefix + "dialog-info\" style=\"\">",
							"<div class=\"" + classPrefix + "dialog-container\">Loading...</div>",
							"<a href=\"javascript:;\" class=\"fa fa-close " + classPrefix + "dialog-close\"></a>",
							"</div>"
						].join("\n"));

						dialog = editor.children("." + classPrefix + "dialog-info");
						dialog
							.find("." + classPrefix + "dialog-close")
							.bind(editormd.mouseOrTouch("click", "touchend"), function () {
								$("html,body").css("overflow-x", "");
								dialog.hide();
								dialogMask.hide();
								thisEditormd.lockScreen(false);
							})
							.css("border", (editormd.isIE8) ? "1px solid #ddd" : "")
							.css("z-index", editormd.dialogZindex);
					}
					dialog.css("z-index", editormd.dialogZindex).show();
					myEditorUtils.resetOSSGetAllDialogPosition(dialog);
					$(window).resize(myEditorUtils.resetOSSGetAllDialogPosition(dialog));
					return dialog;
				},
				updateOSSGetAllDialog: function (dialogContent, dialog, thisEditormd) {
					dialog.find("." + thisEditormd.classPrefix + "dialog-container").text("").append(dialogContent);
					myEditorUtils.resetOSSGetAllDialogPosition(dialog);
				},
				resetOSSGetAllDialogPosition: function (dialog) {
					dialog.css({
						top: ($(window).height() - dialog.height()) / 2 + "px",
						left: ($(window).width() - dialog.width()) / 2 + "px"
					});
				}
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
						window.open(myEditorUtils.editormdDevExamplePath);
					},
					ossSave: function (cm, icon, cursor, selection) {
						//TODO: file key should comes from OSS server
						ossService.save($scope, "test.md", $scope.myEditormd.getMarkdown());
					},
					ossGetAll: function () {
						var thisEditormd = this;
						var dialog = myEditorUtils.showOSSGetAllDialog(thisEditormd);
						// get all from oss
						httpService.post("/oss/get", {},
							function (response) {
								if (response.data.success) {
									var $dialogContent = $("<div>").addClass("list-group");
									var $dialogTitle = $("<a>").addClass("list-group-item").addClass("active").text("Click a file to edit");
									$dialogContent.append($dialogTitle);
									$.each(response.data.responseData.results, function (index, result) {
										$dialogTitle = $("<a>").addClass("list-group-item").text(result.key);
										$dialogTitle.on("click", function () {
											myEditorUtils.ossGet(result.key);
										});
										$dialogContent.append($dialogTitle);
									});
									myEditorUtils.updateOSSGetAllDialog($dialogContent, dialog, thisEditormd);
								}
							},
							function (response) {
								$log.debug("oss save error!");
								$log.debug(response);
							},
							function (response) {
								$log.debug("oss save exception occurs!");
								$log.debug(response);
							});
					},
					ossGet: function (objKey) {
						httpService.post("/oss/get/" + $(this).text(), {},
							function (response) {
								if (response.data.success) {
									var result = response.data.responseData.result;
									$scope.myEditormd.cm.setValue("TODO");
								}
							},
							function (response) {
								$log.debug("oss save error!");
								$log.debug(response);
							},
							function (response) {
								$log.debug("oss save exception occurs!");
								$log.debug(response);
							});
					}
				},
				onload: function () {

					$(myTocUtils.tocContainer).before('<h1>Table of Contents</h1>');

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
	}])
	.service('ossService', ['httpService', '$log', '$timeout', function (httpService, $log, $timeout) {
		this.save = function ($scope, objKey, objData) {
			httpService.post("/oss/save", {"objKey": objKey, "objData": objData},
				function (response) {
					$log.debug("oss save success!");
					$log.debug(response);
					if (response.data.success) {
						var data = response.data.responseData;
						$scope.msg.fileSize = ((data.fileSize)/1024).toFixed(2) + "KB";
						$scope.msg.fileKey = data.key;
						$(".msgTips").addClass("alert").addClass("alert-success").fadeIn();
						$timeout(function () {
							$(".msgTips").fadeOut();
						}, 2000);
					}
				},
				function (response) {
					$log.debug("oss save error!");
					$log.debug(response);
				},
				function (response) {
					$log.debug("oss save exception occurs!");
					$log.debug(response);
				});
		};
	}])