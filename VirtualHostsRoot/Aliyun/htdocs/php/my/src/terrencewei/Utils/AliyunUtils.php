<?php

namespace terrencewei\Utils;

// include all my php
require_once(dirname($_SERVER['DOCUMENT_ROOT']) . '/htdocs/php/my/autoload.php');
// include OSS config
require_once(dirname($_SERVER['DOCUMENT_ROOT']) . '/myfolder/php/my/autoload.php');
// include aliyun php sdk
require_once(dirname($_SERVER['DOCUMENT_ROOT']) . '/htdocs/php/lib/aliyun-oss-php-sdk-2.2.4/autoload.php');
use OSS\OssClient;
use OSS\Core\OssException;
use terrencewei\Config\OSSConfig;
use terrencewei\Model\OSSOutput;
use terrencewei\Model\OSSObject;

class AliyunUtils
{
    public static function put()
    {

        echo json_encode("TODO");
    }

    public static function get()
    {

        echo json_encode("TODO");
    }

    public static function list_()
    {
        try {
            $ossClient = new OssClient(OSSConfig::AK, OSSConfig::SK, OSSConfig::END_POINT);
        } catch (OssException $e) {
            print $e->getMessage();
        }
        $ossClient->setConnectTimeout(OSSConfig::EXPIRE_SECONDS);
        $listObjectInfo = AliyunUtils::listAllObjects($ossClient, OSSConfig::BUCKET_NAME);
        $ossOutput = new OSSOutput();
        $ossObjects = array();
        foreach ($listObjectInfo as $index => $ObjectInfo) {
            $ossObject = new OSSObject();
            $ossObject->setKey($ObjectInfo->getKey());
            array_push($ossObjects, $ossObject);
        }
        $ossOutput->setSuccess(true);
        $ossOutput->setObjects($ossObjects);
        echo json_encode($ossOutput);
    }

    /**
     * 列出Bucket内所有目录和文件， 根据返回的nextMarker循环调用listObjects接口得到所有文件和目录
     *
     * @param OssClient $ossClient OssClient实例
     * @param string $bucket 存储空间名称
     * @return null
     */
    private static function listAllObjects($ossClient, $bucket)
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

}