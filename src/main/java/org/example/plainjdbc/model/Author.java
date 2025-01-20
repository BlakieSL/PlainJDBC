package org.example.plainjdbc.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Author implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public boolean addBook(Book book) {
        if (books == null) {
            books = new HashSet<>();
            books.add(book);
            return true;
        } else {
            if (books.contains(book)) {
                return false;
            }
        }
        books.add(book);
        return true;
    }

    @Override
    public String toString() {
        return "Author[id=" +
                id +
                ",firstName=" + firstName +
                ",lastName=" + lastName +
                "]";
    }
}
