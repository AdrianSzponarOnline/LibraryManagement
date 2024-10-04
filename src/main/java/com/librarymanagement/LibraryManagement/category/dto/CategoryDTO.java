package com.librarymanagement.LibraryManagement.category.dto;

public class CategoryDTO {
    private long id;
    private String categoryName;

    public CategoryDTO(long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public CategoryDTO() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

}
