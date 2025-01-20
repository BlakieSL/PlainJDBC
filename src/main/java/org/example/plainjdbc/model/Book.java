package org.example.plainjdbc.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Book implements Serializable {
    private Long id;
    private Long authorId;
    private String title;
    private LocalDate releaseDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Book[id="
                + id
                + ", authorId=" + authorId
                + ", title=" + title
                + ", releaseDate=" + releaseDate;
    }
}
