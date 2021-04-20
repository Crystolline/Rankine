package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet

/**
 * Shared ViewModel for all fragments
 */
class MainViewModel(activity: Activity) : ViewModel() {
    private var ctx = activity
    var idList = ArrayList<String>()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _objectSets: MutableLiveData<ArrayList<ObjectSet>> = MutableLiveData()
    private var _objectSet = ObjectSet()
    var allObjectSets = ArrayList<ObjectSet>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseDBListener()

    }

    companion object {
        private var imageUriString = ""
    }

    /**
     *  Populates the ObjectSet dto with data.
     *  Calls the save function to save the dto in the database.
     *  Calls the clearAll function to clear the data in the dto.
     */
    fun saveSet(setName: String) {
        with(objectSet) {
            this.name = setName
            localUri = imageUriString
        }

        save(objectSet)
        clearAll()
    }

    /**
     * Clears the data sent to the dto
     */
    private fun clearAll() {
        objectSet.elements = ArrayList<ElementObject>()
        objectSet.localUri = ""
        objectSet.name = ""
    }

    fun getImageUriString(imageUri: String) {
        MainViewModel.imageUriString = imageUri
    }

    /**
     * Saves the data from the dto to the firebase
     */
    private fun save(objectSet: ObjectSet) {
        val document = if (objectSet.id.isNotBlank()) {
            //update existing
            firestore.collection("rankData").document()
        } else {
            //create new
            firestore.collection("rankData").document()
        }
        objectSet.id = document.id
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

    /**
     * deletes the specified document from the DB
     * @param id the id of the document to be deleted
     */
    fun removeDbDoc(id: String) {
        val document = firestore.collection("rankData").document(id)
        document.delete()
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

