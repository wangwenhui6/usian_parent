package com.usian.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.usian.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FastFileStorageClient storageClient;

    private static final List<String> cpmtemt_types = Arrays.asList("image/jpeg","image/gif","image/png");

    @RequestMapping("/upload")
    public Result fileUpload (MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        //校验文件类型
        String contentType = file.getContentType();
        if(!cpmtemt_types.contains(contentType)){
            //文件类型不合法，直接俄返回
            return Result.error("文件类型不合法:"+originalFilename);
        }
        //校验文件内容
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null){
            return Result.error("文件类型不合法:"+originalFilename);
        }

        //保存到服务器
        String ext = StringUtils.substringAfterLast(originalFilename, ".");
        // 上传并保存图片，参数：1-上传的文件流 2-文件的大小 3-文件的后缀 4-可以不管他
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
        //生成url的地址 返回
        return Result.ok("http://image.usian.com/" + storePath.getFullPath());
    }
}
