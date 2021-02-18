package edu.uc.group.rankine

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

    }

    private fun thenContainsRankine() {
        assert(false)
    }

    private fun thenNotRankable() {
        assert(false)
    }

    private fun whenAddRankineAndKelvinElements() {

    }

    private fun thenContainsRankineAndKelvin() {
        assert(false)
    }

    private fun thenIsRankable(){
        assert(false)
    }

    private fun givenRankineAndKelvinInUnrankedSet() {

    }

    private fun whenAddFahrenheitElement() {

    }

    private fun thenContainsFahrenheit() {
        assert(false)
    }

    private fun givenRankineKelvinAndFahrenheitInRankedSet() {

    }

    private fun whenAddCelsiusElement() {

    }

    private fun thenContainsCelsius() {
        assert(false)
    }

    private fun thenRankingInProgress() {
        assert(false)
    }
}