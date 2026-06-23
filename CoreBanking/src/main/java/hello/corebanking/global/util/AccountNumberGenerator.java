package hello.corebanking.global.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class AccountNumberGenerator {

    private static final String BANK_CODE = "110";

    public String generate() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int randomPart = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return BANK_CODE + "-" + datePart + "-" + randomPart;
    }
}
