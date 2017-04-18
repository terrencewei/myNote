$(function () {

	var myEditormd = editormd("test-editormd", {
		width: "90%",
		height: 720,
		path: "/lib/editormd/lib/",
		tocStartLevel: 1,
		tocContainer: "#custom-toc-container",
		tocDropdown: true,
		tocTitle: "Table of Contents",
		toolbar: true,
		toolbarIcons: function () {
			return ["toc", "|", "preview", "watch", "fullscreen", "|", "gotoDevExamples"]
		},
		toolbarCustomIcons: {
			toc: '<div class="markdown-body editormd-preview-container" id="custom-toc-container"></div>'
		},
		toolbarIconsClass: {
			gotoDevExamples: "fa-hand-o-right"  // FontAawsome class
		},
		toolbarHandlers: {
			gotoDevExamples: function (cm, icon, cursor, selection) {
				window.open("/lib/editormd/examples/index.html");
			}
		},
		onload : function() {
			// click full screen button
			$("i[name='fullscreen']").trigger("click");
		}
	});

});