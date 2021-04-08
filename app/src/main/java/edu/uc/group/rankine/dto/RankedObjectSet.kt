package edu.uc.group.rankine.dto


/**
 * A <set> of elements that is being ranked or is ranked
 */
class RankedObjectSet(var set: ObjectSet) {
    private var rankedElements: ArrayList<ElementObject>? = null
    private lateinit var fullMergeList: ArrayList<ArrayList<ElementObject>>
    private lateinit var arrayLeft: ArrayList<ElementObject>
    private var indexLeft: Int = 0
    lateinit var leftElement: ElementObject
    private lateinit var arrayRight: ArrayList<ElementObject>
    private var indexRight: Int = 0
    lateinit var rightElement: ElementObject
    private lateinit var arrayMerge: ArrayList<ElementObject>

    fun startRanking() {
        if (set.elements.size <= 1) {
            rankedElements = set.elements
        } else {
            set.elements.forEach {
                fullMergeList.add(arrayListOf(it))
            }
            arrayLeft = fullMergeList.removeAt(0)
            indexLeft = 0
            leftElement = arrayLeft[indexLeft]
            arrayRight = fullMergeList.removeAt(0)
            indexRight = 0
            rightElement = arrayRight[indexRight]
            arrayMerge = ArrayList()
        }
    }

    @JvmName("getRankedElements1")
    fun getRankedElements(): ArrayList<ElementObject> {
        if (rankedElements != null) {
            return rankedElements!!
        }
        return set.elements
    }

    fun getRankings(index: Int): ElementObject? {
        if (rankedElements != null && index < rankedElements!!.count()) {
            return rankedElements!![index]
        }
        return null
    }

    fun isRanked(): Boolean {
        return rankedElements != null
    }

    fun isRanking(): Boolean {
        return fullMergeList.count() > 0 || arrayLeft.count() > 0 || arrayRight.count() > 0 || arrayMerge.count() > 0
    }

    /**
     * Retrieve the results of a comparison and perform MergeSort steps up until next comparison is needed
     */
    fun sortStep(side: Boolean) {
        if(isRanking()) {
            if (side) {
                arrayMerge.add(arrayRight[indexRight])
                indexRight++
            } else {
                arrayMerge.add(arrayLeft[indexLeft])
                indexLeft++
            }

            if (indexRight < arrayRight.count() && indexLeft < arrayLeft.count()) {
                leftElement = arrayLeft[indexLeft]
                rightElement = arrayRight[indexRight]
            } else {
                while (indexLeft < arrayLeft.count()) {
                    arrayMerge.add(arrayLeft[indexLeft])
                    indexLeft++
                }
                while (indexRight < arrayRight.count()) {
                    arrayMerge.add(arrayRight[indexRight])
                    indexRight++
                }

                fullMergeList.add(arrayMerge)
                if (fullMergeList.count() == 1) {
                    rankedElements = fullMergeList[0]
                    fullMergeList.clear()
                    arrayLeft.clear()
                    indexLeft = 0
                    leftElement = ElementObject("Ranking")
                    arrayRight.clear()
                    indexRight = 0
                    rightElement = ElementObject("Completed")
                    arrayMerge.clear()
                } else {
                    arrayLeft = fullMergeList.removeAt(0)
                    indexLeft = 0
                    leftElement = arrayLeft[indexLeft]
                    arrayRight = fullMergeList.removeAt(0)
                    indexRight = 0
                    rightElement = arrayRight[indexRight]
                    arrayMerge.clear()
                }
            }
        }
    }
}