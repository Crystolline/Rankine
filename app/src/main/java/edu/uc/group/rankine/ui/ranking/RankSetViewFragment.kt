package edu.uc.group.rankine.ui.ranking

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.RankedObjectSet
import edu.uc.group.rankine.ui.main.MainActivity
import edu.uc.group.rankine.ui.main.MainViewModel
import edu.uc.group.rankine.ui.main.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_rank_set_view.*
import java.io.File


class RankSetViewFragment : Fragment() {
    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory
    private var _rankSets = ArrayList<RankedObjectSet>()
    private var adapter: RankSetViewAdapter? = null

    companion object {
        fun newInstance() = RankSetViewFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rank_set_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recycle = activity?.findViewById<RecyclerView>(R.id.fragment_rank_set_view_recycle)
        vmFactory = activity?.let { MainViewModelFactory(it) }!!
        activity.let {
            vm = ViewModelProvider(it!!.viewModelStore, vmFactory).get(MainViewModel::class.java)
        }

        recycle?.layoutManager = LinearLayoutManager(context)
        recycle?.itemAnimator = DefaultItemAnimator()
        adapter = RankSetViewAdapter(_rankSets)
        recycle?.adapter = adapter
        val set = ItemTouchHelper(ItemTouchCallback(adapter!!))
        set.attachToRecyclerView(recycle!!)

        vm.rankSets.observe(viewLifecycleOwner, Observer {
            _rankSets.removeAll(_rankSets)
            _rankSets.addAll(it)
            recycle.adapter!!.notifyDataSetChanged()
        })

        btnMainView.setOnClickListener{
            (activity as MainActivity).moveToMain()
        }


    }

    /**
     * Defines the adapter being used to bind the views to their data
     */
    open inner class RankSetViewAdapter(
        private val ranks: ArrayList<RankedObjectSet>,
        private val SHOW_MENU: Int = 1,
        private val HIDE_MENU: Int = 2
    ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        /**
         * determines if the type of view based on if the menu is shown
         */
        override fun getItemViewType(position: Int): Int {
            return if (ranks[position].isShowMenu()) {
                SHOW_MENU
            } else {
                HIDE_MENU
            }
        }


        /**
         * Constructs the view that represents the ArrayList<RankedObjectSet>
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == SHOW_MENU) {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ranked_options, parent, false)
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
            return ranks.size
        }

        /**
         * Used to display items of the adapter
         */
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val rank = ranks[position]
            if (holder is MainViewHolder) {
                holder.updateElement(rank)
            }

            if (holder is MenuViewHolder) {
                holder.setClickListeners(holder.adapterPosition, rank)

            }

        }

        /**
         * sets the dto menu var to true at the swiped specified position
         * @param position the item position that is to be shown
         */
        fun showMenu(position: Int) {
            for (i in 0 until ranks.size) {
                ranks[i].setShowMenu(false)
            }
            ranks[position].setShowMenu(true)
            notifyDataSetChanged()
        }

        /**
         * determines if the menu is currently being shown.
         * @return true if menu is shown
         * @return false if menu is not shown
         */
        fun isMenuShown(): Boolean {
            for (i in 0 until ranks.size) {
                if (ranks[i].isShowMenu()) {
                    return true
                }
            }
            return false
        }

        /**
         * closes the menu by setting the dto menu var to false
         */
        fun closeMenu() {
            for (i in 0 until ranks.size) {
                ranks[i].setShowMenu(false)
            }
            notifyDataSetChanged()
        }

        /**
         * Describes the data that should be put in the view holder object for the dyanmic_main_view
         * What happens in dynamic_main_view
         */
        inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var name: TextView = itemView.findViewById(R.id.dynamic_name)
            private var image: ImageView = itemView.findViewById(R.id.dynamic_image)
            fun updateElement(rankedObjectSet: RankedObjectSet) {
                image.clipToOutline = true
                name.text = rankedObjectSet.set.name
                if (rankedObjectSet.set.localUri != "null" && rankedObjectSet.set.localUri != "" && File(
                        rankedObjectSet.set.localUri
                    ).exists()
                ) {
                    val uri = Uri.fromFile(File(rankedObjectSet.set.localUri))
                    val source = ImageDecoder.createSource(activity!!.contentResolver, uri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    image.setImageBitmap(bitmap)
                } else {

                    image.setImageResource(R.drawable.ic_launcher_foreground)
                }

            }
        }

        /**
         * Describes the data that should be put in the view holder object for the ranked_options
         * What should happen in ranked_options
         */
        inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var rankBtn: Button = itemView.findViewById(R.id.ranked_options_rankBtn)
            private var editBtn: Button = itemView.findViewById(R.id.ranked_options_editBtn)
            private var deleteBtn: Button = itemView.findViewById(R.id.ranked_options_deleteBtn)
            fun setClickListeners(
                adapterPosition: Int,
                rankedObjectSet: RankedObjectSet
            ) {
                rankBtn.setOnClickListener {
                    vm.rankSet = rankedObjectSet
                    (activity as MainActivity).moveToRankSet()
                }

                editBtn.setOnClickListener {
                    vm.rankSet = rankedObjectSet
                    (activity as MainActivity).moveToViewSelectedRankSetFragment()
                }

                deleteBtn.setOnClickListener {
                    vm.removeDbDoc(rankedObjectSet.id)
                }
            }
        }
    }


    /**
     * Adds swipe support to RecyclerView
     */
    inner class ItemTouchCallback(adapter: RankSetViewAdapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        private val background: ColorDrawable? =
            ColorDrawable(Color.rgb(1, 64, 23))

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        /**
         * What happens when the view is completely swiped
         */
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            if (direction == ItemTouchHelper.RIGHT) {
                adapter?.showMenu(position)
            } else {
                adapter?.closeMenu()
            }


        }

        /**
         * What is to be drawn while the view is being swiped
         */
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