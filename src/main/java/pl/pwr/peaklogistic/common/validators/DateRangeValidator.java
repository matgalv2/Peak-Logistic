package pl.pwr.peaklogistic.common.validators;

import pl.pwr.peaklogistic.dto.request.transportOffer.PostTransportOffer;
import pl.pwr.peaklogistic.dto.request.transportOrder.PostTransportOrder;


public class DateRangeValidator {
    public static boolean validate(PostTransportOrder order)
    {
        boolean startFromBeforeStartTo = order.getStartDateFrom().compareTo(order.getStartDateTo()) <= 0;
        boolean endFromBeforeEndTo = order.getEndDateFrom().compareTo(order.getEndDateTo()) <= 0;
        boolean endNotBeforeStart1 = order.getEndDateFrom().compareTo(order.getStartDateFrom()) >= 0;
        boolean endNotBeforeStart2 = order.getEndDateTo().compareTo(order.getStartDateTo()) >= 0;
        return startFromBeforeStartTo && endFromBeforeEndTo && endNotBeforeStart1 && endNotBeforeStart2;
    }

    public static boolean validate(PostTransportOffer offer){
        return offer.getStartDate().compareTo(offer.getEndDate()) <= 0;
    }
}
