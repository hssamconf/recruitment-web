package fr.d2factory.libraryapp.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Resident extends Member {
    @Override
    public void payBook(int numberOfDays) {

    }
}
