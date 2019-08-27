package com.openclassrooms.realestatemanager.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Component(
    @SerializedName("long_name")
    @Expose
    var longName: String? = null,
    @SerializedName("short_name")
    @Expose
    var shortName: String? = null,
    @SerializedName("types")
    @Expose
    var types: List<String>? = null)
