package edu.uc.group.rankine.dto


/**
 * A <set> of elements that is being ranked or is ranked
 */
class RankedObjectSet(var set : ObjectSet)
{
    var rankedElements: ArrayList<ElementObject> = ArrayList()

    fun getRankedElements() : ArrayList<ElementObject>
    {
        return mergeSort(set.getElements())
    }


    fun mergeSort(elementsToRank: ArrayList<ElementObject>): ArrayList<ElementObject>
    {
        if (elementsToRank.size <= 1)
        {
            return elementsToRank
        }

        val middle = elementsToRank.size / 2
        var left = elementsToRank.subList(0,middle);
        var right = elementsToRank.subList(middle,elementsToRank.size);

        return merge(mergeSort(left), mergeSort(right))
    }

    fun merge(left: ArrayList<ElementObject>, right: ArrayList<ElementObject>): ArrayList<ElementObject>
    {
        var indexLeft = 0
        var indexRight = 0
        var newList : ArrayList<ElementObject> = ArrayList()

        while (indexLeft < left.count() && indexRight < right.count())
        {
            //If it is already in the right order
            if (promptUser(left[indexLeft], right[indexRight]) == left[indexRight])
            {
                newList.add(left[indexLeft])
                indexLeft++
            }
            else
            {
                newList.add(right[indexRight])
                indexRight++
            }
        }

        while (indexLeft < left.size)
        {
            newList.add(left[indexLeft])
            indexLeft++
        }

        while (indexRight < right.size)
        {
            newList.add(right[indexRight])
            indexRight++
        }
        return newList;
    }

    /**
     * Prompt the user to make a choice between two objects in the ObjectSet
     */
    fun promptUser(var firstElement : ElementObject, var secondElement : ElementObject) : ElementObject
    {





        return winningElement
    }



}