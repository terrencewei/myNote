<?php

// include all my php
require_once(dirname($_SERVER['DOCUMENT_ROOT']) . '/myfolder/php-my/autoload.php');

use terrencewei\Utils\AliyunUtils;

switch ($_POST["phpApiUrl"]) {
    case "/oss/put/cloud":
        $objs = $_POST["objects"];
        AliyunUtils::put($objs[0]["objKey"], $objs[0]["content"]);
        break;
    case "/oss/get/cloud":
        $objs = $_POST["objects"];
        AliyunUtils::get($objs[0]["objKey"]);
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
