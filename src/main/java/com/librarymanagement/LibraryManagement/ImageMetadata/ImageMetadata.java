package com.librarymanagement.LibraryManagement.ImageMetadata;

import jakarta.persistence.*;

@Entity
@Table(name = "metadata")
public class ImageMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "caption")
    private String caption;

    @Column(name = "image_url")
    private String imageUrl;

    public ImageMetadata() {
    }

    public ImageMetadata(String caption, String imageUrl) {
        this.caption = caption;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ImageMetadata{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

