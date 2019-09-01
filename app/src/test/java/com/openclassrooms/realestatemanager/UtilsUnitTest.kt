package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.Utils.Utils
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class UtilsUnitTest {

    @Test
    fun currentDateInCorrectFormat(){
        val currentDate = Calendar.getInstance()
        val simpleFormat = SimpleDateFormat("dd/MM/yyyy")
        Assert.assertEquals(simpleFormat.format(currentDate.time), Utils.getTodayDate())
    }

    @Test
    fun dollarsToEuros(){
        val dollarsValue = 50000
        val conversion = 0.912
        Assert.assertEquals((dollarsValue * conversion).roundToInt(), Utils.convertDollarToEuro(dollarsValue))
    }

    @Test
    fun eurosToDollars(){
        val eurosValue = 50000
        val conversion = 1.10
        Assert.assertEquals((eurosValue * conversion).roundToInt(), Utils.convertEuroToDollar(eurosValue))
    }
}