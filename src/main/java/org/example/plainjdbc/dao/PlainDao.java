package org.example.plainjdbc.dao;

import org.example.plainjdbc.model.Author;

import java.util.Set;

public interface PlainDao extends CoreDao {
    Set<Author> findAll();
    Set<Author> findAllByFirstName(String firstName);
    Author findById(Long id);
    String findFirstNameById(Long id);
    Author create(Author author);
    void update(Author author);
    void delete(Long id);
}
