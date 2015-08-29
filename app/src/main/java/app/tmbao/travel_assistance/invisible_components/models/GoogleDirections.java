package app.tmbao.travel_assistance.invisible_components.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmbao on 8/25/2015.
 */
public class GoogleDirections {
    private static final String ROUTES_TAG = "routes";
    private static final String LEGS_TAG = "legs";
    private static final String STEPS_TAG = "steps";
    private static final String POLYLINE_TAG = "polyline";
    private static final String POINTS_TAG = "points";

    public List<List<LatLng>> getRoutes() {
        return routes;
    }

    private List<List<LatLng>> routes;

    public GoogleDirections(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public GoogleDirections(JSONObject jsonObject) throws JSONException {
        JSONArray jsonRoutes = jsonObject.getJSONArray(ROUTES_TAG);

        routes = new ArrayList<>();
        for (int routeIndex = 0; routeIndex < jsonRoutes.length(); routeIndex++) {
            JSONArray jsonLegs = jsonRoutes.getJSONObject(routeIndex).getJSONArray(LEGS_TAG);

            List<LatLng> path = new ArrayList<>();
            for (int legIndex = 0; legIndex < jsonLegs.length(); legIndex++) {
                JSONArray jsonSteps = jsonLegs.getJSONObject(legIndex).getJSONArray(STEPS_TAG);

                for (int stepIndex = 0; stepIndex < jsonSteps.length(); stepIndex++) {
                    String polyline =  jsonSteps.getJSONObject(stepIndex).getJSONObject(POLYLINE_TAG).getString(POINTS_TAG);

                    path.addAll(decodePoly(polyline));
                }
            }
            routes.add(path);
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        for (int index = 0, lat = 0, lng = 0; index < encoded.length(); ) {
            int bit = 0, shift = 0, result = 0;
            do {
                bit = encoded.charAt(index++) - 63;
                result |= (bit & 31) << shift;
                shift += 5;
            } while (bit >= 32);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            shift = 0; result = 0;
            do {
                bit = encoded.charAt(index++) - 63;
                result |= (bit & 31) << shift;
                shift += 5;
            } while (bit >= 32);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            poly.add(new LatLng(lat / 1e5, lng / 1e5));
        }

        return poly;
    }
}
