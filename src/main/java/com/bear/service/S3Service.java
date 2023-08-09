package com.bear.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3Service {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    //public void uploadImage()
}
