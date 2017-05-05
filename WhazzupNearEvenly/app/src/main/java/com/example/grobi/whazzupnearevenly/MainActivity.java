package com.example.grobi.whazzupnearevenly;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements LocationListener, Callback<FoursquareData> {

    public final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
    };

    @BindView(R.id.goToEvenlyBtn)
    Button evenlyBtn;
    @BindView(R.id.getUserPosition)
    Button updateLocBtn;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecyclerView.Adapter rvAdapter;

    ArrayAdapter<List<String>> arrayAdapter;

    private LocationManager locationManager;
    private final int LOC_REFRESH_TIME = 100;
    private final int LOC_REFRESH_DIST = 5;

    private static final String clientID = "YNGCEVMKSPKNOTFPWZTKYBVNYW1VMH0Q4J4TDEHCMSFJHHCK";
    private static final String clientSecret = "CRNN0HJSZCQHK3HULCAYWAM5ZLTKHD55FSWWYUOZK5UWTIZ2";
    private static final String evenlyLL = "52.500342,13.425170";

    private Double lat = 0.0;
    private Double lon = 0.0;
    private String latLon;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private FoursquareData foursquareData;

    private ArrayList<FoursquareData> foursquareDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        foursquareDataList = new FoursquareData().createFoursquareDataList();
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvAdapter = new VenueRVAdapter(foursquareDataList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rvAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkAndRequestPermissions();
        else
            setupLocationProvider();

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<List<String>>());
        latLon = evenlyLL;
        doApiCall();

        updateLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserLoc();
            }
        });

        evenlyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latLon = evenlyLL;
                doApiCall();
            }
        });
    }

    private void updateUserLoc() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider;
        provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager.getLastKnownLocation(provider) != null){
            lon = locationManager.getLastKnownLocation(provider).getLongitude();
            lat = locationManager.getLastKnownLocation(provider).getLatitude();
        }
        else {
            locationManager.requestLocationUpdates(provider, LOC_REFRESH_TIME, LOC_REFRESH_DIST, locationListener);
            provider = locationManager.getAllProviders().get(0);
            lon = locationManager.getLastKnownLocation(provider).getLongitude();
            lat = locationManager.getLastKnownLocation(provider).getLatitude();
        }
        latLon = lat.toString() +","+ lon.toString();
        doApiCall();
    }

    public void setupRecyclerView(ArrayList<FoursquareData> venueDatas){
        foursquareData = new FoursquareData();
        foursquareDataList = venueDatas;

        rvAdapter = new VenueRVAdapter(foursquareDataList, this);
        if (recyclerView != null){
            recyclerView.setAdapter(rvAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

    }

    private void doApiCall() {
        Retrofit retrofit = ApiFactory.getApiFactory().getRetrofit();
        FoursquareAPI foursquareAPI = retrofit.create(FoursquareAPI.class);
        Call<FoursquareData> call = foursquareAPI.loadData(clientID, clientSecret, latLon);
        call.enqueue(this);
    }

    private boolean checkAndRequestPermissions() {

        List<String> listNeededPermissions = new ArrayList<>();
        for (String permission:permissions){
            if (ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_DENIED)
                listNeededPermissions.add((permission));
        }
        if(!listNeededPermissions.isEmpty()){
            ActivityCompat.requestPermissions(this, listNeededPermissions.toArray(new String[listNeededPermissions.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS){
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);

            if(grantResults.length > 0){
                for (int i = 0; i<permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if(perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
                    //permissions ok
                    setupLocationProvider();
                }
                else {
                    showReminderDialog("sorry, but these permissions are required", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    checkAndRequestPermissions();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    });
                }
            }
            else {
                Toast.makeText(this, "Please go to settings and enable permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showReminderDialog(String message, DialogInterface.OnClickListener okListener){
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void setupLocationProvider() {
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_REFRESH_TIME, LOC_REFRESH_DIST, this);
            else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOC_REFRESH_TIME, LOC_REFRESH_TIME, this);

            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider;
            provider = locationManager.getBestProvider(criteria, true);

            if (provider != null){
                Location location;
                location = locationManager.getLastKnownLocation(provider);
                if (location != null){
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    latLon = lat+","+lon;
                }
            } else Toast.makeText(this, "no location", Toast.LENGTH_SHORT).show();
        }
        catch (SecurityException e){
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lon = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onResponse(Call<FoursquareData> call, Response<FoursquareData> response) {

        arrayAdapter.clear();

        if (response.body() != null){

            if (response.body().getResponse() != null ){
                ArrayList<FoursquareData> foursquareDataList = new ArrayList<>();

                for (int i = 0; i < response.body().getResponse().getVenues().size(); i++){
                    foursquareData = response.body().getResponse().getVenues().get(i);
                    FoursquareData recent = new FoursquareData();
                    recent.setName(foursquareData.getName());
                    recent.setId(foursquareData.getId());
                    recent.setLocation(foursquareData.getLocation());

                    if (recent.getLocation().getAddress() != null)
                        recent.getLocation().setAddress(foursquareData.getLocation().getAddress());
                    if (recent.getLocation().getLat() != null && recent.getLocation().getLng() != null){
                        recent.getLocation().setLat(recent.getLocation().getLat());
                        recent.getLocation().setLng(recent.getLocation().getLng());
                    }

                    recent.getLocation().setDistance(foursquareData.getLocation().getDistance());

                    foursquareDataList.add(recent);
                }
                setupRecyclerView(foursquareDataList);
            }
            else{
                Toast.makeText(this, "response.body.venues is empty", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "response.body is empty...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<FoursquareData> call, Throwable t) {
        Toast.makeText(this, "Call onFailuere", Toast.LENGTH_SHORT).show();

    }

}
