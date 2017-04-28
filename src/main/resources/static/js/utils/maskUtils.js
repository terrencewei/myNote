/** ---------------------------------------------------------------------------------- */
/** mask utils
 /** ---------------------------------------------------------------------------------- */
var maskUtils = {
	zIndex: globalAppVar.config.zIndex,
	mask: null,
	show: function (isShow) {
		var _this = this;
		if (isShow) {
			_this.mask.show();
		} else {
			_this.mask.hide();
		}
	},
	init: function () {
		var _this = this;
		var mask = $("<div>")
			.addClass("maskContainer")
			.css("width", "100%")
			.css("height", "100%")
			.css("top", "0")
			.css("left", "0")
			.css("position", "fixed")
			.css("background", "#000")
			.css("z-index", "99998")
			.css("opacity", "0.3")
			.css("background-color", "rgb(0, 0, 0)")
			.append('<table width=\"100%\" height=\"100%\"><tr><td align=\"center\"><i class=\"fa fa-spinner fa-spin fa-3x fa-fw\"></i></td></tr></table>');

		// build mask div
		$("body").children().first().before(mask);

		_this.mask = mask;

		_this.show(false);
	}
};