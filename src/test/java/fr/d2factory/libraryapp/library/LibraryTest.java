package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.exception.BookAlreadyBorrowedException;
import fr.d2factory.libraryapp.library.exception.HasLateBooksException;
import fr.d2factory.libraryapp.library.exception.MemberDoesNotHaveEnoughMoneyException;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Log
public class LibraryTest {
    private TownsvilleLibrary library;
    private Student firstYearStudent, student;
    private Resident resident;

    @BeforeEach
    public void setup() throws IOException {
        //Done - instantiate the library and the repository
        library = new TownsvilleLibrary();
        BookRepository bookRepository = BookRepository.getInstance();
        ObjectMapper objectMapper = new ObjectMapper();
        //DONE - add some test books (use BookRepository#addBooks) from books.json
        bookRepository.addBooks(objectMapper.readValue(getClass().getClassLoader().getResourceAsStream("books.json"), new TypeReference<List<Book>>() {
        }));
        // create test members all set with wallet amount of 10 EURO
        firstYearStudent = Student.builder().wallet(new BigDecimal(10)).yearOfStudy(1).build();
        student = Student.builder().wallet(new BigDecimal(10)).yearOfStudy(5).build();
        resident = Resident.builder().wallet(new BigDecimal(10)).build();
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available() {
        assertNotNull(library.borrowBook(46578964513L, student, LocalDate.now()));
        assertNotNull(library.borrowBook(3326456467846L, resident, LocalDate.now()));
    }

    @Test
    public void borrowed_book_is_no_longer_available() {
        library.borrowBook(46578964513L, resident, LocalDate.now());
        assertThrows(BookAlreadyBorrowedException.class, () -> library.borrowBook(46578964513L, student, LocalDate.now()));
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
        Book borrowedBook = library.borrowBook(46578964513L, resident, LocalDate.now().minusDays(10));

        library.returnBook(borrowedBook, resident);

        assertEquals(resident.getWallet().setScale(1, RoundingMode.HALF_EVEN), BigDecimal.valueOf(9.0));
    }

    @Test
    public void students_pay_10_cents_the_first_30days() {
        Book borrowedBook = library.borrowBook(46578964513L, student, LocalDate.now().minusDays(20));

        library.returnBook(borrowedBook, student);

        assertEquals(student.getWallet().setScale(1, RoundingMode.HALF_EVEN), BigDecimal.valueOf(8.0));
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days() {
        Book borrowedBook = library.borrowBook(46578964513L, firstYearStudent, LocalDate.now().minusDays(15));

        library.returnBook(borrowedBook, firstYearStudent);

        assertEquals(firstYearStudent.getWallet().setScale(1, RoundingMode.HALF_EVEN), BigDecimal.valueOf(10.0));
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {
        Book borrowedBook = library.borrowBook(46578964513L, student, LocalDate.now().minusDays(40));

        library.returnBook(borrowedBook, student);

        // (30 * 0.1) + (10 * 0.15) = 4.5 EURO a payer
        assertEquals(student.getWallet().setScale(1, RoundingMode.HALF_EVEN), BigDecimal.valueOf(5.5));
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
        Book borrowedBook = library.borrowBook(46578964513L, resident, LocalDate.now().minusDays(80));

        library.returnBook(borrowedBook, resident);

        // (60 * 0.1) + (20 * 0.2) = 10 EURO a payer
        assertEquals(resident.getWallet().setScale(0, RoundingMode.HALF_EVEN), BigDecimal.ZERO);
    }

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books() {
        library.borrowBook(46578964513L, student, LocalDate.now().minusDays(35));
        assertThrows(HasLateBooksException.class, () -> library.borrowBook(3326456467846L, student, LocalDate.now()));
    }


    @Test
    public void members_does_not_have_enough_money() {
        Book borrowedBook = library.borrowBook(46578964513L, resident, LocalDate.now().minusDays(90));
        assertThrows(MemberDoesNotHaveEnoughMoneyException.class, () -> library.returnBook(borrowedBook, resident));
    }
}
