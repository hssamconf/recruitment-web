package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private List<Book> availableBooks = new ArrayList<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    public void addBooks(List<Book> books) {
        availableBooks.addAll(books);
    }

    public Book findBook(long isbnCode) {
        //TODO implement the missing feature
        return null;
    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt) {
        //TODO implement the missing feature
    }

    public LocalDate findBorrowedBookDate(Book book) {
        //TODO implement the missing feature
        return null;
    }
}
