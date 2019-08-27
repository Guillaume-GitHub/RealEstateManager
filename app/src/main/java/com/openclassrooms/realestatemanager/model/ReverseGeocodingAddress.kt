package com.openclassrooms.realestatemanager.model


import com.google.android.libraries.places.api.internal.impl.net.pablo.PlaceResult.Geometry
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReverseGeocodingAddress(

        @SerializedName("address_components")
        @Expose
        var addressComponents: List<Component>? = null,
        @SerializedName("formatted_address")
        @Expose
        var formattedAddress: String? = null,
        @SerializedName("geometry")
        @Expose
        var geometry: Geometry? = null,
        @SerializedName("place_id")
        @Expose
        var placeId: String? = null,
        @SerializedName("types")
        @Expose
        var types: List<String>? = null,
        @SerializedName("postcode_localities")
        @Expose
        var postcodeLocalities: List<String>? = null)

