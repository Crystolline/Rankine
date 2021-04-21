package edu.uc.group.rankine

import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.dto.RankedObjectSet
import org.junit.Test
import kotlin.random.Random

class RankingSetTest {
    lateinit var workingObjectSet: ObjectSet
    lateinit var workingRankedSet: RankedObjectSet
    var loopStop = 500000000

    @Test
    fun rankRankineOverKelvin_RankineOverKelvin() {
        for (i in 1..500) {
            givenSetOfTemperatureSystems()
            whenRankRankineOverKelvinAndRestRandom()
            thenRankineOverKelvin()
        }
    }

    @Test
    fun rankRankineOverKelvinOverCelsiusOverFahrenheit_RankineOverKelvinOverCelsiusOverFahrenheit() {
        givenSetOfTemperatureSystems()
        whenRankRankineOverKelvinOverCelsiusOverFahrenheit()
        thenRankineOverKelvinOverCelsiusOverFahrenheit()
    }

    @Test
    fun rankRandomNumbers_NumbersInOrder() {
        for (i in 1..500) {
            givenSetOfRandomNumbers()
            whenRankNumbers()
            thenNumbersInOrder()
        }
    }

    private fun givenSetOfTemperatureSystems() {
        workingObjectSet = ObjectSet()
        with(workingObjectSet)
        {
            addElement(ElementObject("Fahrenheit"))
            addElement(ElementObject("Celsius"))
            addElement(ElementObject("Kelvin"))
            addElement(ElementObject("Rankine"))
        }
        workingRankedSet = RankedObjectSet()
        workingRankedSet.set = workingObjectSet
        workingRankedSet.startRanking()
    }

    private fun whenRankRankineOverKelvinAndRestRandom() {
        var i = 0
        while(workingRankedSet.isRanking() && i < loopStop) {
            if (workingRankedSet.leftElement.element == "Rankine" && workingRankedSet.rightElement.element == "Kelvin") {
                workingRankedSet.sortStep(false)
            } else if (workingRankedSet.leftElement.element == "Kelvin" && workingRankedSet.rightElement.element == "Rankine") {
                workingRankedSet.sortStep(true)
            } else {
                workingRankedSet.sortStep(Random.nextBoolean())
            }
            i++
        }
    }

    private fun thenRankineOverKelvin() {
        var seenRankine = false
        var seenKelvin = false
        assert(workingRankedSet.isRanked())
        workingRankedSet.getCleanRankedElements().forEach {
            if (it.element == "Rankine")
                seenRankine = true
            else if (it.element == "Kelvin") {
                assert(seenRankine)
                seenKelvin = true
            }
        }
        assert(seenKelvin)
    }

    private fun whenRankRankineOverKelvinOverCelsiusOverFahrenheit() {
        var i = 0
        while(workingRankedSet.isRanking() && i < loopStop) {
            when {
                workingRankedSet.leftElement.element == "Rankine" -> {
                    workingRankedSet.sortStep(false)
                }
                workingRankedSet.rightElement.element == "Rankine" -> {
                    workingRankedSet.sortStep(true)
                }
                workingRankedSet.leftElement.element == "Kelvin" -> {
                    workingRankedSet.sortStep(false)
                }
                workingRankedSet.rightElement.element == "Kelvin" -> {
                    workingRankedSet.sortStep(true)
                }
                workingRankedSet.leftElement.element == "Celsius" -> {
                    workingRankedSet.sortStep(false)
                }
                else -> {
                    workingRankedSet.sortStep(true)
                }
            }
            i++
        }
    }

    private fun thenRankineOverKelvinOverCelsiusOverFahrenheit() {
        var seenRankine = false
        var seenKelvin = false
        var seenCelsius = false
        var seenFahrenheit = false
        assert(workingRankedSet.isRanked())
        workingRankedSet.getCleanRankedElements().forEach {
            when (it.element) {
                "Rankine" -> seenRankine = true
                "Kelvin" -> {
                    assert(seenRankine)
                    seenKelvin = true
                }
                "Celsius" -> {
                    assert(seenKelvin)
                    seenCelsius = true
                }
                else -> {
                    assert(seenCelsius)
                    assert(it.element == "Fahrenheit")
                    seenFahrenheit = true
                }
            }
        }
        assert(seenFahrenheit)
    }

    private fun givenSetOfRandomNumbers() {
        workingObjectSet = ObjectSet()
        for (i in 1..5000) {
            workingObjectSet.addElement(ElementObject(Random.nextInt(-2000000000, 2000000000).toString()))
        }
        workingRankedSet = RankedObjectSet()
        workingRankedSet.set = workingObjectSet
        workingRankedSet.startRanking()
    }

    private fun whenRankNumbers() {
        var i = 0
        while(workingRankedSet.isRanking() && i < loopStop) {
            workingRankedSet.sortStep(workingRankedSet.rightElement.element.toInt() > workingRankedSet.leftElement.element.toInt())
            i++
        }
    }

    private fun thenNumbersInOrder() {
        assert(workingRankedSet.isRanked())
        for (i in 1 until workingRankedSet.getCleanRankedElements().size) {
            assert(workingRankedSet.getCleanRankedElements()[i].element.toInt() <= workingRankedSet.getCleanRankedElements()[i - 1].element.toInt())
        }
    }
}