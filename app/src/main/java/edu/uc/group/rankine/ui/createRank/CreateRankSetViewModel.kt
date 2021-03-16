package edu.uc.group.rankine.ui.createRank

import android.app.Activity
import android.view.View

import androidx.lifecycle.ViewModel
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.utilities.DynamicFieldUtil

/**
 * CreateRankViewModel that takes CreateRanks Context as a constructor
 */
class CreateRankSetViewModel(activity: Activity) : ViewModel() {
    var ctx = activity
    var dynamicFieldService = DynamicFieldUtil(ctx)
    var objectSet = ObjectSet()
    private val createRankSet = CreateRankSet()
    private var imageUriString = ""


    /**
     *  calls addElements function from DynamicFieldUtil
     */
    fun addElements() {
        dynamicFieldService.addElements()
    }

    /**
     *  calls removeElements function from DynamicFieldUtil
     */
    fun removeElements(view: View) {
        dynamicFieldService.removeElements(view)
    }

    /**
     *  calls addFields function from DynamicFieldUtil
     */
    fun addFields(id: Int, view: View) {
        dynamicFieldService.addFields(id, view)
    }

    /**
     *  calls removeFields function from DynamicFieldUtil
     */
    fun removeFields(view: View, id: Int) {
        dynamicFieldService.removeFields(view, id)
    }

    /**
     *  calls create function from DynamicFieldUtil
     */
    fun create() {

        var data = dynamicFieldService.create()
        if (imageUriString == "") {
            objectSet.getUserJSONData(data)
        } else {
            objectSet.getUserJSONData(data, imageUriString)
        }
    }

    fun getImageUriString(imageUri: String) {
        imageUriString = imageUri
    }
}