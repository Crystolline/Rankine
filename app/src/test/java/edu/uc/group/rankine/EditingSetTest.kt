package edu.uc.group.rankine

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.FieldObject
import edu.uc.group.rankine.dto.ObjectSet
import junit.framework.Assert.assertTrue
import org.junit.Test

class EditingSetTest {
    lateinit var workingObjectSet: ObjectSet

    @Test
    fun ConfirmCelcius_outputsCelcius(){
        var element: ElementObject = ElementObject("Celcius")
        assertTrue(element.name.equals("Celcius"))
    }

    @Test
    fun editElement_containsNewElement()
    {
        givenSet()
        whenEditElement()
        thenContainsNewElement()
    }

    private fun thenContainsNewElement()
    {
        var containsMewtwo = false

        if(workingObjectSet.elements.contains(ElementObject("Mewtwo")))
        {
            containsMewtwo = true
        }
        assert(containsMewtwo)

    }

    private fun whenEditElement()
    {
        workingObjectSet.modifyElements(ElementObject("Meowth"), ElementObject("Mewtwo"))
        assert(!workingObjectSet.elements.contains(ElementObject("Meowth")))
    }

    private fun givenSet()
    {

        workingObjectSet = ObjectSet()
        with(workingObjectSet)
        {
            addElement(ElementObject("Pikachu"))
            addElement(ElementObject("Charmander"))
            addElement(ElementObject("Bulbasaur"))
            addElement(ElementObject("Squirtle"))
            addElement(ElementObject("Meowth"))
            addField(FieldObject("Type", "String"))
            addField(FieldObject("Best Ability","String"))
            addField(FieldObject("Weight", "Double"))
        }
    }
}