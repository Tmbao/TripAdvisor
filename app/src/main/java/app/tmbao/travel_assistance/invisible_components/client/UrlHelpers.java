package app.tmbao.travel_assistance.invisible_components.client;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tmbao on 8/25/2015.
 */
public class UrlHelpers {
    private static String protocol = "http";
    private static String baseUrl = "112.78.11.178";

    public static String getRequestUrl() {
        return String.format("%s://%s/retrieve/requests", protocol, baseUrl);
    }

    public static String getResponseLandscapeUrl(int responseId) {
        return String.format("%s://%s/retrieve/response/landscape/%d", protocol, baseUrl, responseId);
    }

    public static String getLandscapesUrl() {
        return String.format("%s://%s/landscapes/", protocol, baseUrl);
    }

    public static String getLandscapeUrl(int landscapeId) {
        return String.format("%s://%s/landscapes/%d", protocol, baseUrl, landscapeId);
    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest) {
        return String.format("https://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f",
                origin.latitude, origin.longitude,
                dest.latitude, dest.longitude);
    }
}
