package com.app.room_navigation_service.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {

    private final MinioClient minioClient;
    private final String bucketName = "room-images";

    public StorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadFile(MultipartFile file, String folder) throws Exception {
        // รวม folder + ชื่อไฟล์
        String objectName = folder + "/" + file.getOriginalFilename();

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        }

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .method(Method.GET)
                        .expiry(7, TimeUnit.DAYS)
                        .build()
        );
    }


    public String uploadAsWebp(MultipartFile file, String folder) throws Exception {

        int orientation = 1;
        try (InputStream is = file.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (Exception e) {
            orientation = 1;
        }


        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) throw new IllegalArgumentException("ไฟล์ไม่ใช่รูปภาพที่ถูกต้อง");


        image = rotateImage(image, orientation);


        ImageWriter writer = ImageIO.getImageWritersByFormatName("webp").next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[0]);
        writeParam.setCompressionQuality(0.75f);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (var ios = ImageIO.createImageOutputStream(os)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), writeParam);
        } finally {
            writer.dispose();
        }

        byte[] webpBytes = os.toByteArray();


        String originalFilename = file.getOriginalFilename();
        String fileNameOnly = (originalFilename != null)
                ? originalFilename.replaceAll("\\.[^.]+$", "").replaceAll("[^a-zA-Z0-9.-]", "_")
                : "image_" + System.currentTimeMillis();
        String objectName = folder + "/" + fileNameOnly + ".webp";


        try (InputStream is = new ByteArrayInputStream(webpBytes)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, webpBytes.length, -1)
                            .contentType("image/webp")
                            .build()
            );
        }


        return String.format("http://localhost:9000/%s/%s", bucketName, objectName);
    }

    private BufferedImage rotateImage(BufferedImage img, int orientation) {
        AffineTransform at = new AffineTransform();
        int width = img.getWidth();
        int height = img.getHeight();

        switch (orientation) {
            case 3: // หมุน 180 องศา
                at.translate(width, height);
                at.rotate(Math.PI);
                break;
            case 6: // หมุน 90 องศา (ตามเข็มนาฬิกา)
                BufferedImage newImg6 = new BufferedImage(height, width, img.getType());
                Graphics2D g2d6 = newImg6.createGraphics();
                g2d6.translate(height, 0);
                g2d6.rotate(Math.PI / 2.0);
                g2d6.drawRenderedImage(img, null);
                g2d6.dispose();
                return newImg6;
            case 8: // หมุน 270 องศา (หรือ 90 องศาทวนเข็มนาฬิกา)
                BufferedImage newImg8 = new BufferedImage(height, width, img.getType());
                Graphics2D g2d8 = newImg8.createGraphics();
                g2d8.translate(0, width);
                g2d8.rotate(-Math.PI / 2.0);
                g2d8.drawRenderedImage(img, null);
                g2d8.dispose();
                return newImg8;
            default:
                return img;
        }
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return op.filter(img, null);
    }
}
