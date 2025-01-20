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
}
