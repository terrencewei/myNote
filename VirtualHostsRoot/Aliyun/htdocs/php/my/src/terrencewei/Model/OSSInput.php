<?php

namespace terrencewei\Model;

class OSSInput implements \JsonSerializable
{
    private $bucketName;
    private $objects = array();

    public function jsonSerialize()
    {
        return get_object_vars($this);
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
     * @return array
     */
    public function getObjects()
    {
        return $this->objects;
    }

    /**
     * @param array $objects
     */
    public function setObjects($objects)
    {
        $this->objects = $objects;
    }


}