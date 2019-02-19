package in.akshay.newsbuddy.api;

import in.akshay.newsbuddy.model.newsmodel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetroInterface {
    @GET("top-headlines")
    Call<newsmodel> getNews(

            @Query("country") String country,
            @Query("apiKey") String apiKey

    );


    @GET("everything")
    Call<newsmodel> getNewsSearch(

            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey

    );
}
