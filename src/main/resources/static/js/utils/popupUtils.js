/** ---------------------------------------------------------------------------------- */
/** popup utils */
/** ---------------------------------------------------------------------------------- */
var popupUtils = {
	zIndex: globalAppVar.config.zIndex,
	popup: null,
	show: function (pEditormd) {
		var _this = this;
		var classPrefix = pEditormd.classPrefix;
		var editor = pEditormd.editor;

		pEditormd.lockScreen(true);

		$("html,body").css("overflow-x", "hidden");
		pEditormd.mask.css({
			opacity: "0.3",
			backgroundColor: "#000"
		}).show();
		_this.popup.css("z-index", _this.zIndex).show();

		_this.reset(_this.popup);
		$(window).resize(_this.reset(_this.popup));
	},
	update: function (pPopupContent, pEditormd) {
		var _this = this;
		_this.popup.find("." + pEditormd.classPrefix + "dialog-container").text("").append(pPopupContent);
		_this.reset();
	},
	close: function (pEditormd) {
		$("html,body").css("overflow-x", "");
		pEditormd.editor.children("." + pEditormd.classPrefix + "dialog-info").hide();
		pEditormd.mask.hide();
		pEditormd.lockScreen(false);
	},
	reset: function () {
		var _this = this;
		_this.popup.css({
			top: ($(window).height() - _this.popup.height()) / 2 + "px",
			left: ($(window).width() - _this.popup.width()) / 2 + "px"
		});
	},
	init: function (pEditormd) {
		var _this = this;
		var classPrefix = pEditormd.classPrefix;
		var editor = pEditormd.editor;
		var popup = editor.children("." + classPrefix + "dialog-info");

		// create a new popup
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

		pEditormd.mask.bind(window.editormd.mouseOrTouch("click", "touchend"), function () {
			_this.close(pEditormd);
		});

		_this.popup = popup;
	}
};