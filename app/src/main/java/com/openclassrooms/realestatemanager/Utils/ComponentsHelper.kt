package com.openclassrooms.realestatemanager.Utils

import com.openclassrooms.realestatemanager.model.Component
import com.openclassrooms.realestatemanager.model.ParsedAddressComponent

class ComponentsHelper(private val addressComponents: List<Component>) {

    private var postalCode: String? = null
    private var streetNumber: String? = null
    private var route: String? = null
    private var city: String? = null
    private var country: String? = null
    private var area: String? = null
    private var typeList = ArrayList<String>()

    fun parseStreetNumber(): String?{
        this.addressComponents.forEach { addressComponent ->
            val types = addressComponent.types
            if (types != null) {
                if (types.contains("street_number")) { this.streetNumber = addressComponent.longName }
            }
        }
        return this.streetNumber
    }

    fun parseRoute(): String?{
        this.addressComponents.forEach { addressComponent ->
            val types = addressComponent.types
            if (types != null) {
                if (types.contains("route")){ this.route = addressComponent.longName }
            }
        }
        return this.route
    }

    fun parsePostalCode(): String?{
        this.addressComponents.forEach { addressComponent ->
            val types = addressComponent.types
            if (types != null) {
                if (types.contains("postal_code")) this.postalCode = addressComponent.longName
            }
        }
        return this.postalCode
    }

    fun parseCity(): String?{
        this.addressComponents.forEach { addressComponent ->
            val types = addressComponent.types
            if (types != null) {
                if (types.contains("locality") || types.contains("sublocality")) this.city = addressComponent.longName
            }
        }
        return this.city
    }

    fun parseArea(): String?{
        this.addressComponents.forEach { addressComponent ->
            val types = addressComponent.types
            if (types != null) {
                if (types.contains("administrative_area_level_3")) this.area = addressComponent.longName
            }
        }
        return this.area
    }

    fun parseCountry(): String?{
        this.addressComponents.forEach { addressComponent ->
            val types = addressComponent.types
            if (types != null) {
                if (types.contains("country")) this.country = addressComponent.longName
            }
        }
        return this.country
    }

    fun getAllTypes():ArrayList<String>? {
        this.addressComponents.forEach { addressComponent ->
            this.typeList.add("type :${addressComponent.types}")
        }
       return if (this.typeList.size > 0) this.typeList else null
    }

    private fun parseAll(){
        this.parseStreetNumber()
        this.parseRoute()
        this.parsePostalCode()
        this.parseCity()
        this.parseCountry()
    }

    fun getFormattedAddress(): String {
        this.parseAll()
        var formattedAddress = ""
        if (this.streetNumber != null) formattedAddress += this.streetNumber
        if (this.route != null) formattedAddress += " ${this.route}"
        if (this.postalCode != null) formattedAddress += ", ${this.postalCode}"
        if (this.city != null) formattedAddress += " ${this.city}"
        if (this.country != null) formattedAddress += ", ${this.country}"

        return  formattedAddress
    }

    fun getParsedAddressComponent(): ParsedAddressComponent{
        this.parseAll()
        return ParsedAddressComponent(streetNumber,route,postalCode,city,country)
    }

}