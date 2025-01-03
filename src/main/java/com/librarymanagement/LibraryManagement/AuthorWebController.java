package com.librarymanagement.LibraryManagement;

import com.librarymanagement.LibraryManagement.author.Author;
import com.librarymanagement.LibraryManagement.author.AuthorService;
import com.librarymanagement.LibraryManagement.author.dto.AuthorMapper;
import com.librarymanagement.LibraryManagement.author.dto.BaseAuthorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LibraryManagementController {
    private final AuthorService authorService;

    @Autowired
    public LibraryManagementController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Zarządzanie Biblioteką");
        model.addAttribute("welcomeMessage", "Witamy w systemie zarządzania biblioteką");
        model.addAttribute("year", LocalDate.now().getYear());
        model.addAttribute("activePage", "home");
        return "index";
    }

    @GetMapping("/authors")
    public String getAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Page<BaseAuthorDTO> authorsPage = authorService.getAllAuthors(PageRequest.of(page, size))
                .map(AuthorMapper::toBaseDTO);

        model.addAttribute("authorsPage", authorsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", authorsPage.getTotalPages());
        model.addAttribute("activePage", "authors");
        return "authors"; // Zwracamy pełen template authors.html
    }
    @PostMapping("/authors")
    public String createAuthorFromForm(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String nationality,
            @RequestParam String dateOfBirth
    ) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setNationality(nationality);
        author.setDateOfBirth(LocalDate.parse(dateOfBirth)); // Parsowanie do daty

        authorService.saveAuthor(author);

        // Po dodaniu autora przekieruj do listy autorów
        return "redirect:/authors";
    }

}

