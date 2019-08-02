package fr.d2factory.libraryapp.book;

import lombok.Data;

/**
 * A simple representation of a book
 */
@Data
public class Book {
    String title;
    String author;
    ISBN isbn;
}
