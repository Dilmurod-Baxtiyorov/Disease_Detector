package com.example.diseasedetector

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

}

fun main(){
    val phoneNumber = "1234Dd11"
    if (phoneNumber.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$"))){
        println(true)
    }

    val map = mutableMapOf("s" to "yes", "h" to "x")
    println(map.toList()[0].first)
}