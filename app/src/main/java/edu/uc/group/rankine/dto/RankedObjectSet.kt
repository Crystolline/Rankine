package edu.uc.group.rankine.dto

import com.google.firebase.firestore.Exclude
import edu.uc.group.rankine.ui.ranking.RankSetViewFragment

/**
 * A set of elements that is being ranked or is ranked
 * @property indexLeft The index of the next [ElementObject] in [arrayLeft]
 * @property indexRight The index of the next [ElementObject] in [arrayRight]
 * @property id The Firestore id for the RankedObjectSet
 * @property set A copy of the [ObjectSet] the RankedObjectSet was made from
 * @property rankedElements A list of ranked [ElementObject]s, assuming the RankedObjectSet has been ranked
 * @property fullMergeList The ArrayList of [ElementObject] ArrayLists to be merged
 * @property arrayLeft The ArrayList of [ElementObject]s that is currently being merged on the left
 * @property leftElement The current left [ElementObject]
 * @property arrayRight The ArrayList of [ElementObject]s that is currently being merged on the right
 * @property rightElement The current right [ElementObject]
 * @property arrayMerge The merged [ElementObject]s from [arrayLeft] and [arrayRight]
 * @property menu Whether or not the menu is currently showing on the [RankSetViewFragment]
 */
class RankedObjectSet(
    var indexLeft: Int = 0,
    var indexRight: Int = 0,
    var id: String = ""
) {

    private var _set: ObjectSet = ObjectSet()
    private var _rankedElements: ArrayList<ElementObject> = ArrayList()
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

    var rankedElements: ArrayList<ElementObject>
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

    private var menu: Boolean
        @Exclude get() {
            return _menu
        }
        set(value) {
            _menu = value
        }

    /**
     * Starts a new ranking
     */
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

    /**
     * @return [rankedElements] if the RankedObjectSet has been ranked, [set]'s elements if otherwise
     */
    @Exclude
    fun getCleanRankedElements(): ArrayList<ElementObject> {
        if (rankedElements.isNotEmpty()) {
            return rankedElements
        }
        return set.elements
    }

    /**
     * @return Whether or not the RankedObjectSet has been ranked
     */
    @Exclude
    fun isRanked(): Boolean {
        return rankedElements.isNotEmpty()
    }

    /**
     * @return Whether or not the RankedObjectSet has a ranking in progress
     */
    @Exclude
    fun isRanking(): Boolean {
        return fullMergeList.count() > 0 || arrayLeft.count() > 0 || arrayRight.count() > 0 || arrayMerge.count() > 0
    }

    /**
     * Retrieve the results of a comparison and perform MergeSort steps up until next comparison is needed
     * @param rightChosen whether the rightElement was chosen over the leftElement
     */
    fun sortStep(rightChosen: Boolean) {
        if (isRanking()) {
            if (rightChosen) {
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