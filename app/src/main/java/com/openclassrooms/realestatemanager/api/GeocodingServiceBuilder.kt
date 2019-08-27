package com.openclassrooms.realestatemanager.api

import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.model.ReverseGeocodingResult
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GeocodingServiceBuilder {
    companion object {
        private const val GEOCODING_SERVICE_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/"

       private fun getGeocodingService(): GeocodingService {
            val retrofit :Retrofit = Retrofit.Builder().baseUrl(GEOCODING_SERVICE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(GeocodingService::class.java)
        }

        fun getAddressFromLatLng(position: LatLng): Single<ReverseGeocodingResult> {
            val params = HashMap<String,String>()
            params["latlng"] = "${position.latitude},${position.longitude}"
           return this.getGeocodingService().getAddressFromLatLng(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

}