package in.akshay.newsbuddy.api;

import in.akshay.newsbuddy.model.newsmodel;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetroInterface {
    @GET("top-headlines")
    Observable<newsmodel> getNews(

            @Query("country") String country,
            @Query("category") String category,
            @Query("pageSize") String pagesize,
            @Query("apiKey") String apiKey

    );


    @GET("everything")
    Observable  <newsmodel> getNewsSearch(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("pageSize") String pagesize,
            @Query("apiKey") String apiKey

    );


}
