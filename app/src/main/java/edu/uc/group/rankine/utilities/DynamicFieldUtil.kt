package edu.uc.group.rankine.utilities

import android.app.Activity
import android.app.Application
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ElementObject

/**
 * Utility that gets the text from the ScrollContainer and filter that checks if all views have been filled.
 */
class DynamicFieldUtil {
    private val getAllViewChildren = GetAllViewChildren()
    private var attribute = ""
    var name = ""
    var elementArray = ArrayList<ElementObject>()

    /**
     * Updates the ElementObject dto with the attribute data.
     * Then stores a ElementObject object into an ArrayList
     * @param nameEditText this is the name EditText view
     * @param scrollContainer this is the Layout that holds the ScrollView
     */
    fun create(nameEditText: EditText, scrollContainer: LinearLayout) {
        val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(scrollContainer)
        name = nameEditText.text.toString()
        for (child: View in allViews) {
            if (child is EditText) run {
                attribute = child.text.toString()
                val elementObject = ElementObject(attribute)
                elementArray.add(elementObject)
            }
        }
    }

    /**
     * returns true if name and all created elements are populated otherwise return false
     * @param view an ArrayList of views to check
     * @param nameEditText the name EditText to check
     * @return true if all fields have been filled
     * @return false if a field is blank
     */
    fun userFilter(view: ArrayList<View>, nameEditText: EditText): Boolean {
        val storeNameText = nameEditText.text
        if (view.size == 0) {
            return false
        }
        for (child: View in view) {
            if (child is EditText) {
                if (child.text.toString() == "" || storeNameText.toString() == "") {
                    return false
                }
            }
        }
        return true
    }


}