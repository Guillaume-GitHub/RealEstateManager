package com.openclassrooms.realestatemanager.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    companion object {
        private const val GEOCODING_SERVICE_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"

        fun getGeocodingService(): GeocodingService {
            val retrofit :Retrofit = Retrofit.Builder().baseUrl(GEOCODING_SERVICE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(GeocodingService::class.java)
        }
    }
}