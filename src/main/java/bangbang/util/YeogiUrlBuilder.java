package bangbang.util;

import bangbang.dto.CrawlingRequest;

public class YeogiUrlBuilder {
    //QUESTION : I remember that utility class should have private constructor to prevent instantiation
    private YeogiUrlBuilder() {
    }

    private static final String BASE_URL = "https://www.yeogi.com/domestic-accommodations/%s?checkIn=%s&checkOut=%s";

    public static String build(CrawlingRequest request) {
        return String.format(BASE_URL, request.getAccommodationId(), request.getCheckIn().toString(),
                request.getCheckOut().toString());
    }
}
