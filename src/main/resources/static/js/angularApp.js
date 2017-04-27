var myApp = angular.module('myApp', [])
	.config(function ($logProvider) {
		$logProvider.debugEnabled(globalAppVar.config.angular.loggingDebug);
	})
	.controller('editorController', ['$scope', 'ossService',
		function ($scope, ossService) {

			// $scope definition
			$scope.oss = {
				key: null
			};

			// initialization
			editorUtils.shared.data.watch("currentObjKey", function (id, oldValue, newValue) {
				$scope.oss.key = newValue;
				return newValue;
			})
			editorUtils.shared.fn.save = ossService.save;
			editorUtils.shared.fn.get = ossService.get;

			editorUtils.init();

		}
	])
	.service('httpService', ['$http', 'logService', function ($http, logService) {
		this.post = function (apiurl, apidata,
		                      successFn,
		                      errorFn,
		                      exceptionFn) {
			logService.debug("send post request apiurl:" + apiurl + ", apidata:" + JSON.stringify(apidata));
			$http.post(apiurl, apidata).then(
				function (response) {
					logService.debug("receive post success response:" + JSON.stringify(response));
					if (successFn && typeof(successFn) == "function") {
						successFn(response);
					}
				},
				function (response) {
					logService.debug("receive post error response:" + JSON.stringify(response));
					if (errorFn && typeof(errorFn) == "function") {
						errorFn(response);
					}
				}
			).catch(
				function (response) {
					logService.debug("receive post exception response:" + JSON.stringify(response));
					if (exceptionFn && typeof(exceptionFn) == "function") {
						exceptionFn(response);
					}
				}
			);
		};
	}])
	.service('ossService', ['httpService', function (httpService) {
		this.save = function (successFn, objKey, objData) {
			httpService.post("/oss/save", {"objKey": objKey, "objData": objData},
				function (response) {
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
	}])
	.service('logService', ['$log', function ($log) {
		this.debug = function (logMsg) {
			$log.debug("=====>>>> DEBUG: " + logMsg);
		};
	}]);