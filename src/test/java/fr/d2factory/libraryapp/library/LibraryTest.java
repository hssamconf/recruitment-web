package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@Log
public class LibraryTest {
    private Library library;
    private BookRepository bookRepository;

    @Before
    public void setup() throws IOException {
        //Done - instantiate the library and the repository
        library = new OurLibrary();
        bookRepository = new BookRepository();
        ObjectMapper objectMapper = new ObjectMapper();

        //DONE - add some test books (use BookRepository#addBooks) from books.json
        bookRepository.addBooks(objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("books.json"), new TypeReference<List<Book>>() {
        }));
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available() {
        // TODO
    }

    @Test
    public void borrowed_book_is_no_longer_available() {
        fail("Implement me");
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
        fail("Implement me");
    }

    @Test
    public void students_pay_10_cents_the_first_30days() {
        fail("Implement me");
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days() {
        fail("Implement me");
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {
        fail("Implement me");
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
        fail("Implement me");
    }

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books() {
        fail("Implement me");
    }
}
