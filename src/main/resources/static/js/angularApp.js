var myApp = angular.module('myApp', [])
	.config(function ($logProvider) {
		$logProvider.debugEnabled(globalAppVar.config.angular.loggingDebug);
	})
	.controller('editorController', ['$scope', 'ossService',
		function ($scope, ossService) {

			// $scope definition
			$scope.oss = {
				objKey: editorUtils.shared.data.currentObjKey
			};

			// bind editorUtils.shared.data.currentObjKey --> Angular $scope.oss.objKey
			editorUtils.shared.data.watch("currentObjKey", function (id, oldValue, newValue) {
				$scope.oss.objKey = newValue;
				return newValue;
			});
			// bind Angular $scope.oss.objKey --> editorUtils.shared.data.currentObjKey
			$scope.$watch('oss.objKey', function (newValue, oldValue, scope) {
				editorUtils.shared.data.currentObjKey = newValue;
			});

			editorUtils.shared.fn.putCloud = ossService.putCloud;
			editorUtils.shared.fn.getCloud = ossService.getCloud;
			editorUtils.shared.fn.listCloud = ossService.listCloud;
			editorUtils.shared.fn.removeCloud = ossService.removeCloud;

			editorUtils.shared.fn.putLocal = ossService.putLocal;
			editorUtils.shared.fn.getLocal = ossService.getLocal;
			editorUtils.shared.fn.listLocal = ossService.listLocal;
			editorUtils.shared.fn.removeLocal = ossService.removeLocal;

			editorUtils.init();

		}
	])
	.service('httpService', ['$http', 'logService', function ($http, logService) {
		this.post = function (apiurl, apidata,
		                      successFn,
		                      errorFn,
		                      exceptionFn) {
			logService.debug("send post request apiurl:" + apiurl + ", apidata:" + JSON.stringify(apidata));
			// if is using PHP as server
			var headers = {'Content-type': 'application/json;charset=UTF-8'};
			if (globalAppVar.config.angular.usePHPServer) {
				apidata = $.extend(apidata, {phpApiUrl: apiurl});
				apidata = $.param(apidata);

				apiurl = "/phpServer/server.php";

				headers = {'Content-Type': 'application/x-www-form-urlencoded'};
			}
			$http({
				method: 'POST',
				url: apiurl,
				data: apidata,
				headers: headers,
			}).then(
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
		var _this = this;
		this.putCloud = function (successFn, objKey, objData) {
			httpService.post("/oss/put/cloud", {
					bucketName: "",
					objects: [
						{
							objKey: objKey,
							content: objData
						}
					]
				},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					}
				});
		};
		this.getCloud = function (successFn, objKey) {
			httpService.post("/oss/get/cloud", {
					bucketName: "",
					objects: [
						{
							objKey: objKey
						}
					]
				},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					}
				});
		};
		this.listCloud = function (successFn, errorFn) {
			httpService.post("/oss/list/cloud", {},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					} else {
						errorFn(response.data);
					}
				});
		};
		this.removeCloud = function (successFn, objKey) {
			httpService.post("/oss/remove/cloud", {
					bucketName: "",
					objects: [
						{
							objKey: objKey
						}
					]
				},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					}
				});
		};
		this.putLocal = function (successFn, objKey, objData) {
			httpService.post("/oss/put/local", {
					bucketName: "",
					objects: [
						{
							objKey: objKey,
							content: objData
						}
					]
				},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					}
				});
		};
		this.getLocal = function (successFn, objKey) {
			httpService.post("/oss/get/local", {
					bucketName: "",
					objects: [
						{
							objKey: objKey
						}
					]
				},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					}
				});
		};
		this.listLocal = function (successFn, errorFn) {
			httpService.post("/oss/list/local", {},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					} else {
						errorFn(response.data);
					}
				});
		};
		this.removeLocal = function (successFn, objKey) {
			httpService.post("/oss/remove/local", {
					bucketName: "",
					objects: [
						{
							objKey: objKey
						}
					]
				},
				function (response) {
					if (response.data.success) {
						successFn(response.data);
					}
				});
		};
	}])
	.service('logService', ['$log', function ($log) {
		this.debug = function (logMsg) {
			$log.debug("=====>>>> DEBUG: " + logMsg);
		};
	}]);