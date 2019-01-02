package com.example.shazahassan.booklistapp;

/**
 * Created by Shaza Hassan on 25-Sep-18.
 */
public class Book {
    private String title,author,uri;

    public Book(String title, String author, String uri) {
        this.title = title;
        this.author = author;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }


    public String getUri() {
        return uri;
    }
}
