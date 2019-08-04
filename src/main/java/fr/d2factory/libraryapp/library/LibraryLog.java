package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class LibraryLog {
    private Book book;
    private LocalDate borrowedAt;
    private LocalDate returnedAt;
    private Member member;
}
