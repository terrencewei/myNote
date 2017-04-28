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
			put: function (successFn, objKey, objData) {
				// to be implements by other module
			},
			get: function (successFn, objKey) {
				// to be implements by other module
			},
			list: function (successFn) {
				// to be implements by other module
			}
		}
	},
	oss: {
		save: function (thisEditormd) {
			maskUtils.show(true);

			if (editorUtils.shared.data.currentObjKey != null) {
				// save to OSS
				editorUtils.shared.fn.put(function (ossOutput) {
					maskUtils.show(false);
					msgUtils.success("\" " + ossOutput.objects[0].key + " \" 已保存到云端 (" + ((ossOutput.objects[0].size) / 1024).toFixed(2) + "KB)");
				}, editorUtils.shared.data.currentObjKey, thisEditormd.getMarkdown());
			} else {
				msgUtils.warn("你还没有选择任何文件");
				maskUtils.show(false);
			}
		},
		getAll: function (thisEditormd) {

			maskUtils.show(true);

			// call service to get all files from OSS
			editorUtils.shared.fn.list(function (ossOutput) {

				maskUtils.show(false);

				var popupContent = $('<div class="list-group"></div>')
					.append('<a class="list-group-item active">Click a file to edit</a>');
				// each line content
				$.each(ossOutput.objects, function (index, object) {
					popupContent.append(
						$('<a class="list-group-item">' + object.key + '</a>')
							.on("click", function () {
								// call service to get single file content from OSS
								editorUtils.shared.fn.get(function (ossOutput) {

									popupUtils.close(thisEditormd);

									editorUtils.shared.data.currentObjKey = object.key;

									thisEditormd.cm.setValue(ossOutput.objects[0].content);

								}, object.key);
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
