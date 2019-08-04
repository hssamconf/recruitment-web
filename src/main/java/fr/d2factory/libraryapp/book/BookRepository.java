package fr.d2factory.libraryapp.book;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The book repository emulates a database via 2 HashMaps
 */
@NoArgsConstructor
public final class BookRepository {

    private static BookRepository INSTANCE;

    private Map<Book, BookStatus> books = new HashMap<>();

    public static BookRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BookRepository();
        }
        return INSTANCE;
    }

    public void addBooks(List<Book> books) {
        for (Book b : books) this.books.put(b, BookStatus.AVAILABLE);
    }

    public Optional<Book> findBook(long isbnCode, BookStatus bookStatus) {
        return books.entrySet().stream()
                .filter(item -> item.getKey().getIsbn().getIsbnCode() == isbnCode && item.getValue() == bookStatus)
                .findFirst()
                .map(Map.Entry::getKey);
    }

    public void saveBookBorrow(Book book) {
        books.replace(book, BookStatus.AVAILABLE, BookStatus.UNAVAILABLE);
    }

    public void saveBookReturn(Book book) {
        books.replace(book, BookStatus.UNAVAILABLE, BookStatus.AVAILABLE);
    }
}
