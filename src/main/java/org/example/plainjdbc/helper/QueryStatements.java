package org.example.plainjdbc.helper;

public enum QueryStatements {
    ;
    public static final String ALL_SELECT = """
            SELECT
             a.id AS author_id, a.first_name, a.last_name,
             b.id AS book_id, b.title, b.release_date
            FROM AUTHOR a
            LEFT JOIN BOOK b ON a.id = b.author_id
            """;
    public static final String ALL_SELECT_BY_FIRST_NAME = """
            SELECT
             a.id AS author_id, a.first_name, a.last_name,
             b.id AS book_id, b.title, b.release_date
            FROM AUTHOR a
            LEFT JOIN BOOK b ON a.id = b.author_id
            WHERE a.first_name = ?
            """;
    public static final String SELECT_BY_ID = """
            SELECT
             a.id AS author_id, a.first_name, a.last_name,
             b.id AS book_id, b.title, b.release_date
            FROM AUTHOR a
            LEFT JOIN BOOK b ON a.id = b.author_id
            WHERE a.id = ?
            """;
    public static final String SELECT_FIRST_NAME_BY_ID = """
            SELECT first_name
            FROM AUTHOR
            WHERE id = ?
            """;
    public static final String INSERT_AUTHOR = """
            INSERT INTO author (first_name, last_name)
            VALUES (?, ?)
            """;
    public static final String UPDATE_AUTHOR = """
            UPDATE author
            SET first_name = ?, last_name = ?
            WHERE id = ?
            """;
    public static final String DELETE_AUTHOR = """
            DELETE FROM author WHERE id = ?
            """;


}
