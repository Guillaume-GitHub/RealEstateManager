package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.ReverseGeocodingAddress
import com.openclassrooms.realestatemanager.model.ReverseGeocodingResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap
import kotlin.collections.ArrayList

interface GeocodingService {
    @GET("json?&key=AIzaSyDAozKdpeVeJy83RUuYdHE7RXTZP5gAPZQ")
    fun getAddressFromLatLng(@QueryMap position: Map<String,String>): Single<ReverseGeocodingResult>
}