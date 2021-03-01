package edu.uc.group.rankine

import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import org.junit.Test


class ObjectSetCreationTest
{
    lateinit var workingObjectSet:ObjectSet

    @Test
    fun addRankineElement_containsRankineElement()
    {
        givenEmptySet()
        whenAddRankineElement()
        thenContainsRankine()
    }

    @Test
    fun addRankineAndKelvinElements_containsRankineAndKelvinElements()
    {
        givenEmptySet()
        whenAddRankineAndKelvinElements()
        thenContainsRankineAndKelvin()
    }

    @Test
    fun addFahrenheitElementToUnrankedSet_containsFahrenheitElement()
    {
        givenRankineAndKelvinInUnrankedSet()
        whenAddFahrenheitElement()
        thenContainsFahrenheit()

    }

    @Test
    fun addCelsiusElementToRankedSet_containsCelsiusElement()
    {
        givenRankineKelvinAndFahrenheitInRankedSet()
        whenAddCelsiusElement()
        thenContainsCelsius()
    }

    private fun givenEmptySet() {
        workingObjectSet = ObjectSet()
    }

    private fun whenAddRankineElement() {
        workingObjectSet.addElement(ElementObject("Rankine"))
    }

    private fun thenContainsRankine() {
        var containsRankine = false
        if(workingObjectSet.elements.contains(ElementObject("Rankine")))
        {
            containsRankine = true
        }
        assert(containsRankine)
    }


    private fun whenAddRankineAndKelvinElements() {
        workingObjectSet.addElement(ElementObject("Rankine"))
        workingObjectSet.addElement(ElementObject("Kelvin"))
    }

    private fun thenContainsRankineAndKelvin() {
        var containsRankine = false
        var containsKelvin = false

        if(workingObjectSet.elements.contains(ElementObject("Rankine")))
        {
            containsRankine = true
        }
        if(workingObjectSet.elements.contains(ElementObject("Kelvin")))
        {
            containsKelvin = true
        }
        assert(containsRankine && containsKelvin)
    }


    private fun givenRankineAndKelvinInUnrankedSet() {
        workingObjectSet = ObjectSet(arrayListOf<ElementObject>(ElementObject("Rankine"), ElementObject("Kelvin")))
    }

    private fun whenAddFahrenheitElement() {
        workingObjectSet.addElement(ElementObject("Fahrenheit"))
    }

    private fun thenContainsFahrenheit() {
        var containsFahrenheit = false

        if(workingObjectSet.elements.contains(ElementObject("Fahrenheit")))
        {
            containsFahrenheit = true
        }
        assert(containsFahrenheit)
    }

    private fun givenRankineKelvinAndFahrenheitInRankedSet() {
        workingObjectSet = ObjectSet(arrayListOf(ElementObject("Rankine"), ElementObject("Kelvin"), ElementObject("Fahrenheit")))
    }

    private fun whenAddCelsiusElement() {
        workingObjectSet.addElement(ElementObject("Celsius"))
    }

    private fun thenContainsCelsius() {
        var containsCelsius = false

        if(workingObjectSet.elements.contains(ElementObject("Celsius")))
        {
            containsCelsius = true
        }
        assert(containsCelsius)
    }


}