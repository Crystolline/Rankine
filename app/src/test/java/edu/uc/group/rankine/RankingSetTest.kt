package edu.uc.group.rankine

import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.FieldObject
import edu.uc.group.rankine.dto.ObjectSet
import org.junit.Test

class RankingSetTest
{
    lateinit var workingObjectSet : ObjectSet




    private fun givenSet()
    {

        //We still need to implement a map of elements to fields
        //For example, the element Pikachu has attributes of type, best ability, and weight that define it
        //When a user is creating a set, the number and type of fields should change how the elements are declared

        workingObjectSet = ObjectSet()
        with(workingObjectSet)
        {
            addElement(ElementObject("Pikachu"))
            addElement(ElementObject("Charmander"))
            addElement(ElementObject("Bulbasaur"))
            addElement(ElementObject("Squirtle"))
            addElement(ElementObject("Meowth"))


            addField(FieldObject("Type","String"))
            addField(FieldObject("Best Ability","String"))
            addField(FieldObject("Weight","Double"))

        }
    }


}