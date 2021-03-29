package edu.uc.group.rankine.ui.createRank

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.utilities.DynamicFieldUtil

/**
 * CreateRankViewModel that takes CreateRanks Context as a constructor
 */
class CreateRankSetViewModel(activity: Activity) : ViewModel() {
    private var ctx = activity
    private var dynamicFieldService = DynamicFieldUtil(ctx)
    private var objectSet = ObjectSet()
    var idList = ArrayList<String>()
    private val createRankSet = CreateRankSet()
    private var imageUriString = ""
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _objectSets: MutableLiveData<ArrayList<ObjectSet>> =
        MutableLiveData<ArrayList<ObjectSet>>()


    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseDBListener()
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
        val data = dynamicFieldService.create()
        objectSet.getUserJSONData(data, imageUriString)
        save(objectSet)
        objectSet = ObjectSet()
    }

    fun getImageUriString(imageUri: String) {
        imageUriString = imageUri
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
                val allObjectSets = ArrayList<ObjectSet>()
                val documents = snapshot.documents
                documents.forEach {
                    val objectSet = it.toObject(ObjectSet::class.java)
                    if (objectSet != null) {
                        allObjectSets.add(objectSet!!)
                    }
                }
                _objectSets.value = allObjectSets
            }
        }
    }

    internal var objectSetLiveData: MutableLiveData<ArrayList<ObjectSet>>
        get() {
            return _objectSets
        }
        set(value) {
            _objectSets = value
        }
}