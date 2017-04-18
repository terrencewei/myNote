var testEditor;

$(function () {
	testEditor = editormd("test-editormd", {
		width: "90%",
		height: 720,
		path: "/lib/editormd/lib/",
		tocStartLevel: 1,
		tocContainer: "#custom-toc-container",
		tocDropdown: true,
		tocTitle: "目录",
		toolbar : false,
	});


});