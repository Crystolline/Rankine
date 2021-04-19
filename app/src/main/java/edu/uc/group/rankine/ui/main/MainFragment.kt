package edu.uc.group.rankine.ui.main

import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ObjectSet
import java.io.File

open class MainFragment : Fragment() {
    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory
    private var _objectSets = ArrayList<ObjectSet>()
    private var adapter: MainViewAdaptor? = null

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

        recycle?.layoutManager = LinearLayoutManager(context)
        adapter = MainViewAdaptor(_objectSets)
        recycle?.adapter = adapter
        val set = ItemTouchHelper(ItemTouchCallback(adapter!!))
        set.attachToRecyclerView(recycle!!)

        vm.objectSets.observe(viewLifecycleOwner, Observer {
            _objectSets.removeAll(_objectSets)
            _objectSets.addAll(it)
            recycle.adapter!!.notifyDataSetChanged()
        })


    }

    /**
     * Defines the adapter being used to bind the views to their data
     */
    open inner class MainViewAdaptor(
            private val elements: ArrayList<ObjectSet>,
            private val SHOW_MENU: Int = 1,
            private val HIDE_MENU: Int = 2
    ) :
            RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return if (elements[position].isShowMenu()) {
                SHOW_MENU
            } else {
                HIDE_MENU
            }
        }


        /**
         * Constructs the view that represents the ArrayList<ObjectSet>
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == SHOW_MENU) {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.dynamic_options, parent, false)
                MenuViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.dynamic_main_view, parent, false)
                MainViewHolder(view)
            }

        }

        /**
         * Gets the number of elements in the arrayList
         */
        override fun getItemCount(): Int {
            return elements.size
        }

        /**
         * Used to display items of the adapter
         */
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val element = elements[position]
            if (holder is MainViewHolder) {
                holder.updateElement(element)
            }

            if (holder is MenuViewHolder) {
                holder.setClickListeners(holder.adapterPosition, element)

            }

        }

        /**
         * sets the dto menu var to true at the swiped specified position
         */
        fun showMenu(position: Int) {
            for (i in 0 until elements.size) {
                elements[i].setShowMenu(false)
            }
            elements[position].setShowMenu(true)
            notifyDataSetChanged()
        }

        fun isMenuShown(): Boolean {
            for (i in 0 until elements.size) {
                if (elements[i].isShowMenu()) {
                    return true
                }
            }
            return false
        }

        fun closeMenu() {
            for (i in 0 until elements.size) {
                elements[i].setShowMenu(false)
            }
            notifyDataSetChanged()
        }

        /**
         * Describes the data that should be put in the view holder object for the dyanmic_main_view
         * What happens in dynamic_main_view
         */
        inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var element: TextView = itemView.findViewById(R.id.dynamic_main_view_name)
            private var image: ImageView = itemView.findViewById(R.id.dynamic_main_view_image)
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

        /**
         * Describes the data that should be put in the view holder object for the dynamic_options
         * What should happen in dynamic_options
         */
        inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var rankBtn: Button = itemView.findViewById(R.id.dynamic_options_rankBtn)
            private var editBtn: Button = itemView.findViewById(R.id.dynamic_options_editBtn)
            private var deleteBtn: Button = itemView.findViewById(R.id.dynamic_options_deleteBtn)
            fun setClickListeners(
                    adapterPosition: Int,
                    element: ObjectSet
            ) {
                rankBtn.setOnClickListener {
                    (activity as MainActivity).moveToRankSet()
                }

                editBtn.setOnClickListener {
                    MainViewModel.getSetData(element)
                    (activity as MainActivity).moveToEditFragment()
                }

                deleteBtn.setOnClickListener {
                    vm.removeDbDoc(element.id)
                }
            }
        }

    }


    /**
     * Adds swipe support to RecyclerView
     */
    inner class ItemTouchCallback(adapter: MainViewAdaptor) :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        private val background: ColorDrawable? =
                ColorDrawable(android.graphics.Color.rgb(1, 64, 23))

        override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            adapter?.showMenu(position)

        }

        override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(
                    c,
                    recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
            )
            val itemView = viewHolder.itemView
            when {
                dX > 0 -> {
                    background!!.setBounds(
                            itemView.left,
                            itemView.top,
                            itemView.left + dX.toInt(),
                            itemView.bottom
                    )
                }
                dX < 0 -> {
                    background!!.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                    )
                }
                else -> {
                    background!!.setBounds(0, 0, 0, 0)
                }
            }
            background.draw(c)
        }

    }

}



