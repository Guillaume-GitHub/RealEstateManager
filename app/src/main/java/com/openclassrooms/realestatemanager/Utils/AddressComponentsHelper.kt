package com.openclassrooms.realestatemanager.Utils

import com.google.android.libraries.places.api.model.Place

class AddressComponentsHelper(private val place: Place) {

    private var postalCode: String? = null
    private var streetNumber: String? = null
    private var city: String? = null
    private var country: String? = null

    fun parseStreetNumber(): String?{
        this.place.addressComponents?.asList()?.forEach { addressComponent ->
            if (addressComponent.types.contains("street_number")) this.streetNumber = addressComponent.name
        }
        return this.streetNumber
    }

    fun parsePostalCode(): String?{
        this.place.addressComponents?.asList()?.forEach { addressComponent ->
            if (addressComponent.types.contains("street_number")) this.postalCode = addressComponent.name
        }
        return this.postalCode
    }

    fun parseCity(): String?{
        this.place.addressComponents?.asList()?.forEach { addressComponent ->
            if (addressComponent.types.contains("locality")) this.city = addressComponent.name
        }
        return this.city
    }

    fun parseCountry(): String?{
        this.place.addressComponents?.asList()?.forEach { addressComponent ->
            if (addressComponent.types.contains("country")) this.country = addressComponent.name
        }
        return this.country
    }
}