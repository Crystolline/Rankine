package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.content.ContentValues.TAG
import android.renderscript.Allocation
import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.dto.RankedObjectSet
import kotlinx.coroutines.*

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
    var allRankedObjectSets = ArrayList<RankedObjectSet>()
    private var _elements = MutableLiveData<List<ElementObject>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseDBListenerSets()
        firebaseDBListenerRanks()
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
            if(imageUriString != "") localUri = imageUriString
        }

        var document = if (objectSet.id.isNotBlank()) {
            //update existing
            firestore.collection("rankData").document(objectSet.id)
        } else {
            //create new
            firestore.collection("rankData").document()
        }
        saveSetToFirebase(document, objectSet)
        objectSet = ObjectSet()
        imageUriString = ""
    }

    fun getImageUriString(imageUri: String) {
        MainViewModel.imageUriString = imageUri
    }

    fun saveRankToFirebase(rankedSet: RankedObjectSet) {
        val document = if (rankedSet.id.isNotBlank()) {
            firestore.collection("rankedData").document(rankedSet.id)
        } else {
            firestore.collection("rankedData").document()
        }
        rankedSet.id = document.id
        val set = document.set(rankedSet)
        set.addOnSuccessListener {
            //Save rankedSet.set
            saveSetToFirebase(
                document.collection("set").document("set"), rankedSet.set
            )
            //Save rankedSet.rankedElements
            saveNullableArrayListOfElementsToFirebase(
                document.collection("rankedElements"),
                rankedSet.rankedElements
            )
            //Save rankedSet.fullMergeList
            saveArrayListOfArrayListOfElementsToFirebase(
                document.collection("fullMergeList"),
                rankedSet.fullMergeList
            )
            //Save rankedSet.arrayLeft
            saveArrayListOfElementsToFirebase(
                document.collection("arrayLeft"),
                rankedSet.arrayLeft
            )
            //Save rankedSet.leftElement
            saveElement(
                rankedSet.leftElement,
                document.collection("NextElements").document("leftElement")
            )
            //Save rankedSet.arrayRight
            saveArrayListOfElementsToFirebase(
                document.collection("arrayRight"),
                rankedSet.arrayRight
            )
            //Save rankedSet.rightElement
            saveElement(
                rankedSet.rightElement,
                document.collection("NextElements").document("rightElement")
            )
            //Save rankedSet.arrayMerge
            saveArrayListOfElementsToFirebase(
                document.collection("arrayMerge"),
                rankedSet.arrayMerge
            )
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    /**
     * Saves the data from the dto to the firebase
     */
    private fun saveSetToFirebase(document: DocumentReference, objectSet: ObjectSet) {
        objectSet.id = document.id
        val set = document.set(objectSet)
        set.addOnSuccessListener {
            saveArrayListOfElementsToFirebase(document.collection("elements"), objectSet.elements)
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    private fun saveArrayListOfElementsToFirebase(
        collection: CollectionReference,
        elements: ArrayList<ElementObject>
    ) {
        for (i in 0 until elements.size) {
            var id = "00000000000000000000".dropLast(i.toString().length) + i.toString()
            saveElement(elements[i], collection.document(id))
        }
        collection.get()
            .addOnSuccessListener { doc ->
                for (document in doc)
                    if (document.reference.id.toInt() >= elements.size) document.reference.delete()
            }
    }

    private fun saveElement(elementObject: ElementObject, elementDocument: DocumentReference) {
        elementObject.id = elementDocument.id
        val set = elementDocument.set(elementObject)
        set.addOnSuccessListener {
            Log.d("Firebase", "Element saved $elementObject")
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    private fun saveNullableArrayListOfElementsToFirebase(
        collection: CollectionReference,
        list: ArrayList<ElementObject>?
    ) {
        if (list != null) {
            saveArrayListOfElementsToFirebase(collection, list)
        }
    }

    private fun saveArrayListOfArrayListOfElementsToFirebase(
        collection: CollectionReference,
        list: ArrayList<ArrayList<ElementObject>>
    ) {
        collection.get()
            .addOnSuccessListener { doc ->
                for (document in doc)
                    removeArrayListFromFirebase(document.reference)
            }
        for (i in 0 until list.size) {
            var id = "00000000000000000000".dropLast(i.toString().length) + i.toString()
            saveArrayListOfElementsToFirebase(
                collection.document(id).collection("elements"),
                list[i]
            )
        }
    }

    private fun removeArrayListFromFirebase(document: DocumentReference) {
        document.collection("elements").get()
            .addOnSuccessListener { doc ->
                for (document in doc)
                    document.reference.delete()
            }
        document.delete()
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
        firestore.collection("rankedData").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                allRankedObjectSets = ArrayList<RankedObjectSet>()
                val documents = snapshot.documents
                documents.forEach {
                    var rankedSet = it.toObject(RankedObjectSet::class.java)

                    if (rankedSet != null) {
                        //Retrieve set
                        fetchSetFromFirebase(
                            it.reference.collection("set").document("set"),
                            rankedSet.set
                        )
                        //Retrieve rankedElements
                        val standInArray = ArrayList<ElementObject>()
                        fetchArrayListOfElementsFromFirebase(
                            it.reference.collection("rankedElements"),
                            rankedSet.rankedElements
                        )
                        //Retrieve fullMergeList
                        fetchArrayListOfArrayListOfElementsFromFirebase(
                            it.reference.collection("fullMergeList"),
                            rankedSet.fullMergeList
                        )
                        //Retrieve arrayLeft
                        fetchArrayListOfElementsFromFirebase(
                            it.reference.collection("arrayLeft"),
                            rankedSet.arrayLeft
                        )
                        //Retrieve leftElement
                        fetchElement(
                            it.reference.collection("NextElements").document("leftElement"),
                            rankedSet.leftElement
                        )
                        //Retrieve arrayRight
                        fetchArrayListOfElementsFromFirebase(
                            it.reference.collection("arrayRight"),
                            rankedSet.arrayRight
                        )
                        //Retrieve rightElement
                        fetchElement(
                            it.reference.collection("NextElements").document("rightElement"),
                            rankedSet.rightElement
                        )
                        //Retrieve arrayMerge
                        fetchArrayListOfElementsFromFirebase(
                            it.reference.collection("arrayMerge"),
                            rankedSet.arrayMerge
                        )
                        allRankedObjectSets.add(rankedSet)
                    }
                }
                _rankSets.value = allRankedObjectSets
            }
        }
    }

    private fun fetchSetFromFirebase(document: DocumentReference, objectSet: ObjectSet) {
        document.get().addOnSuccessListener {
            val localObjectSet = it.toObject(ObjectSet::class.java)
            if (localObjectSet != null) {
                objectSet.id = localObjectSet.id
                objectSet.name = localObjectSet.name
                objectSet.localUri = localObjectSet.localUri
                fetchArrayListOfElementsFromFirebase(
                    document.collection("elements"),
                    objectSet.elements
                )
            }
        }
    }

    private fun fetchArrayListOfArrayListOfElementsFromFirebase(
        collection: CollectionReference,
        list: ArrayList<ArrayList<ElementObject>>
    ) {
        collection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            snapshot?.documents?.forEach {
                val elementList: ArrayList<ElementObject> = ArrayList<ElementObject>()
                fetchArrayListOfElementsFromFirebase(
                    it.reference.collection("elements"),
                    elementList
                )
                list.add(elementList)
            }
        }
    }

    private fun fetchArrayListOfElementsFromFirebase(
        collection: CollectionReference,
        elements: ArrayList<ElementObject>
    ) {
        collection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val allElements =
                    snapshot.toObjects(ElementObject::class.java) as ArrayList<ElementObject>
                allElements.forEach {
                    elements.add(it)
                }
            }
        }
    }

    private fun fetchElement(document: DocumentReference, elementObject: ElementObject) {
        document.get().addOnSuccessListener {
            var localElement = it.toObject(ElementObject::class.java)
            if (localElement != null) {
                elementObject.element = localElement.element
                elementObject.id = localElement.id
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


