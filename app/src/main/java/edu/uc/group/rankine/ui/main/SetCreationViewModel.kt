package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.view.View

import androidx.lifecycle.ViewModel
import edu.uc.group.rankine.utilities.DynamicFieldService

class SetCreationViewModel(activity: Activity) : ViewModel() {
    var ctx = activity
    var dynamicFieldService =
        DynamicFieldService(ctx)


    fun addElements(){
       dynamicFieldService.addElements()
    }

    fun removeElements(view: View){
        dynamicFieldService.removeElements(view)
    }

    fun addFields(){
        dynamicFieldService.addFields()
    }

    fun removeFields(){
        dynamicFieldService.removeFields()
    }

    fun create(){

        dynamicFieldService.create()
    }
}