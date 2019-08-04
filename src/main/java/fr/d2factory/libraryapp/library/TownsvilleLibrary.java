package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.BookStatus;
import fr.d2factory.libraryapp.library.exception.BookAlreadyBorrowedException;
import fr.d2factory.libraryapp.library.exception.HasLateBooksException;
import fr.d2factory.libraryapp.library.exception.MemberDoesNotHaveEnoughMoneyException;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Student;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

public class TownsvilleLibrary implements Library {

    public static final BigDecimal CHARGE_AMOUNT = new BigDecimal(0.1);
    public static final BigDecimal STUDENT_INCREASED_AMOUNT = new BigDecimal(0.15);
    public static final BigDecimal RESIDENT_INCREASED_AMOUNT = new BigDecimal(0.2);
    // All periods are by DAY
    public static final int STUDENT_KEEP_PERIOD = 30;
    public static final int FIRST_YEAR_STUDENT_FREE_PERIOD = 15;
    public static final int RESIDENT_KEEP_PERIOD = 60;

    private final BookRepository bookRepository = BookRepository.getInstance();

    // This List is used to store all actions of the libray
    // We can rely on this List to verify :
    // which book was borrowed by which member. If Member has returned his book ...
    private List<LibraryLog> libraryLogs = new ArrayList<>();

    @Override
    public Book borrowBook(@NonNull Long isbnCode, @NonNull Member member, @NonNull LocalDate borrowedAt) throws HasLateBooksException, BookAlreadyBorrowedException {
        // check if member have late book
        if (libraryLogs.stream()
                .anyMatch(log -> log.getMember().equals(member) && log.getReturnedAt() == null && log.getBorrowedAt().plusDays(member instanceof Student ? STUDENT_KEEP_PERIOD : RESIDENT_KEEP_PERIOD).isBefore(LocalDate.now())))
            throw new HasLateBooksException();
        else {
            Optional<Book> opBook = bookRepository.findBook(isbnCode, BookStatus.AVAILABLE);
            if (opBook.isPresent()) {
                // borrow the book
                bookRepository.saveBookBorrow(opBook.get());
                // save log
                libraryLogs.add(LibraryLog.builder()
                        .book(opBook.get())
                        .member(member)
                        .borrowedAt(borrowedAt)
                        .build());
                return opBook.get();
            } else throw new BookAlreadyBorrowedException();
        }
    }

    @Override
    public void returnBook(@NonNull Book book, @NonNull Member member) throws MemberDoesNotHaveEnoughMoneyException {
        libraryLogs.stream()
                .filter(log -> log.getMember().equals(member) && log.getBook().equals(book) && log.getReturnedAt() == null)
                .findFirst()
                .ifPresent(log -> {
                    bookRepository.saveBookReturn(log.getBook());
                    member.payBook((int) log.getBorrowedAt().until(LocalDate.now(), DAYS));
                });
    }
}
