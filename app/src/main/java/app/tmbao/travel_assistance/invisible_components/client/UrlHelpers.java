package app.tmbao.travel_assistance.invisible_components.client;

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
}
