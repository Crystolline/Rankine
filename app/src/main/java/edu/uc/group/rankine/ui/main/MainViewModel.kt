package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.ColorSpace
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.utilities.DynamicFieldUtil

/**
 * CreateRankViewModel that takes CreateRanks Context as a constructor
 */
class MainViewModel(activity: Activity) : ViewModel() {
    private var ctx = activity
    private var dynamicFieldService = DynamicFieldUtil(ctx)
    var idList = ArrayList<String>()

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _objectSets: MutableLiveData<ArrayList<ObjectSet>> = MutableLiveData()
    private var _objectSet = ObjectSet()
    var allObjectSets = ArrayList<ObjectSet>()
    val recycle = activity?.findViewById<RecyclerView>(R.id.recycle)


    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseDBListener()

    }

    companion object {
        private var imageUriString = ""
    }


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
     *  calls create function from DynamicFieldUtil
     */
    fun create() {

        objectSet = ObjectSet()
        dynamicFieldService.create()
        with(objectSet) {

            name = dynamicFieldService.name
            elements = dynamicFieldService.elementArray
            localUri = MainViewModel.imageUriString

        }

        save(objectSet)
        clearAll()
    }

    private fun clearAll() {
        objectSet.elements = ArrayList<ElementObject>()
        objectSet.localUri = ""
        objectSet.name = ""

    }

    fun getImageUriString(imageUri: String) {
        MainViewModel.imageUriString = imageUri

    }

    private fun save(objectSet: ObjectSet) {
        val document = firestore.collection("rankData").document()
        val set = document.set(objectSet)
        set.addOnSuccessListener {
            Log.d("Firebase", "document saved")
            idList.add(document.id)
            val id = document.id
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    private fun firebaseDBListener() {
        firestore.collection("rankData").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                allObjectSets = ArrayList<ObjectSet>()
                val documents = snapshot.documents
                documents.forEach {
                    val objectSet = it.toObject(ObjectSet::class.java)
                    if (objectSet != null) {
                        allObjectSets.add(objectSet)
                    }
                }
                _objectSets.value = allObjectSets
            }
        }
    }

    internal var objectSets: MutableLiveData<ArrayList<ObjectSet>>
        get() {
            return _objectSets
        }
        set(value) {
            _objectSets = value
        }

    internal var objectSet: ObjectSet
        get() {
            return _objectSet
        }
        set(value) {
            _objectSet = value
        }
}

