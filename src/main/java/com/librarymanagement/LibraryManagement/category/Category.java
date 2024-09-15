package com.librarymanagement.LibraryManagement.genre;

import jakarta.persistence.*;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

}
