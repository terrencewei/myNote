/** ---------------------------------------------------------------------------------- */
/** editorUtils
 /** ---------------------------------------------------------------------------------- */
// enum
if (typeof ObjType == "undefined") {
	var ObjType = {};
	ObjType.OSS = 1;
	ObjType.Local = 2;
}
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
			putCloud: function (successFn, objKey, objData) {
				// put object to cloud
			},
			getCloud: function (successFn, objKey) {
				// get object from cloud
			},
			listCloud: function (successFn) {
				// get object list from cloud
			},
			removeCloud: function (successFn, objKey) {
				// delete object at Cloud
			},
			putLocal: function (successFn, objKey, objData) {
				// put object to local
			},
			getLocal: function (successFn, objKey) {
				// get object from local
			},
			listLocal: function (successFn) {
				// get object list from local
			},
			removeLocal: function (successFn, objKey) {
				// delete object at local
			},
		}
	},
	oss: {
		put: function (thisEditormd, objType) {
			maskUtils.show(true);

			if (editorUtils.shared.data.currentObjKey != null) {
				// save to OSS
				var successFn = function (ossOutput) {
					maskUtils.show(false);
					msgUtils.success({
						"oss": "\" " + ossOutput.objects[0].objKey + " \" 已保存到云端 (" + ((ossOutput.objects[0].size) / 1024).toFixed(2) + "KB)",
						"local": "\" " + ossOutput.objects[0].objKey + " \" 已保存到本地 (" + ((ossOutput.objects[0].size) / 1024).toFixed(2) + "KB)",
					}[objType]);
				};
				editorUtils.switchInvoker(objType, {
					"oss": function () {
						editorUtils.shared.fn.putCloud(successFn, editorUtils.shared.data.currentObjKey, thisEditormd.getMarkdown());
					},
					"local": function () {
						editorUtils.shared.fn.putLocal(successFn, editorUtils.shared.data.currentObjKey, thisEditormd.getMarkdown());
					},
				});
			} else {
				msgUtils.warn("你还没有选择任何文件");
				maskUtils.show(false);
			}
		},
		list: function (thisEditormd, objType) {
			maskUtils.show(true);
			// call service to get all files from OSS
			var listSuccessFn = function (ossOutput) {
				maskUtils.show(false);
				var popupContent = $('<div class="list-group"></div>')
					.append('<a class="list-group-item active">Click a file to edit</a>');
				// each line content
				$.each(ossOutput.objects, function (index, object) {
					popupContent.append(
						$('<a class="list-group-item">' + object.objKey + '</a>')
							.on("click", function () {
								// call service to get single file content from OSS
								var getSuccessFn = function (ossOutput) {
									popupUtils.close(thisEditormd);
									editorUtils.shared.data.currentObjKey = object.objKey;
									thisEditormd.cm.setValue(ossOutput.objects[0].content);
								};
								editorUtils.switchInvoker(objType, {
									"oss": function () {
										editorUtils.shared.fn.getCloud(getSuccessFn, object.objKey);
									},
									"local": function () {
										editorUtils.shared.fn.getLocal(getSuccessFn, object.objKey);
									},
								});
							})
					);
				});
				popupUtils.update(popupContent, thisEditormd);
				popupUtils.show(thisEditormd);
			};
			var listErrorFn = function (ossOutput) {
				maskUtils.show(false);
				var popupContent = $('<div class="list-group"></div>')
					.append('<a class="list-group-item">No files exists.</a>');
				popupUtils.update(popupContent, thisEditormd);
				popupUtils.show(thisEditormd);
			};
			editorUtils.switchInvoker(objType, {
				"oss": function () {
					editorUtils.shared.fn.listCloud(listSuccessFn, listErrorFn);
				},
				"local": function () {
					editorUtils.shared.fn.listLocal(listSuccessFn, listErrorFn);
				},
			});
		},
		remove: function (thisEditormd, objType) {
			maskUtils.show(true);

			if (editorUtils.shared.data.currentObjKey != null) {
				// delete object from OSS
				var successFn = function (ossOutput) {
					maskUtils.show(false);
					msgUtils.success({
						"oss": "\" " + ossOutput.objects[0].objKey + " \" 已从云端删除 (" + ((ossOutput.objects[0].size) / 1024).toFixed(2) + "KB)",
						"local": "\" " + ossOutput.objects[0].objKey + " \" 已从本地删除 (" + ((ossOutput.objects[0].size) / 1024).toFixed(2) + "KB)",
					}[objType]);
				};
				editorUtils.switchInvoker(objType, {
					"oss": function () {
						editorUtils.shared.fn.removeCloud(successFn, editorUtils.shared.data.currentObjKey)
					},
					"local": function () {
						editorUtils.shared.fn.removeLocal(successFn, editorUtils.shared.data.currentObjKey)
					},
				});
			} else {
				msgUtils.warn("你还没有选择任何文件");
				maskUtils.show(false);
			}
		}
	},
	switchInvoker: function (caseKey, switchFn) {
		if (switchFn[caseKey]) {
			switchFn[caseKey]();
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
				return ["listCloud", "putCloud", "removeCloud", "|", "syncCloud2Local", "syncLocal2Cloud", "|", "listLocal", "putLocal", "removeLocal", "|", "gotoDevExamples"];
			},
			toolbarIconsClass: {
				// FontAawsome class
				listCloud: "fa-cloud-download",
				putCloud: "fa-cloud-upload",
				removeCloud: "fa-trash",
				syncCloud2Local: "fa-download",
				syncLocal2Cloud: "fa-upload",
				listLocal: "fa-folder-open-o",
				putLocal: "fa-floppy-o",
				removeLocal: "fa-trash-o",
				gotoDevExamples: "fa-hand-o-right",
			},
			lang: {
				toolbar: {
					listCloud: "打开云端文件",
					putCloud: "保存到云端",
					removeCloud: "删除云端文件",
					syncCloud2Local: "下载云端覆盖本地",
					syncLocal2Cloud: "上传本地覆盖云端",
					listLocal: "打开本地文件",
					putLocal: "保存到本地",
					removeLocal: "删除本地文件",
					gotoDevExamples: "查看Editor.md开发者示例",
				}
			},
			toolbarHandlers: {
				listCloud: function () {
					editorUtils.oss.list(this, "oss");
				},
				putCloud: function () {
					editorUtils.oss.put(this, "oss");
				},
				removeCloud: function () {
					editorUtils.oss.remove(this, "oss");
				},
				syncCloud2Local: function () {
					alert("TODO");
				},
				syncLocal2Cloud: function () {
					alert("TODO");
				},
				listLocal: function () {
					editorUtils.oss.list(this, "local");
				},
				putLocal: function () {
					editorUtils.oss.put(this, "local");
				},
				removeLocal: function () {
					editorUtils.oss.remove(this, "local");
				},
				gotoDevExamples: function (cm, icon, cursor, selection) {
					window.open(editorUtils.config.devExamplePath);
				},
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
					"Ctrl-O": function (cm) {
						editorUtils.oss.list(_this, "local");
					},
					"Ctrl-S": function (cm) {
						editorUtils.oss.put(_this, "local");
					},
				});
			}
		});

		popupUtils.init(myEditormd);

		maskUtils.init();
	}
};
