package com.librarymanagement.LibraryManagement.category;

import com.librarymanagement.LibraryManagement.category.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Read all categories
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Read a single category by ID
    public Category getById(long id) {
         return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }
    public Optional<Category> getByName(String name) {
        return categoryRepository.findByCategoryName(name);
    }

    // Create a new category
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Update an existing category
    public Category updateCategory(long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        category.setCategoryName(categoryDetails.getCategoryName());

        return categoryRepository.save(category);  // Save the updated category
    }

    // Delete a category by ID
    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }
}