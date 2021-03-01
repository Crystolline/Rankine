package edu.uc.group.rankine.dto

data class ObjectSet(var elements: ArrayList<ElementObject> = ArrayList(), var fields: ArrayList<FieldObject> = ArrayList())
{

    fun addElement(e: ElementObject)
    {
        elements.add(e)

    }


    fun addField(f: FieldObject)
    {
        fields.add(f)
    }



    fun modifyElements(inputObject: ElementObject, newObject : ElementObject)
    {
        //Eventually should bring up a list of each field
        //Select whether the user would like to delete or edit the fields in a set
        elements.remove(inputObject)
        addElement(newObject)





    }

    fun modifyFields(inputObject: FieldObject, newObject : FieldObject)
    {
        //Eventually should bring up a list of each field
        //Select whether the user would like to delete or edit the fields in a set
        fields.remove(inputObject)
        addField(newObject)
    }

}


