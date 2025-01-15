package com.librarymanagement.LibraryManagement.ImageMetadata;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Service
public class ImageMetadataService {
    private ImageMetadataRepository imageMetadataRepository;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String connectionString;

    private BlobServiceClient blobServiceClient;

    public List<ImageMetadata> findAll() {
        return imageMetadataRepository.findAll();
    }

    @PostConstruct
    public void init() {
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    public ImageMetadata uploadImageWithCaption(MultipartFile image, String caption) throws IOException {
        String blobFilename = image.getOriginalFilename();
        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(containerName)
                .getBlobClient(blobFilename);
        blobClient.upload(image.getInputStream(), image.getSize(), true);
        String imageUrl = blobClient.getBlobUrl();
        ImageMetadata imageMetadata = new ImageMetadata(caption, imageUrl);

        return imageMetadataRepository.save(imageMetadata);
    }


    @Autowired
    public ImageMetadataService(ImageMetadataRepository imageMetadataRepository) {
        this.imageMetadataRepository = imageMetadataRepository;
    }
}
