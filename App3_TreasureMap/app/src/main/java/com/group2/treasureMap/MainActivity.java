package com.group2.treasureMap;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity{
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private FusedLocationProviderClient fusedLocationClient;
    private MyLocationNewOverlay mLocationOverlay;

    private String lat;
    private String lon;
    private GeoPoint point = null ;

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //handle permissions first, before map is created. not depicted here
        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMaxZoomLevel(20.0);

        map.setMultiTouchControls(true);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);


        IMapController mapController = map.getController();

        GeoPoint startPoint = new GeoPoint(47.1344, 7.2437);
        mapController.setZoom(17.0);
        mapController.setCenter(startPoint);

        requestPermissionsIfNecessary(new String[]{
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton getPosButton = findViewById(R.id.fab);
        getPosButton.setOnClickListener(v -> {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                System.out.println(location.toString());
                                point = new GeoPoint(location.getLatitude(), location.getLongitude());
                                mapController.setCenter(point);
                                mapController.setZoom(18.0);
                                mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),map);
                                mLocationOverlay.enableMyLocation();
                                map.getOverlays().add(mLocationOverlay);
                            }
                        }
                    });
        });
        HashSet<GeoPoint> points = new HashSet<>();
        FloatingActionButton setMarkerButton = findViewById(R.id.addMarkerButton);
        setMarkerButton.setOnClickListener(v -> {
            Marker startMarker = new Marker(map);
            startMarker.setPosition(point);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            int addFlag = 0;
            for(Overlay ol : map.getOverlays()){
                if (ol instanceof Marker
                        && ((Marker) ol).getPosition().getLongitude() == startMarker.getPosition().getLongitude()
                        && ((Marker) ol).getPosition().getLatitude() == startMarker.getPosition().getLatitude()){
                    addFlag = 1;
                    break;
                }
            }
            if (addFlag == 0){
                map.getOverlays().add(startMarker);
                points.add(point);
                mapController.setCenter(point);
                mapController.setZoom(19.5);
                lat = String.valueOf(point.getLatitudeE6());
                lon = String.valueOf(point.getLongitudeE6());
                this.writeToSharedPref(lat,lon);
                startMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        map.getOverlays().remove(startMarker);
                        points.removeIf(point -> point.getLatitude() == marker.getPosition().getLatitude() &&
                                point.getLongitude() == marker.getPosition().getLongitude());
                        Toast.makeText(MainActivity.this, "Deleting", Toast.LENGTH_LONG).show();
                        loadData();
                        return true;
                    }
                });
                Toast.makeText(MainActivity.this, "Click on marker to delete", Toast.LENGTH_LONG).show();
            }
        });
        Button button = findViewById(R.id.sendLogButton);
        button.setOnClickListener(v -> {
            try {
                log(points);
            } catch (ActivityNotFoundException e){
            }
        });

    }

    public void writeToSharedPref(String lat, String lon){
        SharedPreferences sharedPref = getSharedPreferences("GeoLocations", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lat", lat);
        editor.putString("lon", lon);
        editor.apply();
    }

    //TO DO
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("GeoLocations",Context.MODE_PRIVATE);
        String res = sharedPreferences.getString("lat","");
    }


    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    /**
     * Crate a Logbook Format as JSON and send via LogBuch App Activity
     * @param points
     */
    private void log(HashSet<GeoPoint> points) {
        Intent intent = new Intent("ch.blockwoche.intent.LOG");
        // format depends on app, see logbook format guideline
        ArrayList<JSONObject> pointsAsArray = new ArrayList<>();
        JSONObject log = new JSONObject();
        for (GeoPoint geoPoint : points) {
            JSONObject point = new JSONObject();
            try {
                point.put("lat",geoPoint.getLatitudeE6());
                point.put("lon",geoPoint.getLongitudeE6());
            }catch (JSONException j){

            }
            pointsAsArray.add(point);
        }
        try {
            log.put("task", "Treasuremap");
            log.put("points", new JSONArray(pointsAsArray));
            } catch (JSONException j){ }
        intent.putExtra("ch.blockwoche.logmessage", log.toString());
        try { startActivity(intent);
        } catch (ActivityNotFoundException e){ }
    }
}