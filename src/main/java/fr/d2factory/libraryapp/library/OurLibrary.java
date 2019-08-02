package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.member.Member;

import java.time.LocalDate;

public class OurLibrary implements Library {
    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
        return null;
    }

    @Override
    public void returnBook(Book book, Member member) {

    }
}
