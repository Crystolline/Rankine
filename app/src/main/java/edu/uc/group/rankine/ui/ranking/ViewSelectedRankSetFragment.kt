package edu.uc.group.rankine.ui.ranking

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.RankedObjectSet
import edu.uc.group.rankine.ui.main.MainActivity
import edu.uc.group.rankine.ui.main.MainViewModel
import edu.uc.group.rankine.ui.main.MainViewModelFactory
import kotlinx.android.synthetic.main.view_selected_rank_set.*
import java.io.File

/**
 * A fragment for viewing the elements of a selected [RankedObjectSet], either ranked in order or using the order of the original set
 */
open class ViewSelectedRankSetFragment : Fragment() {

    companion object {
        fun newInstance() = ViewSelectedRankSetFragment()
    }

    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_selected_rank_set, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmFactory = activity?.let { MainViewModelFactory() }!!
        activity.let {
            vm = ViewModelProvider(it!!.viewModelStore, vmFactory).get(MainViewModel::class.java)
        }
        rcyRankedElements.hasFixedSize()
        rcyRankedElements.layoutManager = LinearLayoutManager(context)
        rcyRankedElements.itemAnimator = DefaultItemAnimator()
        //Will use rankedElements if fully ranked, and the original set if not ranked yet
        rcyRankedElements.adapter =
            RankedElementsAdapter(vm.rankSet.getCleanRankedElements(), R.layout.dynamic_elements)
        (toolbarCreateRankSet as Toolbar).navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_back_icon, null)
        (toolbarCreateRankSet as Toolbar).setNavigationOnClickListener { (activity as MainActivity).moveToRankedSetViewFragment() }

        // Resume the ranking of the current RankedSetFragment
        btn_Continue_Ranking.setOnClickListener {
            (activity as MainActivity).moveToRankSet()
        }
    }

    /**
     * Update the components of the ViewSelectedRankSetFragment
     */
    fun updateSelectedRankSetView() {
        val file = File(vm.rankSet.set.localUri)
        set_image.clipToOutline = true
        if (file.exists()) {
            set_image.setImageURI(Uri.parse(vm.rankSet.set.localUri))
        } else {
            set_image.setImageResource(R.drawable.ic_launcher_foreground)
        }
        name_view.text = vm.rankSet.set.name
        (rcyRankedElements.adapter as RankedElementsAdapter).notifyDataSetChanged()
    }

    inner class RankedElementsAdapter(
        private var elements: ArrayList<ElementObject>,
        private val itemLayout: Int
    ) :
        RecyclerView.Adapter<RankedElementsAdapter.RankedElementsViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RankedElementsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
            return RankedElementsViewHolder(view)
        }

        override fun getItemCount(): Int {
            return elements.size
        }

        override fun onBindViewHolder(holder: RankedElementsViewHolder, position: Int) {
            val element = elements[getItemViewType(position)]
            // update MyCustomEditTextListener every time we bind a new item
            // so that it knows what item in mDataset to update
            holder.updateElement(element)
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        inner class RankedElementsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private var lblElementName: TextView = itemView.findViewById(R.id.lblElementName)

            /**
             * This function will get called once for each item in the collection in the recyclerView
             * Paints a single row of the recyclerView with this event
             */
            fun updateElement(element: ElementObject) {
                lblElementName.text = element.element
            }
        }

    }
}