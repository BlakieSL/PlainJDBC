package org.example.plainjdbc;

import org.example.plainjdbc.dao.PlainDaoImpl;
import org.example.plainjdbc.model.Author;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.logging.Logger;


class PlainDaoImplTest {
    private final PlainDaoImpl plainDao = new PlainDaoImpl();
    private static final Logger LOGGER = Logger.getLogger(PlainDaoImplTest.class.getName());

    @Test
    void testFindAllAuthors() {
        Set<Author> authors = plainDao.findAll();
        LOGGER.info("Authors: " + authors);
    }


}
