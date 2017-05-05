<?php

namespace terrencewei\Utils;

// include all my php
require_once(dirname($_SERVER['DOCUMENT_ROOT']) . '/myfolder/php-my/autoload.php');
// include aliyun php sdk
require_once(dirname($_SERVER['DOCUMENT_ROOT']) . '/myfolder/php-lib/aliyun-oss-php-sdk-2.2.4/autoload.php');
use OSS\Core\OssException;
use OSS\OssClient;
use terrencewei\Config\OSSConfig;
use terrencewei\Model\OSSObject;
use terrencewei\Model\OSSOutput;

class AliyunUtils
{
    public static function put($objKey, $objContent)
    {

        try {
            $ossClient = new OssClient(OSSConfig::AK, OSSConfig::SK, OSSConfig::END_POINT);
        } catch (OssException $e) {
            print $e->getMessage();
        }
        $ossClient->setConnectTimeout(OSSConfig::EXPIRE_SECONDS);
        AliyunUtils::putObject($objKey, $objContent, $ossClient, OSSConfig::BUCKET_NAME);

        $obj = new OSSObject();
        $obj->setKey($objKey);
        $obj->setSize(strlen($objContent));
        // output
        $ossOutput = new OSSOutput();
        $ossOutput->setSuccess(true);
        $ossOutput->setObjects(array($obj));
        echo json_encode($ossOutput);
    }

    public static function get($objKey)
    {
        try {
            $ossClient = new OssClient(OSSConfig::AK, OSSConfig::SK, OSSConfig::END_POINT);
        } catch (OssException $e) {
            print $e->getMessage();
        }
        $ossClient->setConnectTimeout(OSSConfig::EXPIRE_SECONDS);
        $objContent = AliyunUtils::getObject($objKey, $ossClient, OSSConfig::BUCKET_NAME);

        $obj = new OSSObject();
        $obj->setKey($objKey);
        $obj->setContent($objContent);
        // output
        $ossOutput = new OSSOutput();
        $ossOutput->setSuccess(true);
        $ossOutput->setObjects(array($obj));
        echo json_encode($ossOutput);
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
     * 上传字符串作为object的内容
     *
     * @param OssClient $ossClient OSSClient实例
     * @param string $bucket 存储空间名称
     * @return null
     */
    function putObject($objKey, $objContent, $ossClient, $bucket)
    {
        try {
            $ossClient->putObject($bucket, $objKey, $objContent);
        } catch (OssException $e) {
            printf(__FUNCTION__ . ": FAILED\n");
            printf($e->getMessage() . "\n");
            return;
        }
    }

    /**
     * 获取object的内容
     *
     * @param OssClient $ossClient OSSClient实例
     * @param string $bucket 存储空间名称
     * @return null
     */
    private static function getObject($objKey, $ossClient, $bucket)
    {
        try {
            $content = $ossClient->getObject($bucket, $objKey);
        } catch (OssException $e) {
            printf(__FUNCTION__ . ": FAILED\n");
            printf($e->getMessage() . "\n");
            return;
        }
        return $content;
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