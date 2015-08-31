package app.tmbao.travel_assistance.invisible_components.client;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import app.tmbao.travel_assistance.invisible_components.models.Landscape;
import app.tmbao.travel_assistance.invisible_components.models.RetrievedLandscape;
import app.tmbao.travel_assistance.invisible_components.networks.HttpResponse;
import app.tmbao.travel_assistance.invisible_components.networks.Requests;
import app.tmbao.travel_assistance.invisible_components.utilities.JSONHelpers;

/**
 * Created by tmbao on 8/25/2015.
 */
public class ClientHelpers {
    private static int WAIT_TIME = 1000;

    public static List<Landscape> downloadLandscapes() throws IOException, JSONException {
        HttpResponse response = Requests.get(UrlHelpers.getLandscapesUrl(), null);
        if (response.getCode() == 200)
            return JSONHelpers.createLandscapes(response.getContent());
        else
            return null;
    }

    public static Landscape downloadLandscape(int landscapeId) throws IOException, JSONException {
        HttpResponse response = Requests.get(UrlHelpers.getLandscapeUrl(landscapeId), null);
        if (response.getCode() == 200)
            return JSONHelpers.createLandscape(response.getContent());
        else
            return null;
    }

    public static String recognizeLandscape(String fileName) throws IOException, InterruptedException, JSONException {
        List<Requests.FormData> parts = new ArrayList<>();
        parts.add(new Requests.FormData("content", Requests.FormData.Type.FILE, fileName));

        HttpResponse response = Requests.post(UrlHelpers.getRequestUrl(), parts);
        if (response.getCode() == 200 || response.getCode() == 201) {
            int responseId = JSONHelpers.getRetrievedLandscapeId(response.getContent());
            while (true) {
                response = Requests.get(UrlHelpers.getResponseLandscapeUrl(responseId), null);
                if (response.getCode() == 200 || response.getCode() == 201) {
                    RetrievedLandscape retrievedLandscape = JSONHelpers.createRetrievedLandscape(response.getContent());
                    if (retrievedLandscape.getStatus() == RetrievedLandscape.Status.COMPLETED) {
//                        Use KNN with K = sqrt(size(ranked_list))
                        int K = (int) Math.sqrt(retrievedLandscape.getContent().size());

                        TreeMap<String, Integer> occurrences = new TreeMap<>();
                        for (int index = 0; index < K; index++) {
                            String key = retrievedLandscape.getContent().get(index);
                            if (!occurrences.containsKey(key))
                                occurrences.put(key, 0);
                            occurrences.put(key, occurrences.get(key) + 1);
                        }

                        Map.Entry<String, Integer> result = null;
                        for (Map.Entry<String, Integer> entry : occurrences.entrySet())
                            if (result == null || result.getValue() < entry.getValue())
                                result = entry;

                        return result.getKey();
                    }
                } else
//                    Connection problem
                    return null;
                Thread.sleep(WAIT_TIME);
            }
        } else
//            Connection problem / File not found
            return null;
    }
}
