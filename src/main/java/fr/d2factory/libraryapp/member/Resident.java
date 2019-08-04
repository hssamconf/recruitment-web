package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.TownsvilleLibrary;
import fr.d2factory.libraryapp.library.exception.MemberDoesNotHaveEnoughMoneyException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.java.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Log
public class Resident extends Member {

    @Builder
    public Resident(BigDecimal wallet) {
        super(wallet);
    }

    @Override
    public void payBook(int numberOfDays) {
        if (numberOfDays > 0) {
            BigDecimal amount;
            if (numberOfDays <= TownsvilleLibrary.RESIDENT_KEEP_PERIOD){
                amount = TownsvilleLibrary.CHARGE_AMOUNT.multiply(BigDecimal.valueOf(numberOfDays));
            }
            else {
                int majoredDays = numberOfDays - TownsvilleLibrary.RESIDENT_KEEP_PERIOD;
                amount = TownsvilleLibrary.CHARGE_AMOUNT.multiply(BigDecimal.valueOf(TownsvilleLibrary.RESIDENT_KEEP_PERIOD));
                amount = amount.add(TownsvilleLibrary.RESIDENT_INCREASED_AMOUNT.multiply(BigDecimal.valueOf(majoredDays)));
            }
            if (amount.setScale(2,RoundingMode.HALF_EVEN).compareTo(this.getWallet()) > 0)
                throw new MemberDoesNotHaveEnoughMoneyException();
            else
                this.setWallet(this.getWallet().subtract(amount));
        }
    }
}
