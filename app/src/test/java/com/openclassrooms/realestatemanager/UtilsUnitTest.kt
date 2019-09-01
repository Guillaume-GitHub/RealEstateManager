package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.Utils.Utils
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class UtilsUnitTest {

    @Test
    fun currentDateInCorrectFormat(){
        val currentDate = Calendar.getInstance()
        val simpleFormat = SimpleDateFormat("dd/MM/yyyy")
        Assert.assertEquals(simpleFormat.format(currentDate.time), Utils.getTodayDate())
    }
}