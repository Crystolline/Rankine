package edu.uc.group.rankine

import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import org.junit.Test

class EditingSetTest {
    lateinit var workingObjectSet: ObjectSet

    @Test
    fun editElement_containsNewElement() {
        givenSet()
        whenEditElement()
        thenContainsNewElement()
    }

    private fun thenContainsNewElement() {
        var containsMewtwo = false
        if (workingObjectSet.elements.contains(ElementObject("Mewtwo"))) {
            containsMewtwo = true
        }
        assert(containsMewtwo)

    }

    private fun whenEditElement() {
        workingObjectSet.modifyElements(ElementObject("Meowth"), ElementObject("Mewtwo"))
        assert(!workingObjectSet.elements.contains(ElementObject("Meowth")))
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