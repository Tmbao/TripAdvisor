package app.tmbao.travel_assistance.invisible_components.storage;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.tmbao.travel_assistance.invisible_components.client.ClientHelpers;
import app.tmbao.travel_assistance.invisible_components.models.Landscape;

/**
 * Created by tmbao on 8/25/2015.
 */
public class LandscapeStorage {
    private static LandscapeStorage ourInstance = new LandscapeStorage();

    public static LandscapeStorage getInstance() {
        return ourInstance;
    }

    private List<Landscape> landscapes;

    private LandscapeStorage() {
        reloadLandscapes();
    }

    private void reloadLandscapes() {
        landscapes = new ArrayList<>();
        try {
            landscapes = ClientHelpers.downloadLandscapes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Landscape> getLandscapes() {
        return landscapes;
    }
}
