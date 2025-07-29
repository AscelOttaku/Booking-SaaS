package kg.attractor.bookingsaas.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtil {

    public static String convertLocalDate(LocalDate localDate) {
        Assert.notNull(localDate, "localDate must not be null");

        return DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(localDate);
    }

    public static String convertToLocalDateTime(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime must not be null");

        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(localDateTime);
    }
}
