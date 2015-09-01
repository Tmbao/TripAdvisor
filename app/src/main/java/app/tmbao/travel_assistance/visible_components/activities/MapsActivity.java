package app.tmbao.travel_assistance.visible_components.activities;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import app.tmbao.travel_assistance.R;
import app.tmbao.travel_assistance.invisible_components.client.UrlHelpers;
import app.tmbao.travel_assistance.invisible_components.models.GoogleDirections;
import app.tmbao.travel_assistance.invisible_components.models.Landscape;
import app.tmbao.travel_assistance.invisible_components.networks.HttpResponse;
import app.tmbao.travel_assistance.invisible_components.networks.Requests;
import app.tmbao.travel_assistance.invisible_components.storage.LandscapeStorage;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Landscape landscape;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        initializeLandscape();
        initializeLocation();
    }

    private void initializeLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null)
            onLocationChanged(location);
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }

    private void initializeLandscape() {
        int landscapeId = getIntent().getIntExtra("landscapeID", 0);
        landscape = LandscapeStorage.getInstance().getLandscapes().get(landscapeId);
    }

    private void showDirections(LatLng currentLocation, LatLng destinationLocation) {
        try {
            HttpResponse response = Requests.get(UrlHelpers.getDirectionsUrl(currentLocation, destinationLocation), null);
            if (response.getCode() == 200) {
                GoogleDirections directions = new GoogleDirections(response.getContent());
                drawDirections(directions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void drawDirections(GoogleDirections directions) {
        mMap.clear();
        for (List<LatLng> route : directions.getRoutes())
            mMap.addPolyline(new PolylineOptions().addAll(route).width(8).color(Color.RED));
        mMap.addMarker(new MarkerOptions().position(landscape.getLocation()));
        mMap.addMarker(new MarkerOptions().position(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        currentLocation = new LatLng(lat, lng);
        showDirections(currentLocation, landscape.getLocation());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
