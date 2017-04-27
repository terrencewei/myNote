var myApp = angular.module('myApp', [])
	.config(function ($logProvider) {
		$logProvider.debugEnabled(globalAppVar.config.angular.loggingDebug);
	})
	.controller('editorController', ['$scope', 'httpService', 'ossService', '$log', '$timeout',
		function ($scope, httpService, ossService, $log, $timeout) {

			// $scope definition
			$scope.oss = {
				key: null
			};

			// initialization
			editorUtils.shared.data.currentObjKey = $scope.oss.key;
			editorUtils.shared.data.watch("currentObjKey", function (id, oldValue, newValue) {
				$scope.oss.key = newValue;
				return newValue;
			})

			editorUtils.shared.fn.save = ossService.save;
			editorUtils.shared.fn.get = ossService.get;

			editorUtils.init();

		}
	])
	.service('httpService', ['$http', '$log', function ($http, $log) {
		this.post = function (apiurl, apidata,
		                      successFn,
		                      errorFn,
		                      exceptionFn) {
			$log.debug("send post request apiurl:" + apiurl + ", apidata:" + JSON.stringify(apidata));
			$http.post(apiurl, apidata).then(
				function (response) {
					$log.debug("receive post success response:" + JSON.stringify(response));
					if (successFn && typeof(successFn) == "function") {
						successFn(response);
					}
				},
				function (response) {
					$log.debug("receive post error response:" + JSON.stringify(response));
					if (errorFn && typeof(errorFn) == "function") {
						errorFn(response);
					}
				}
			).catch(
				function (response) {
					$log.debug("receive post exception response:" + JSON.stringify(response));
					if (exceptionFn && typeof(exceptionFn) == "function") {
						exceptionFn(response);
					}
				}
			);
		};
	}])
	.service('ossService', ['httpService', '$log', '$timeout', function (httpService, $log, $timeout) {
		this.save = function (successFn, objKey, objData) {
			httpService.post("/oss/save", {"objKey": objKey, "objData": objData},
				function (response) {
					$log.debug("oss save success!");
					$log.debug(response);
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
	}]);