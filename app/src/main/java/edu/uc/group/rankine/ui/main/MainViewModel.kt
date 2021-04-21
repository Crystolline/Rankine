package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.dto.RankedObjectSet

/**
 * Shared ViewModel for all fragments
 */
class MainViewModel(activity: Activity) : ViewModel() {
    private var ctx = activity
    var idList = ArrayList<String>()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _objectSets: MutableLiveData<ArrayList<ObjectSet>> = MutableLiveData()
    private var _objectSet = ObjectSet()
    private var _rankSets: MutableLiveData<ArrayList<RankedObjectSet>> = MutableLiveData()
    private var _rankSet: RankedObjectSet = RankedObjectSet()
    var allObjectSets = ArrayList<ObjectSet>()
    private var _elements = MutableLiveData<List<ElementObject>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseDBListenerSets()
    }

    companion object {
        var getData: ObjectSet? = null
        var imageUriString = ""
        var getRank: RankedObjectSet? = null

        /**
         * Sets the getData var to an individual ObjectSet
         * This should be used to get the ObjectSet at a certain position in the MainFragment RecyclerView
         */
        fun setData(objectSet: ObjectSet) {
            getData = objectSet
        }

        fun setRank(rankSet: RankedObjectSet) {
            getRank = rankSet
        }

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

        saveSets(objectSet)
        objectSet = ObjectSet()
        imageUriString = ""
    }

    fun getImageUriString(imageUri: String) {
        MainViewModel.imageUriString = imageUri
    }

    /**
     * Saves the data from the dto to the firebase
     */
    private fun saveSets(objectSet: ObjectSet) {
        val document = if (objectSet.id.isNotBlank()) {
            //update existing
            firestore.collection("rankData").document(objectSet.id)
        } else {
            //create new
            firestore.collection("rankData").document()
        }
        objectSet.id = document.id
        val set = document.set(objectSet)
        set.addOnSuccessListener {
            var zeroes = "00000000000000000000"
            for(i in 0 until objectSet.elements.size){
                var id = zeroes
                id = id.dropLast(i.toString().length)
                id += i.toString()
                saveElement(objectSet.elements[i], id, document)
            }
            for(i in objectSet.elements.size until objectSet.initialSize){
                var id = zeroes
                id = id.dropLast(i.toString().length)
                id += i.toString()
                removeElement(id, document)
            }
            objectSet.initialSize = objectSet.elements.size
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    private fun saveRanks(rankedSet: RankedObjectSet) {
        val document = if (rankedSet.id.isNotBlank()) {
            firestore.collection("rankedData").document(rankedSet.id)
        } else {
            firestore.collection("rankedData").document()
        }
        rankedSet.id = document.id
        val set = document.set(rankedSet)

    }

    private fun saveElement(elementObject: ElementObject, id: String, document: DocumentReference) {
        val elementDocument = document.collection("elements").document(id)
        elementObject.id = elementDocument.id
        val set = elementDocument.set(elementObject)
        set.addOnSuccessListener {
            Log.d("Firebase", "Element saved $elementObject")
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    private fun removeElement(id: String, document: DocumentReference) {
        val elementDocument = document.collection("elements").document(id)
        elementDocument.delete()
    }

    /**
     * deletes the specified document from the DB
     * @param id the id of the document to be deleted
     */
    fun removeDbDoc(id: String) {
        val document = firestore.collection("rankData").document(id)
        document.delete()
    }

    private fun firebaseDBListenerSets() {
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
                        val elementsCollection = firestore.collection("rankData")
                            .document(objectSet.id)
                            .collection("elements")
                        elementsCollection.addSnapshotListener { querySnapshot, e ->
                            if (querySnapshot != null) {
                                val innerElements =
                                    querySnapshot.toObjects(ElementObject::class.java)
                                objectSet.elements = innerElements as ArrayList<ElementObject>
                            }
                        }
                        allObjectSets.add(objectSet)
                    }
                }
                _objectSets.value = allObjectSets
            }
        }
    }

    private fun firebaseDBListenerRanks() {

    }

    internal var objectSets: MutableLiveData<ArrayList<ObjectSet>>
        get() {
            return _objectSets
        }
        set(value) {
            _objectSets = value
        }

    internal var rankSets: MutableLiveData<ArrayList<RankedObjectSet>>
        get() {
            return _rankSets
        }
        set(value) {
            _rankSets = value
        }

    internal var objectSet: ObjectSet
        get() {
            return _objectSet
        }
        set(value) {
            _objectSet = value
        }

    internal var rankSet: RankedObjectSet
        get() {
            return _rankSet
        }
        set(value) {
            _rankSet = value
        }
}


