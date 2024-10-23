package com.librarymanagement.LibraryManagement;

import com.librarymanagement.LibraryManagement.author.AuthorService;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LibraryManagementController {
    private final AuthorService authorService;

    @Autowired
    public LibraryManagementController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String getAuthors(Model model) {
        List<BaseAuthorDTO> authors = authorService.getAllAuthors().stream()
                .map(AuthorMapper::toBaseDTO)
                .collect(Collectors.toList());
        model.addAttribute("authors", authors);
        return "authors";
    }
}

