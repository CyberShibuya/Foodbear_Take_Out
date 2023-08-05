package com.bear.controller;

import com.bear.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${bear.path}")
    private String basePath;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("get file: {}", file.toString());

        //check if folder exist or not
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        log.info(basePath);
        //get original file name and suffix
        String originalName = file.getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        //ensure not repeat the name using UUID in Java util
        String fileName = UUID.randomUUID() + suffix;


        try{
            file.transferTo(new File(basePath+ fileName));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return Result.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws FileNotFoundException {//Here the download actually means loading img

        try {
            //InputStream to read img file
            FileInputStream inputStream = new FileInputStream(basePath + name);
            //OutputStram to write img file to browser
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

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
