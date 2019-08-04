package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.Library;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
@Data
@AllArgsConstructor
public abstract class Member {
    /**
     * An initial sum of money the member has
     */
    // USE BigDecimal instead of float (https://stackoverflow.com/a/3730040/6364177)
    private BigDecimal wallet;

    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays);

}
