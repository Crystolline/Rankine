package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.view.View

import androidx.lifecycle.ViewModel
import edu.uc.group.rankine.utilities.DynamicFieldUtil

class SetCreationViewModel(activity: Activity) : ViewModel() {
    var ctx = activity
    var dynamicFieldService = DynamicFieldUtil(ctx)


    fun addElements(){
        dynamicFieldService.addElements()
    }

    fun removeElements(view: View){
        dynamicFieldService.removeElements(view)
    }

    fun addFields(id:Int){
        dynamicFieldService.addFields(id)
    }

    fun removeFields(view: View, id: Int){
        dynamicFieldService.removeFields(view, id)
    }

    fun create(){

        dynamicFieldService.create()
    }
}