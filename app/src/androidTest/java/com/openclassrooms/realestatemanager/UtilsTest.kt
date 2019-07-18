package com.openclassrooms.realestatemanager


import androidx.test.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test

class UtilsTest {

    private val context = InstrumentationRegistry.getContext()

    @Test
    fun internetConnection_Available_ReturnTrue(){
        assertTrue(Utils.isInternetAvailable(context))
    }
}