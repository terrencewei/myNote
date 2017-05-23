<?php

namespace terrencewei\Model;

class OSSObject implements \JsonSerializable
{
    private $objKey;
    private $content;
    private $url;
    private $bucketName;
    private $hash;
    private $size;
    private $createdTime;
    private $updateTime;

    /**
     * @return mixed
     */
    public function getObjKey()
    {
        return $this->objKey;
    }

    /**
     * @param mixed $objKey
     */
    public function setObjKey($objKey)
    {
        $this->objKey = $objKey;
    }

    /**
     * @return mixed
     */
    public function getContent()
    {
        return $this->content;
    }

    /**
     * @param mixed $content
     */
    public function setContent($content)
    {
        $this->content = $content;
    }

    /**
     * @return mixed
     */
    public function getUrl()
    {
        return $this->url;
    }

    /**
     * @param mixed $url
     */
    public function setUrl($url)
    {
        $this->url = $url;
    }

    /**
     * @return mixed
     */
    public function getBucketName()
    {
        return $this->bucketName;
    }

    /**
     * @param mixed $bucketName
     */
    public function setBucketName($bucketName)
    {
        $this->bucketName = $bucketName;
    }

    /**
     * @return mixed
     */
    public function getHash()
    {
        return $this->hash;
    }

    /**
     * @param mixed $hash
     */
    public function setHash($hash)
    {
        $this->hash = $hash;
    }

    /**
     * @return mixed
     */
    public function getSize()
    {
        return $this->size;
    }

    /**
     * @param mixed $size
     */
    public function setSize($size)
    {
        $this->size = $size;
    }

    /**
     * @return mixed
     */
    public function getCreatedTime()
    {
        return $this->createdTime;
    }

    /**
     * @param mixed $createdTime
     */
    public function setCreatedTime($createdTime)
    {
        $this->createdTime = $createdTime;
    }

    /**
     * @return mixed
     */
    public function getUpdateTime()
    {
        return $this->updateTime;
    }

    /**
     * @param mixed $updateTime
     */
    public function setUpdateTime($updateTime)
    {
        $this->updateTime = $updateTime;
    }

    
}