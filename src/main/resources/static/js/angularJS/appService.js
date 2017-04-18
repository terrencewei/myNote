// service
myApp.service('httpservice', ['$http', '$log', function ($http, $log) {
	this.post = function (apiurl, apidata,
	                      successcallback,
	                      errcallback,
	                      expcallback) {
		$log.debug("send post request apiurl:" + apiurl + ", apidata:" + json.stringify(apidata));
		$http.post(apiurl, apidata).then(
			function (response) {
				$log.debug("receive post success response:" + json.stringify(response));
				successcallback(response);
			},
			function (response) {
				$log.debug("receive post error response:" + json.stringify(response));
				errcallback(response);
			}
		).catch(
			function (response) {
				$log.debug("receive post exception response:" + json.stringify(response));
				expcallback(response);
			}
		);
	};

}]);

myApp.service('blogservice', ['httpservice', function (httpservice) {
	this.getblogs = function () {
		httpservice.post("/blog", null,
			function (response) {
				console.log(response)
			},
			function (response) {
				console.log(response)
			},
			function (response) {
				console.log(response)
			}
		);
	};

}]);

