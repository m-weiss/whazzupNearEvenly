package com.example.grobi.whazzupnearevenly;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by grobi on 25.04.17.
 */

public interface FoursquareAPI {

    @GET("/v2/venues/search?v=20170425&radius=300")
    Call<FoursquareData> loadData(@Query("client_id") String id, @Query("client_secret") String sec, @Query("ll") String ll);

}
