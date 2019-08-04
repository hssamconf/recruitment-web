package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.TownsvilleLibrary;
import fr.d2factory.libraryapp.library.exception.MemberDoesNotHaveEnoughMoneyException;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class Student extends Member {

    private int yearOfStudy;

    @Builder
    public Student(BigDecimal wallet, int yearOfStudy) {
        super(wallet);
        this.yearOfStudy = yearOfStudy;
    }

    @Override
    public void payBook(int numberOfDays) {
        if (numberOfDays > 0) {
            BigDecimal amount;
            if (numberOfDays <= TownsvilleLibrary.STUDENT_KEEP_PERIOD) {
                amount = TownsvilleLibrary.CHARGE_AMOUNT.multiply(BigDecimal.valueOf(numberOfDays));
            } else {
                int majoredDays = numberOfDays - TownsvilleLibrary.STUDENT_KEEP_PERIOD;
                amount = TownsvilleLibrary.CHARGE_AMOUNT.multiply(BigDecimal.valueOf(TownsvilleLibrary.STUDENT_KEEP_PERIOD));
                amount = amount.add(TownsvilleLibrary.STUDENT_INCREASED_AMOUNT.multiply(BigDecimal.valueOf(majoredDays)));
            }

            if (this.yearOfStudy == 1)
                amount = amount.subtract(TownsvilleLibrary.CHARGE_AMOUNT.multiply(BigDecimal.valueOf(TownsvilleLibrary.FIRST_YEAR_STUDENT_FREE_PERIOD)));

            if (amount.setScale(2, RoundingMode.HALF_EVEN).compareTo(this.getWallet()) >= 0)
                throw new MemberDoesNotHaveEnoughMoneyException();
            else if (amount.compareTo(BigDecimal.ZERO) > 0)
                this.setWallet(this.getWallet().subtract(amount));
        }
    }
}
