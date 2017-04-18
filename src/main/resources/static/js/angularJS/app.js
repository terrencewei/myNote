// MODULE
var myApp = angular.module('myApp', []);

// CONFIG

// CONTROLLER
myApp.controller('homeController', ['$scope',
	function ($scope) {

	}
]);

// DIRECTIVES

// SERVICE
myApp.service('httpService', ['$http', '$log', function ($http, $log) {
	this.post = function (apiURL, apiData,
	                      successCallBack,
	                      errCallBack,
	                      expCallBack) {
		$log.debug("send post request apiURL:" + apiURL + ", apiData:" + JSON.stringify(apiData));
		$http.post(apiURL, apiData).then(
			function (response) {
				$log.debug("receive post success response:" + JSON.stringify(response));
				successCallBack(response);
			},
			function (response) {
				$log.debug("receive post error response:" + JSON.stringify(response));
				errCallBack(response);
			}
		).catch(
			function (response) {
				$log.debug("receive post exception response:" + JSON.stringify(response));
				expCallBack(response);
			}
		);
	};

}]);

myApp.service('blogService', ['httpService', function (httpService) {
	this.getBlogs = function () {
		httpService.post("/blog", null,
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

