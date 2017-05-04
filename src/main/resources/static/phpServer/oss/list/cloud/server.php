<?php
define('__ROOT__', dirname(dirname(dirname(dirname(dirname(dirname(__FILE__)))))));
define('__myfolder__', __ROOT__ . '/myfolder');
define('__aliyun_OSS_SDK__', __ROOT__ . '/htdocs/static/php/lib/aliyun-oss-php-sdk-2.2.4');

// -- start --  load oss config
require_once(__myfolder__ . '/php-config/OSSConfig.php');
$ossConfig = new OSSConfig();
//--end --load oss config

// -- start -- aliyun OSS
require_once(__aliyun_OSS_SDK__ . '/autoload.php');
use OSS\OssClient;
use OSS\Core\OssException;

$accessKeyId = $ossConfig->accessKey;
$accessKeySecret = $ossConfig->secureKey;
$endpoint = $ossConfig->endpoint;
$bucketName = $ossConfig->bucketName;
$timeout = $ossConfig->expireSeconds;


try {
    $ossClient = new OssClient($accessKeyId, $accessKeySecret, $endpoint);
} catch (OssException $e) {
    print $e->getMessage();
}
$ossClient->setConnectTimeout($timeout);
// start business logic
$listObjectInfo = listAllObjects($ossClient, $bucketName);


$ossOutput = new OSSOutput();
foreach ($listObjectInfo as $index => $ObjectInfo) {
    $ossObject = new OSSObject();
    $ossObject->key = $ObjectInfo->getKey();
    array_push($ossOutput->objects, $ossObject);
}
$ossOutput->success = true;
echo json_encode($ossOutput);
/**
 * 列出Bucket内所有目录和文件， 根据返回的nextMarker循环调用listObjects接口得到所有文件和目录
 *
 * @param OssClient $ossClient OssClient实例
 * @param string $bucket 存储空间名称
 * @return null
 */
function listAllObjects($ossClient, $bucket)
{
//    $prefix = 'dir/';
//    $delimiter = '/';
//    $nextMarker = '';
//    $maxkeys = 30;
    while (true) {
        $options = array(
//            'delimiter' => $delimiter,
//            'prefix' => $prefix,
//            'max-keys' => $maxkeys,
//            'marker' => $nextMarker,
        );
        try {
            $listObjectInfo = $ossClient->listObjects($bucket, $options);
        } catch (OssException $e) {
            printf(__FUNCTION__ . ": FAILED\n");
            printf($e->getMessage() . "\n");
            return;
        }
        // 得到nextMarker，从上一次listObjects读到的最后一个文件的下一个文件开始继续获取文件列表
        $nextMarker = $listObjectInfo->getNextMarker();
        $listObject = $listObjectInfo->getObjectList();
        if ($nextMarker === '') {
            return $listObject;
        }
    }
}

// -- end -- aliyun OSS

class OSSOutput
{
    public $success;
    public $code;
    public $msg;
    public $objects = array();
}

class OSSObject
{
    public $key;
    public $content;
    public $url;
    public $bucketName;
    public $hash;
    public $size;
    public $createdTime;
    public $updateTime;
}