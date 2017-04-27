var myApp = angular.module('myApp', [])
	.config(function ($logProvider) {
		$logProvider.debugEnabled(globalAppVar.config.angular.loggingDebug);
	})
	.controller('editorController', ['$scope', 'httpService', 'ossService', '$log', '$timeout',
		function ($scope, httpService, ossService, $log, $timeout) {

			// js definition
			msgUtils.init();

			// $scope definition
			$scope.oss = {
				key: null
			};

			$scope.editorUtils = {
				containerId: "myEditormd",
				libPath: "/lib/editormd-1.5.0/lib/",
				devExamplePath: "/lib/editormd-1.5.0/examples/index.html",
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
								$scope.editorUtils.closeOSSGetAllDialog(thisEditormd);
							})
							.css("border", (editormd.isIE8) ? "1px solid #ddd" : "")
							.css("z-index", editormd.dialogZindex);
					}
					dialog.css("z-index", editormd.dialogZindex).show();
					$scope.editorUtils.resetOSSGetAllDialogPosition(dialog);
					$(window).resize($scope.editorUtils.resetOSSGetAllDialogPosition(dialog));
					return dialog;
				},
				updateOSSGetAllDialog: function (dialogContent, dialog, thisEditormd) {
					dialog.find("." + thisEditormd.classPrefix + "dialog-container").text("").append(dialogContent);
					$scope.editorUtils.resetOSSGetAllDialogPosition(dialog);
				},
				closeOSSGetAllDialog: function (thisEditormd) {
					$("html,body").css("overflow-x", "");
					thisEditormd.editor.children("." + thisEditormd.classPrefix + "dialog-info").hide();
					thisEditormd.mask.hide();
					thisEditormd.lockScreen(false);
				},
				resetOSSGetAllDialogPosition: function (dialog) {
					dialog.css({
						top: ($(window).height() - dialog.height()) / 2 + "px",
						left: ($(window).width() - dialog.width()) / 2 + "px"
					});
				}
			}

			$scope.editormd = editormd($scope.editorUtils.containerId, {
				width: "90%",
				height: 720,
				path: $scope.editorUtils.libPath,
				tocStartLevel: 1,
				tocContainer: myTocUtils.tocContainer,
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
						window.open($scope.editorUtils.devExamplePath);
					},
					ossSave: function (cm, icon, cursor, selection) {
						if ($scope.oss.key != null) {
							// save to OSS
							ossService.save(function (response) {
								msgUtils.success("\" " + response.data.key + " \" 已保存到云端 (" + ((response.data.fileSize) / 1024).toFixed(2) + "KB)");
							}, $scope.oss.key, $scope.editormd.getMarkdown());
						} else {
							msgUtils.warn("你还没有选择任何文件");
						}
					},
					ossGetAll: function () {
						var editormd = this;
						var dialog = $scope.editorUtils.showOSSGetAllDialog(editormd)
						ossService.get(function (response) {
							var _$dialogContent = $("<div>").addClass("list-group");
							var _$dialogTitle = $("<a>").addClass("list-group-item").addClass("active").text("Click a file to edit");
							_$dialogContent.append(_$dialogTitle);
							$.each(response.data.results, function (index, data) {
								_$dialogTitle = $("<a>").addClass("list-group-item").text(data.key);
								_$dialogTitle.on("click", function () {
									ossService.get(function (response) {
										$scope.oss.key = data.key;
										$scope.editormd.cm.setValue(response.data);
										$scope.editorUtils.closeOSSGetAllDialog($scope.editormd);
									}, data.key);
								});
								_$dialogContent.append(_$dialogTitle);
							});
							$scope.editorUtils.updateOSSGetAllDialog(_$dialogContent, dialog, editormd);
						});
					}
				},
				onload: function () {
					myTocUtils.reRenderTOC();
					$scope.editormd.cm.on("change", function (_cm, changeObj) {
						var timeout = setTimeout(function () {
							clearTimeout(timeout);
							myTocUtils.reRenderTOC();
							timeout = null;
						}, $scope.editormd.settings.delay);
					});
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
		this.save = function (successFn, objKey, objData) {
			httpService.post("/oss/save", {"objKey": objKey, "objData": objData},
				function (response) {
					$log.debug("oss save success!");
					$log.debug(response);
					if (response.data.success) {
						successFn(response.data);
					}
				});
		};
		this.get = function (successFn, objKey) {
			httpService.post("/oss/get", {objKey: objKey},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					}
				});
		}
	}]);