package com.openclassrooms.realestatemanager.Utils

import com.google.android.libraries.places.api.model.AddressComponent

class AddressCompenentsHelper(private val addressComponents: List<AddressComponent>?) {

    private var postalCode: String? = null
    private var streetNumber: String? = null
    private var route: String? = null
    private var city: String? = null
    private var country: String? = null
    private var area: String? = null

    fun parseStreetNumber(): String?{
        this.addressComponents?.forEach { addressComponent ->
            if (addressComponent.types.contains("street_number")) { this.streetNumber = addressComponent.name }
        }
        return this.streetNumber
    }

    fun parseRoute(): String? {
        this.addressComponents?.forEach { addressComponent ->
            if (addressComponent.types.contains("route")) this.route = addressComponent.name
        }
        return this.route
    }

    fun parsePostalCode(): String? {
        this.addressComponents?.forEach { addressComponent ->
            if (addressComponent.types.contains("postal_code")) this.postalCode = addressComponent.name
        }
        return this.postalCode
    }

    fun parseCity(): String? {
        this.addressComponents?.forEach { addressComponent ->
            if (addressComponent.types.contains("locality") || addressComponent.types.contains("sublocality")) this.city = addressComponent.name
        }
        return this.city
    }

    fun parseArea(): String? {
        this.addressComponents?.forEach { addressComponent ->
            if (addressComponent.types.contains("administrative_area_level_3")) this.area = addressComponent.name
        }
        return this.area
    }

    fun parseCountry(): String? {
        this.addressComponents?.forEach { addressComponent ->
            if (addressComponent.types.contains("country")) this.country = addressComponent.name
        }
        return this.country
    }
}
