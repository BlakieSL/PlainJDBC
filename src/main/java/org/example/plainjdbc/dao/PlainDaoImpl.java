package org.example.plainjdbc.dao;

import org.example.plainjdbc.model.Author;
import org.example.plainjdbc.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

import static org.example.plainjdbc.helper.QueryStatements.ALL_SELECT;

public class PlainDaoImpl implements PlainDao{
    private static final Logger LOGGER = LoggerFactory.getLogger(PlainDaoImpl.class);

    @Override
    public Set<Author> findAll() {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(ALL_SELECT);
                var resultSet = statement.executeQuery()
        ) {
            Map<Long, Author> authorsMap = new HashMap<>();

            while (resultSet.next()) {
                Long authorId = resultSet.getLong("author_id");
                Author author = authorsMap.get(authorId);
                if (author == null) {
                    author = new Author();
                    author.setId(authorId);
                    author.setFirstName(resultSet.getString("first_name"));
                    author.setLastName(resultSet.getString("last_name"));
                    author.setBooks(new HashSet<>());
                    authorsMap.put(authorId, author);
                }

                Long bookId = resultSet.getLong("book_id");
                if (!resultSet.wasNull()) {
                    var book = new Book();
                    book.setId(bookId);
                    book.setAuthorId(authorId);
                    book.setTitle(resultSet.getString("title"));
                    var date = resultSet.getDate("release_date");
                    if (date != null) {
                        book.setReleaseDate(date.toLocalDate());
                    }
                    author.addBook(book);
                }
            }

            return new HashSet<>(authorsMap.values());
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing SELECT!", ex);
        }
        return Collections.emptySet();
    }


    @Override
    public Set<Author> findAllByFirstName(String firstName) {
        return Set.of();
    }

    @Override
    public Author findById(Long id) {
        return null;
    }

    @Override
    public String findFirstNameById(Long id) {
        return "";
    }

    @Override
    public Author create(Author author) {
        return null;
    }

    @Override
    public void update(Author author) {

    }

    @Override
    public void delete(Long id) {

    }
}
