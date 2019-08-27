package com.openclassrooms.realestatemanager.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Components(
        @SerializedName("long_name")
        @Expose
        var longName: String,
        @SerializedName("short_name")
        @Expose
        var shortName String,
        @SerializedName("types")
        @Expose
        private List<String> types = null
)
