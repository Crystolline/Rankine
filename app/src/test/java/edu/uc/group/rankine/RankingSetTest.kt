package edu.uc.group.rankine

import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet

class RankingSetTest {
    lateinit var workingObjectSet: ObjectSet

    //Not done with this

    fun rankSet() {
        givenSet()
        whenRankSet()
        thenSetRanked()
    }

    private fun thenSetRanked() {
        TODO("Not yet implemented")
    }

    private fun whenRankSet() {
        TODO("Not yet implemented")
    }


    private fun givenSet() {

        workingObjectSet = ObjectSet()
        with(workingObjectSet)
        {
            addElement(ElementObject("Pikachu"))
            addElement(ElementObject("Charmander"))
            addElement(ElementObject("Bulbasaur"))
            addElement(ElementObject("Squirtle"))
            addElement(ElementObject("Meowth"))

        }
    }


}