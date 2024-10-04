package com.librarymanagement.LibraryManagement.category.dto;

import com.librarymanagement.LibraryManagement.category.Category;

public class CategoryMapper {
    public static CategoryDTO toCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName(category.getCategoryName());
        return categoryDTO;
    }
    public static Category toCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        return category;
    }
}
