package app.tmbao.travel_assistance.invisible_components.models;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.tmbao.travel_assistance.invisible_components.utilities.MediaHelpers;

/**
 * Created by tmbao on 8/24/2015.
 */
public class Landscape {
    private int id;
    private String name;
    private String code;
    private String description;
    private double rating;
    private List<LandscapeResource> resources;
    private LatLng location;

    private List<LandscapeResource> imageResources;
    private Random random;

    public Landscape(int id, String name, String code, String description, double rating, List<LandscapeResource> resources, LatLng location) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.rating = rating;
        this.resources = resources;
        this.location = location;

        // Cached images
        this.imageResources = new ArrayList<>();
        for (int index = 0; index < resources.size(); index++)
            if (resources.get(index).getType() == LandscapeResource.ResourceType.IMAGE)
                imageResources.add(resources.get(index));
        random = new Random();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public List<LandscapeResource> getResources() {
        return resources;
    }

    public LatLng getLocation() {
        return location;
    }

    public List<LandscapeResource> getImageResources() {
        return imageResources;
    }

    public Bitmap getRepresentativePhoto() throws IOException, NoSuchAlgorithmException {
        if (imageResources.size() == 0)
            return null;
        else {
            int index = random.nextInt(imageResources.size());
            return MediaHelpers.getLandscapeResourceImage(imageResources.get(index));
        }
    }
}
