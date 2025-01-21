package org.example.plainjdbc;

import org.example.plainjdbc.dao.CoreDao;
import org.example.plainjdbc.dao.PlainDaoImpl;
import org.example.plainjdbc.model.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.SQLException;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

class PlainDaoImplTest implements CoreDao {
    private final PlainDaoImpl plainDao = new PlainDaoImpl();
    private static final Logger LOGGER = LoggerFactory.getLogger(PlainDaoImplTest.class);
    private static final String INSERT_AUTHOR = """
            INSERT INTO author (first_name, last_name) VALUES (?, ?)
            """;
    private static final String INSERT_BOOK = """
            INSERT INTO book (author_id, title, release_date) VALUES (?, ?, ?)
            """;
    private static final String DELETE_AUTHOR = "DELETE FROM author";
    private static final String DELETE_BOOK = "DELETE FROM book";
    private static final String RESET_AUTO_INCREMENT_AUTHOR = "ALTER TABLE author AUTO_INCREMENT = 1";
    private static final String RESET_AUTO_INCREMENT_BOOK = "ALTER TABLE book AUTO_INCREMENT = 1";

    @AfterEach
    void clearDatabase() {
        try (
                var connection = getConnection();
                var statement = connection.createStatement()
        ) {
            statement.execute(DELETE_BOOK);
            statement.execute(DELETE_AUTHOR);
            statement.execute(RESET_AUTO_INCREMENT_AUTHOR);
            statement.execute(RESET_AUTO_INCREMENT_BOOK);
        } catch (SQLException ex) {
            LOGGER.error("Error clearing database: {}", ex.getMessage());
        }
    }

    @BeforeEach
    void populateTestData() {
        long johnId = insertAuthor("John", "Doe");
        long janeId = insertAuthor("Jane", "Smith");
        insertBook(johnId, "Book 1", "2023-01-01");
        insertBook(janeId, "Book 2", "2023-02-01");
    }

    @Test
    void testFindAllAuthors() {
        Set<Author> authors = plainDao.findAll();
        LOGGER.info("Authors: {}", authors);

        assertEquals(2, authors.size());
    }

    @Test
    void testFindAllByFirstName() {
        Set<Author> authors = plainDao.findAllByFirstName("John");
        LOGGER.info("Authors: {}", authors);

        assertEquals(1, authors.size());
        assertEquals("Doe", authors.iterator().next().getLastName());
    }

    @Test
    void testFindById() {
        Author author = plainDao.findById(1L);
        LOGGER.info("Author: {}", author);

        assertNotNull(author);
        assertEquals("John", author.getFirstName());
    }

    @Test
    void testFindFirstNameById() {
        String firstName = plainDao.findFirstNameById(1L);
        LOGGER.info("First Name: {}", firstName);

        assertEquals("John", firstName);
    }

    @Test
    void testCreate() {
        Author author = new Author();
        author.setFirstName("Alice");
        author.setLastName("Johnson");
        Author createdAuthor = plainDao.create(author);
        LOGGER.info("Created Author: {}", createdAuthor);

        assertNotNull(createdAuthor.getId());
        assertEquals("Alice", createdAuthor.getFirstName());
        assertEquals("Johnson", createdAuthor.getLastName());
    }

    @Test
    void testUpdate() {
        Author author = plainDao.findById(1L);
        assertNotNull(author);

        author.setFirstName("Updated Name");
        Author updatedAuthor = plainDao.update(author);
        LOGGER.info("Updated Author: {}", updatedAuthor);

        assertEquals("Updated Name", updatedAuthor.getFirstName());
    }

    @Test
    void testDelete() {
        boolean isDeleted = plainDao.delete(1L);
        LOGGER.info("Is Deleted: {}", isDeleted);

        assertTrue(isDeleted);
        assertNull(plainDao.findById(1L));
    }

    private long insertAuthor(String firstName, String lastName) {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(INSERT_AUTHOR, java.sql.Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.executeUpdate();

            try (var keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("Error inserting author: {}", ex.getMessage());
        }
        throw new RuntimeException("Failed to insert author: " + firstName + " " + lastName);
    }

    private void insertBook(long authorId, String title, String releaseDate) {
        try (
                var connection = getConnection();
                var statement = connection.prepareStatement(INSERT_BOOK)
        ) {
            statement.setLong(1, authorId);
            statement.setString(2, title);
            statement.setDate(3, java.sql.Date.valueOf(releaseDate));
            statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error("Error inserting book: {}", ex.getMessage());
            throw new RuntimeException("Failed to insert book: " + title);
        }
    }
}