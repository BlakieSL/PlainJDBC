package org.example.plainjdbc.dao;

import org.example.plainjdbc.model.Author;
import org.example.plainjdbc.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.NotYetConnectedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.example.plainjdbc.helper.QueryStatements.*;

public class PlainDaoImpl implements PlainDao{
    private static final Logger LOGGER = LoggerFactory.getLogger(PlainDaoImpl.class);

    @Override
    public Set<Author> findAll() {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(ALL_SELECT);
                var resultSet = statement.executeQuery()
        ) {
            return mapResultSet(resultSet);
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing SELECT!", ex);
        }
        return Collections.emptySet();
    }

    @Override
    public Set<Author> findAllByFirstName(String firstName) {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(ALL_SELECT_BY_FIRST_NAME)
        ) {
            statement.setString(1, firstName);

            try (var resultSet = statement.executeQuery()) {
                return mapResultSet(resultSet);
            }

        } catch (SQLException ex) {
            LOGGER.error("Problem when executing SELECT!", ex);
        }
        return Collections.emptySet();
    }

    @Override
    public Author findById(Long id) {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(SELECT_BY_ID)
        ) {
            statement.setLong(1, id);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAuthor(resultSet);
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing SELECT!", ex);
        }
        return null;
    }

    @Override
    public String findFirstNameById(Long id) {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(SELECT_FIRST_NAME_BY_ID)
        ) {
            statement.setLong(1, id);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("first_name");
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing SELECT FIRST NAME BY ID!", ex);
        }
        return null;
    }

    @Override
    public Author create(Author author) {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating author failed, no rows affected.");
            }

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating author failed, no ID obtained.");
            }

            return author;
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing INSERT!", ex);
        }
        return null;
    }

    @Override
    public Author update(Author author) {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(UPDATE_AUTHOR)
        ) {
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.setLong(3, author.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating author failed, no rows affected.");
            }

            return author;
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing UPDATE!", ex);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(DELETE_AUTHOR)
        ) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.warn("No author found with id: {}", id);
                return false;
            }

            return true;
        } catch (SQLException ex) {
            LOGGER.error("Problem when executing DELETE!", ex);
        }
        return false;
    }


    private Author mapAuthor(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("author_id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastName(resultSet.getString("last_name"));
        author.setBooks(new HashSet<>());

        Long bookId = resultSet.getLong("book_id");
        if (!resultSet.wasNull()) {
            Book book = mapBook(resultSet);
            author.addBook(book);
        }

        return author;
    }

    private Book mapBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("book_id"));
        book.setAuthorId(resultSet.getLong("author_id"));
        book.setTitle(resultSet.getString("title"));
        var releaseDate = resultSet.getDate("release_date");
        if (releaseDate != null) {
            book.setReleaseDate(releaseDate.toLocalDate());
        }
        return book;
    }

    private Set<Author> mapResultSet(ResultSet resultSet) throws SQLException {
        Map<Long, Author> authorsMap = new HashMap<>();

        while (resultSet.next()) {
            Long authorId = resultSet.getLong("author_id");
            Author author = authorsMap.computeIfAbsent(authorId, id -> {
                try {
                    Author newAuthor = new Author();
                    newAuthor.setId(id);
                    newAuthor.setFirstName(resultSet.getString("first_name"));
                    newAuthor.setLastName(resultSet.getString("last_name"));
                    newAuthor.setBooks(new HashSet<>());
                    return newAuthor;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            resultSet.getLong("book_id");
            if (!resultSet.wasNull()) {
                author.addBook(mapBook(resultSet));
            }
        }

        return new HashSet<>(authorsMap.values());
    }
}
