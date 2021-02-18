package edu.uc.group.rankine

import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import org.junit.Test

import org.junit.Assert.*


class ObjectSetCreationTest
{
    lateinit var workingObjectSet:ObjectSet

    @Test
    fun addRankineElement_containsRankineElement()
    {
        givenEmptySet()
        whenAddRankineElement()
        thenContainsRankine()
        thenNotRankable()
    }

    @Test
    fun addRankineAndKelvinElements_containsRankineAndKelvinElements()
    {
        givenEmptySet()
        whenAddRankineAndKelvinElements()
        thenContainsRankineAndKelvin()
        thenIsRankable()
    }

    @Test
    fun addFahrenheitElementToUnrankedSet_containsFahrenheitElement()
    {
        givenRankineAndKelvinInUnrankedSet()
        whenAddFahrenheitElement()
        thenContainsFahrenheit()
        thenIsRankable()
    }

    fun addCelsiusElementToRankedSet_containsCelsiusElement()
    {
        givenRankineKelvinAndFahrenheitInRankedSet()
        whenAddCelsiusElement()
        thenContainsCelsius()
        thenRankingInProgress()
    }

    private fun givenEmptySet() {
        workingObjectSet = ObjectSet()
    }

    private fun whenAddRankineElement() {
        workingObjectSet.addElement(ElementObject("Rankine"))
    }

    private fun thenContainsRankine() {
        var containsRankine = false
        workingObjectSet.elements.forEach {
            if(it.name == "Rankine") {
                containsRankine = true
            }
        }
        assert(containsRankine)
    }

    private fun thenNotRankable() {
        assert(!workingObjectSet.canBeRanked())
    }

    private fun whenAddRankineAndKelvinElements() {
        workingObjectSet.addElement(ElementObject("Rankine"))
        workingObjectSet.addElement(ElementObject("Kelvin"))
    }

    private fun thenContainsRankineAndKelvin() {
        var containsRankine = false
        var containsKelvin = false
        workingObjectSet.elements.forEach {
            if(it.name == "Rankine") {
                containsRankine = true
            }
            if(it.name == "Kelvin") {
                containsKelvin = true
            }
        }
        assert(containsRankine && containsKelvin)
    }

    private fun thenIsRankable(){
        assert(workingObjectSet.canBeRanked())
    }

    private fun givenRankineAndKelvinInUnrankedSet() {
        workingObjectSet = ObjectSet(arrayListOf(ElementObject("Rankine"), ElementObject("Kelvin")))
    }

    private fun whenAddFahrenheitElement() {
        workingObjectSet.addElement(ElementObject("Fahrenheit"))
    }

    private fun thenContainsFahrenheit() {
        var containsFahrenheit = false
        workingObjectSet.elements.forEach {
            if(it.name == "Fahrenheit") {
                containsFahrenheit = true
            }
        }
        assert(containsFahrenheit)
    }

    private fun givenRankineKelvinAndFahrenheitInRankedSet() {
        workingObjectSet = ObjectSet(arrayListOf(ElementObject("Rankine"), ElementObject("Kelvin"), ElementObject("Fahrenheit")), ranked = true)
    }

    private fun whenAddCelsiusElement() {
        workingObjectSet.addElement(ElementObject("Celsius"))
    }

    private fun thenContainsCelsius() {
        var containsCelsius = false
        workingObjectSet.elements.forEach {
            if(it.name == "Celsius") {
                containsCelsius = true
            }
        }
        assert(containsCelsius)
    }

    private fun thenRankingInProgress() {
        assert(workingObjectSet.isBeingRanked())
    }
}