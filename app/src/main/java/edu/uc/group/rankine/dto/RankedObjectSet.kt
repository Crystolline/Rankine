package edu.uc.group.rankine.dto

import com.google.firebase.firestore.Exclude

/**
 * A <set> of elements that is being ranked or is ranked
 */
class RankedObjectSet(
    var indexLeft: Int = 0,
    var indexRight: Int = 0,
    var id: String = "") {

    private var _set: ObjectSet = ObjectSet()
    private var _rankedElements: ArrayList<ElementObject>? = null
    private var _fullMergeList: ArrayList<ArrayList<ElementObject>> = ArrayList()
    private var _arrayLeft: ArrayList<ElementObject> = ArrayList()
    private var _leftElement: ElementObject = ElementObject()
    private var _arrayRight: ArrayList<ElementObject> = ArrayList()
    private var _rightElement: ElementObject = ElementObject()
    private var _arrayMerge: ArrayList<ElementObject> = ArrayList()
    private var _menu: Boolean = false

    var set: ObjectSet
        @Exclude get() {
            return _set
        }
        set(value) {
            _set = value
        }

    var rankedElements: ArrayList<ElementObject>?
        @Exclude get() {
            return _rankedElements
        }
        set(value) {
            _rankedElements = value
        }

    var fullMergeList: ArrayList<ArrayList<ElementObject>>
        @Exclude get() {
            return _fullMergeList
        }
        set(value) {
            _fullMergeList = value
        }

    var arrayLeft: ArrayList<ElementObject>
        @Exclude get() {
            return _arrayLeft
        }
        set(value) {
            _arrayLeft = value
        }

    var leftElement: ElementObject
        @Exclude get() {
            return _leftElement
        }
        set(value) {
            _leftElement = value
        }

    var arrayRight: ArrayList<ElementObject>
        @Exclude get() {
            return _arrayRight
        }
        set(value) {
            _arrayRight = value
        }

    var rightElement: ElementObject
        @Exclude get() {
            return _rightElement
        }
        set(value) {
            _rightElement = value
        }

    var arrayMerge: ArrayList<ElementObject>
        @Exclude get() {
            return _arrayMerge
        }
        set(value) {
            _arrayMerge = value
        }

    var menu: Boolean
        @Exclude get() {
            return _menu
        }
        set(value) {
            _menu = value
        }

    fun startRanking() {
        if (set.elements.size <= 1) {
            rankedElements = set.elements
        } else {
            fullMergeList = arrayListOf()
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
        if (isRanking()) {
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
                    fullMergeList = arrayListOf()
                    arrayLeft = arrayListOf()
                    indexLeft = 0
                    leftElement = ElementObject("Ranking")
                    arrayRight = arrayListOf()
                    indexRight = 0
                    rightElement = ElementObject("Completed")
                    arrayMerge = arrayListOf()
                } else {
                    arrayLeft = fullMergeList.removeAt(0)
                    indexLeft = 0
                    leftElement = arrayLeft[indexLeft]
                    arrayRight = fullMergeList.removeAt(0)
                    indexRight = 0
                    rightElement = arrayRight[indexRight]
                    arrayMerge = arrayListOf()
                }
            }
        }
    }

    fun setShowMenu(b: Boolean) {
        menu = b
    }

    fun isShowMenu(): Boolean {
        if (menu) {
            return true
        }
        return false

    }
}