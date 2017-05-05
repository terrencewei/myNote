<?php

// include all my php
require_once(dirname($_SERVER['DOCUMENT_ROOT']) . '/htdocs/php/my/autoload.php');

use terrencewei\Utils\AliyunUtils;

switch ($_POST["phpServerUrl"]) {
    case "/oss/put/cloud":
        AliyunUtils::put();
        break;
    case "/oss/get/cloud":
        AliyunUtils::get();
        break;
    case "/oss/list/cloud":
        AliyunUtils::list_();
        break;
    case "/oss/remove/cloud":
        break;
    case "/oss/put/local":
        break;
    case "/oss/get/local":
        break;
    case "/oss/list/local":
        break;
    case "/oss/remove/local":
        break;
}
