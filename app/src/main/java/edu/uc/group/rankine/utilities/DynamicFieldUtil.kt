package edu.uc.group.rankine.utilities

import android.app.Activity
import android.app.Application
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ElementObject

/**
 * Utility that adds and removes fields dynamically and saves data from editTextViews to a sharedPreference
 */
class DynamicFieldUtil(activity: Activity) : Application() {
    private var _activity = activity
    private val getAllViewChildren = GetAllViewChildren()
    private val nameEditText = _activity.findViewById<EditText>(R.id.create_rank_fragment_name)
    private val scrollContainer = _activity.findViewById<LinearLayout>(R.id.create_rank_fragment_scrollContainer)
    private var attribute = ""
    var name = ""
    var elementArray = ArrayList<ElementObject>()

    /**
     * Updates the ElementObject dto with the attribute data.
     * Then stores a ElementObject object into an ArrayList
     */
    fun create() {
        val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(scrollContainer!!)
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
     */
    fun userFilter(view: ArrayList<View>): Boolean {
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