package pl.pwr.peaklogistic.common.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DateRangeValidatorTest {

    private PostTransportOrder generatePostOrder(Date startDateFrom, Date startDateTo, Date endDateFrom, Date endDateTo){
        return new PostTransportOrder(false,
                "",
                "",
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo,
                0.5f,
                20,
                20,
                20);
    }

    @Test
    @DisplayName("Succeeds with correct dates (starts before ends)")
    void correct_dates_case1(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-28"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-4-1"),
                Date.valueOf("2022-4-2")
        );
        assertTrue(DateRangeValidator.validate(postTransportOrder));
    }

    @Test
    @DisplayName("Succeeds with correct dates (equal starts and equal ends)")
    void correct_dates_case2(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-4-1"),
                Date.valueOf("2022-4-1")
        );
        assertTrue(DateRangeValidator.validate(postTransportOrder));
    }

    @Test
    @DisplayName("Succeeds with correct dates (equal starts and equal ends)")
    void correct_dates_case3(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-28"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-4-1")
        );
        assertTrue(DateRangeValidator.validate(postTransportOrder));
    }
    @Test
    @DisplayName("Succeeds with correct dates (all the same)")
    void correct_dates_case4(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29")
        );
        assertTrue(DateRangeValidator.validate(postTransportOrder));
    }

    @Test
    @DisplayName("Succeeds with correct dates (interlocking)")
    void correct_dates_case5(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-20"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-22"),
                Date.valueOf("2022-3-30")
        );
        assertTrue(DateRangeValidator.validate(postTransportOrder));
    }

    @Test
    @DisplayName("Succeeds with correct dates (equal datesFrom and equal datesTo)")
    void correct_dates_case6(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-20"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-20"),
                Date.valueOf("2022-3-29")
        );
        assertTrue(DateRangeValidator.validate(postTransportOrder));
    }

    @Test
    @DisplayName("Fails when startDateFrom is after startDateTo")
    void wrong_dates_start(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-28"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29")
        );
        assertFalse(DateRangeValidator.validate(postTransportOrder));
    }

    @Test
    @DisplayName("Fails when endDateFrom is after endDateTo")
    void wrong_dates_end(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-28")
        );
        assertFalse(DateRangeValidator.validate(postTransportOrder));
    }

    @Test
    @DisplayName("Fails when endDateFrom is before startDateFrom")
    void wrong_dates_(){
        PostTransportOrder postTransportOrder = generatePostOrder(
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-29"),
                Date.valueOf("2022-3-28")
        );
        assertFalse(DateRangeValidator.validate(postTransportOrder));
    }


}
