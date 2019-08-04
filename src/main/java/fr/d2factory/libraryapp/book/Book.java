package fr.d2factory.libraryapp.book;

import lombok.*;

/**
 * A simple representation of a book
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    String title;
    String author;
    @NonNull ISBN isbn;
}
