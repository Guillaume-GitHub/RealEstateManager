package com.openclassrooms.realestatemanager


import androidx.test.InstrumentationRegistry
import com.openclassrooms.realestatemanager.Utils.Utils
import org.junit.Assert.*
import org.junit.Test

class UtilsInstrumentedTest {

    private val context = InstrumentationRegistry.getContext()

    @Test
    fun internetConnection_Available_ReturnTrue(){
        assertTrue(Utils.isInternetAvailable(context))
    }
}