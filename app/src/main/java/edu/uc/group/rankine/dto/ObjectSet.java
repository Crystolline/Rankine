package edu.uc.group.rankine.dto;

import java.util.Set;

public class ObjectSet
{
    private Set<ElementObject> elements;
    private Set<FieldObject> fields;

    public ObjectSet(Set<ElementObject> elements, Set<FieldObject> fields)
    {
        this.elements = elements;
        this.fields = fields;
    }

    public Set getElements() {
        return elements;
    }

    public Set getFields() {
        return fields;
    }

    public void addElements(ElementObject e)
    {
        if(!elements.add(e))
        {
            //Logic for if item already exists within this set
        }
        else
        {

        }


    }

    public void addFields(FieldObject f)
    {
        if(!fields.add(f))
        {
            //Logic for if item already exists within this set
        }
        else
        {

        }

    }

    public void modifyElements(Set elements)
    {
        //Bring up a list of each element
        //Select whether the user would like to delete or edit the elements in a set
    }

    public void modifyFields(Set fields)
    {
        //Bring up a list of each field
        //Select whether the user would like to delete or edit the fields in a set
    }


}
