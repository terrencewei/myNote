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
			put: function (successFn, objKey, objData) {
				// to be implements by other module
			},
			get: function (successFn, objKey) {
				// to be implements by other module
			},
			list: function (successFn) {
				// to be implements by other module
			},
			putLocal: function (successFn, objKey, objData) {
				// to be implements by other module
			},
			getLocal: function (successFn, objKey) {
				// to be implements by other module
			},
			listLocal: function (successFn) {
				// to be implements by other module
			}
		}
	},
	obj: {
		put: function (thisEditormd, objType) {
			maskUtils.show(true);

			if (editorUtils.shared.data.currentObjKey != null) {
				// save to OSS
				var successFn = function (ossOutput) {
					maskUtils.show(false);
					msgUtils.success({
						"oss": "\" " + ossOutput.objects[0].key + " \" 已保存到云端 (" + ((ossOutput.objects[0].size) / 1024).toFixed(2) + "KB)",
						"local": "\" " + ossOutput.objects[0].key + " \" 已保存到本地 (" + ((ossOutput.objects[0].size) / 1024).toFixed(2) + "KB)",
					}[objType]);
				};
				var putInvoker = {
					"oss": editorUtils.shared.fn.put(successFn, editorUtils.shared.data.currentObjKey, thisEditormd.getMarkdown()),
					"local": editorUtils.shared.fn.putLocal(successFn, editorUtils.shared.data.currentObjKey, thisEditormd.getMarkdown()),
				}[objType];
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
						$('<a class="list-group-item">' + object.key + '</a>')
							.on("click", function () {
								// call service to get single file content from OSS
								var getSuccessFn = function (ossOutput) {
									popupUtils.close(thisEditormd);
									editorUtils.shared.data.currentObjKey = object.key;
									thisEditormd.cm.setValue(ossOutput.objects[0].content);
								};
								var getInvoker = {
									"oss": editorUtils.shared.fn.get(getSuccessFn, object.key),
									"local": editorUtils.shared.fn.getLocal(getSuccessFn, object.key),
								}[objType];
							})
					);
				});
				popupUtils.update(popupContent, thisEditormd);
				popupUtils.show(thisEditormd);
			};
			var listInvoker = {
				"oss": editorUtils.shared.fn.list(listSuccessFn),
				"local": editorUtils.shared.fn.listLocal(listSuccessFn),
			}[objType];
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
				return ["ossDownload", "ossUpload", "|", "syncCloud2Local", "syncLocal2Cloud", "|", "openLocal", "saveLocal", "|", "gotoDevExamples"];
			},
			toolbarIconsClass: {
				// FontAawsome class
				ossDownload: "fa-cloud-download",
				ossUpload: "fa-cloud-upload",
				syncCloud2Local: "fa-download",
				syncLocal2Cloud: "fa-upload",
				openLocal: "fa-folder-open-o",
				saveLocal: "fa-floppy-o",
				gotoDevExamples: "fa-hand-o-right",
			},
			lang: {
				toolbar: {
					ossDownload: "打开云端文件",
					ossUpload: "保存到云端",
					syncCloud2Local: "下载云端覆盖本地",
					syncLocal2Cloud: "上传本地覆盖云端",
					openLocal: "打开本地文件",
					saveLocal: "保存到本地",
					gotoDevExamples: "查看Editor.md开发者示例",
				}
			},
			toolbarHandlers: {
				ossDownload: function () {
					editorUtils.obj.list(this, "oss");
				},
				ossUpload: function () {
					editorUtils.obj.put(this, "oss");
				},
				syncCloud2Local: function () {
					alert("TODO");
				},
				syncLocal2Cloud: function () {
					alert("TODO");
				},
				openLocal: function () {
					alert("TODO");
				},
				saveLocal: function () {
					alert("TODO");
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
						editorUtils.obj.list(_this, "oss");
					},
					"Ctrl-S": function (cm) {
						editorUtils.obj.put(_this, "oss");
					},
				});
			}
		});

		popupUtils.init(myEditormd);

		maskUtils.init();
	}
};
