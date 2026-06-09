package com.yujie.studentapi.controller;

import com.yujie.studentapi.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error(400, "上传文件不能为空");
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            return Result.error(400, "图片大小不能超过 2MB");
        }

        String contentType = file.getContentType();

        if (contentType == null
                || (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png")
                && !contentType.equals("image/gif")
                && !contentType.equals("image/webp"))) {
            return Result.error(400, "文件类型不合法，只支持 jpg、jpeg、png、gif、webp 图片");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            return Result.error(400, "文件名不能为空");
        }

        String lowerName = originalFilename.toLowerCase(Locale.ROOT);

        if (!lowerName.endsWith(".jpg")
                && !lowerName.endsWith(".jpeg")
                && !lowerName.endsWith(".png")
                && !lowerName.endsWith(".gif")
                && !lowerName.endsWith(".webp")) {
            return Result.error(400, "只支持 jpg、jpeg、png、gif、webp 图片");
        }

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            if (Files.exists(uploadPath) && !Files.isDirectory(uploadPath)) {
                return Result.error(500, "上传路径不是文件夹：" + uploadPath);
            }

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String suffix = lowerName.substring(lowerName.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + suffix;

            Path targetPath = uploadPath.resolve(newFilename).normalize();

            if (!targetPath.startsWith(uploadPath)) {
                return Result.error(400, "文件路径不合法");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            String url = "/uploads/" + newFilename;

            return Result.success("上传成功", url);
        } catch (Exception e) {
            return Result.error(500, "文件保存失败：" + e.getClass().getName() + "：" + e.getMessage());
        }
    }
}