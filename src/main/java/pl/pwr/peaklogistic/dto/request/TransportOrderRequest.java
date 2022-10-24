package pl.pwr.peaklogistic.dto.request;

import pl.pwr.peaklogistic.model.User;

import java.sql.Date;

public class TransportOrderRequest {
    private Long customerID;

    private String fromLocation;
    private String toLocation;

    private Date startDateFrom;
    private Date startDateTo;
    private Date endDateFrom;
    private Date endDateTo;

    private float weight;
    private int height;
    private int width;
    private int depth;
}
