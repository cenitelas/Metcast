package kz.education.metcast.data.api

import io.reactivex.Observable
import kz.education.metcast.data.api.models.CityDetail
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestInterface {

    @GET("data/2.5/forecast")
    fun getCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String
    ): Observable<CityDetail>

    @GET("/data/2.5/forecast")
    fun getCityById(
        @Query("id") city: String,
        @Query("appid") apiKey: String,
        @Query("lang") lang: String
    ): Observable<CityDetail>
}