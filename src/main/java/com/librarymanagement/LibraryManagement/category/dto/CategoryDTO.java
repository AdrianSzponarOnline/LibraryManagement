package com.librarymanagement.LibraryManagement.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CategoryDTO {
    private String categoryName;

    public CategoryDTO(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryDTO() {
    }
    @NotNull
    @Pattern(regexp = "")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }




    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryName='" + categoryName + '\'' +
                '}';
    }

}
