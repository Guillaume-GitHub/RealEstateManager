package com.openclassrooms.realestatemanager.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReverseGeocodingResult(
        @SerializedName("results")
        @Expose
        var address: List<ReverseGeocodingAddress>,
        @SerializedName("status")
        @Expose
        var status: String

)
