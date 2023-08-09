package com.bear.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.bear.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;


@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

//    @Value("${bear.path}")
//    private String basePath;

//    @PostMapping("/upload")
//    public Result<String> upload(MultipartFile file) throws IOException {
//        log.info("get file: {}", file.toString());
//
//        //check if folder exist or not
//        File dir = new File(basePath);
//        if (!dir.exists()){
//            dir.mkdirs();
//        }
//        log.info(basePath);
//        //get original file name and suffix
//        String originalName = file.getOriginalFilename();
//        String suffix = originalName.substring(originalName.lastIndexOf("."));
//        //ensure not repeat the name using UUID in Java util
//        String fileName = UUID.randomUUID() + suffix;
//
//
//        try{
//            file.transferTo(new File(basePath+ fileName));
//        }catch (IOException e){
//            throw new RuntimeException(e);
//        }
//        return Result.success(fileName);
//    }

    @Value("${aws.s3.bucketUrl}")
    private String bucketUrl;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("get file: {}", file.toString());

        //get original file name and suffix
        String originalName = file.getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        //ensure not repeat the name using UUID in Java util
        String s3Key = UUID.randomUUID() + suffix;

        try {
            // Set the metadata for the S3 object
            ObjectMetadata  metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // Upload the file to Amazon S3
            amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Construct the URL of the uploaded file on S3
        //String s3Url = bucketUrl + s3Key;

        return Result.success(s3Key);
    }

//    @GetMapping("/download")
//    public void download(String name, HttpServletResponse response) throws FileNotFoundException {//Here the download actually means loading img
//
//        try {
//            //InputStream to read img file
//            FileInputStream inputStream = new FileInputStream(basePath + name);
//            //OutputStram to write img file to browser
//            ServletOutputStream outputStream = response.getOutputStream();
//
//            response.setContentType("image/jpeg");
//
//            int len;
//            byte[] bytes = new byte[1024];
//            while ((len = inputStream.read(bytes)) != -1){
//                outputStream.write(bytes, 0, len);
//                outputStream.flush();
//            }
//            //close stream
//            inputStream.close();
//            outputStream.close();
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws FileNotFoundException {//Here the download actually means loading img

        try {
            //get s3 object
            S3Object s3Object = amazonS3.getObject(bucketName, name);

            response.setContentType(s3Object.getObjectMetadata().getContentType());

            //Initialize input and output streams
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = s3Object.getObjectContent();

            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //close stream
            inputStream.close();
            outputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
