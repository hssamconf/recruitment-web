package fr.d2factory.libraryapp.book;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ISBN {
    @NonNull Long isbnCode;
}
