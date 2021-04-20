package edu.uc.group.rankine.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ObjectSet
import java.io.File

class MainFragment : Fragment() {
    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory
    private var _objectSets = ArrayList<ObjectSet>()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val btnOpenActivity: FloatingActionButton? = activity?.findViewById(R.id.btnCreateRank)
        val recycle = activity?.findViewById<RecyclerView>(R.id.recycle)
        vmFactory = activity?.let { MainViewModelFactory(it) }!!
        vm = ViewModelProvider(this, vmFactory)
                .get(MainViewModel::class.java)
        btnOpenActivity?.setOnClickListener {
            (activity as MainActivity).moveToCreateRankSet()
        }

        recycle?.hasFixedSize()
        recycle?.layoutManager = LinearLayoutManager(context)
        recycle?.itemAnimator = DefaultItemAnimator()
        recycle?.adapter = MainViewAdaptor(_objectSets, R.layout.dynamic_main_view)

        vm.objectSets.observe(viewLifecycleOwner, Observer {
            _objectSets.removeAll(_objectSets)
            _objectSets.addAll(it)
            recycle?.adapter!!.notifyDataSetChanged()
        })
    }

    /**
     * Defines the adapter being used to bind the views to their data
     */
    inner class MainViewAdaptor(val elements: ArrayList<ObjectSet>, val itemLayout: Int) :
            RecyclerView.Adapter<MainFragment.MainViewHolder>() {

        /**
         * Constructs the view that represents the ArrayList<ObjectSet>
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
            return MainViewHolder(view)
        }

        /**
         * Gets the number of elements in the arrayList
         */
        override fun getItemCount(): Int {
            var int = elements.size
            return int!!
        }

        /**
         * Used to display items of the adapter
         */
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val element = elements.get(position)
            holder.updateElement(element)
        }

    }

    /**
     * Describes the data that should be put in the view holder object
     */
    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var element: TextView = itemView.findViewById(R.id.dynamic_name)
        private var image: ImageView = itemView.findViewById(R.id.dynamic_image)
        fun updateElement(objectSet: ObjectSet) {
            image.clipToOutline = true
            element.text = objectSet.name
            if (objectSet.localUri != "null" && objectSet.localUri != "" && File(objectSet.localUri).exists()) {
                val uri = Uri.fromFile(File(objectSet.localUri))
                val source = ImageDecoder.createSource(activity!!.contentResolver, uri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                image.setImageBitmap(bitmap)
            } else {

                image.setImageResource(R.drawable.ic_launcher_foreground)
            }

        }
    }
}