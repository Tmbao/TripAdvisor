package app.tmbao.travel_assistance.invisible_components.utilities;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.tmbao.travel_assistance.invisible_components.models.Landscape;
import app.tmbao.travel_assistance.invisible_components.models.LandscapeResource;
import app.tmbao.travel_assistance.invisible_components.models.RetrievedLandscape;

/**
 * Created by tmbao on 8/25/2015.
 */
public class JSONHelpers {
    public static Landscape createLandscape(String jsonString) throws JSONException {
        return createLandscape(new JSONObject(jsonString));
    }

    public static Landscape createLandscape(JSONObject jsonObject) throws JSONException {
        return new Landscape(
                jsonObject.getInt("id"),
                jsonObject.getString("name"),
                jsonObject.getString("code"),
                jsonObject.getString("description"),
                jsonObject.getDouble("rating"),
                createLandscapeResources(jsonObject.getJSONArray("resources")),
                new LatLng(
                        jsonObject.getDouble("latitude"),
                        jsonObject.getDouble("longitude")
                )
        );
    }

    public static List<Landscape> createLandscapes(String jsonString) throws JSONException {
        return createLandscapes(new JSONArray(jsonString));
    }

    public static List<Landscape> createLandscapes(JSONArray jsonArray) throws JSONException {
        List<Landscape> landscapes = new ArrayList<>();
        for (int index = 0; index < jsonArray.length(); index++)
            landscapes.add(createLandscape(jsonArray.getJSONObject(index)));
        return landscapes;
    }

    public static LandscapeResource createLandscapeResource(String jsonString) throws JSONException {
        return createLandscapeResource(new JSONObject(jsonString));
    }

    public static LandscapeResource createLandscapeResource(JSONObject jsonObject) throws JSONException {
        return new LandscapeResource(
                LandscapeResource.ResourceType.parse(jsonObject.getString("type")),
                jsonObject.getString("content")
        );
    }

    public static List<LandscapeResource> createLandscapeResources(String jsonString) throws JSONException {
        return createLandscapeResources(new JSONArray(jsonString));
    }

    public static List<LandscapeResource> createLandscapeResources(JSONArray jsonArray) throws JSONException {
        List<LandscapeResource> resources = new ArrayList<>();
        for (int index = 0; index < jsonArray.length(); index++)
            resources.add(createLandscapeResource(jsonArray.getJSONObject(index)));
        return resources;
    }

    public static RetrievedLandscape createRetrievedLandscape(String jsonString) throws JSONException {
        return createRetrievedLandscape(new JSONObject(jsonString));
    }

    public static int getRetrievedLandscapeId(String jsonString) throws JSONException {
        return new JSONObject(jsonString).getInt("id");
    }

    public static RetrievedLandscape createRetrievedLandscape(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("id");
        RetrievedLandscape.Status status = RetrievedLandscape.Status.parse(jsonObject.getString("get_status"));

        List<String> content = new ArrayList<>();
        if (status == RetrievedLandscape.Status.COMPLETED) {
            JSONArray jsonArray = jsonObject.getJSONArray("content");
            for (int index = 0; index < jsonArray.length(); index++)
                content.add(jsonArray.getString(index));
        }

        return new RetrievedLandscape(id, status, content);
    }
}
