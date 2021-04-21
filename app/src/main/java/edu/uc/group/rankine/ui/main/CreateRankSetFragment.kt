package edu.uc.group.rankine.ui.main

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_create_rank_set.*
import kotlinx.android.synthetic.main.dynamic_elements.*
import java.io.File


open class CreateRankSetFragment : Fragment() {
    companion object {
        fun newInstance() = CreateRankSetFragment()
    }

    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_create_rank_set, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmFactory = activity?.let { MainViewModelFactory() }!!
        activity.let {
            vm = ViewModelProvider(it!!.viewModelStore, vmFactory).get(MainViewModel::class.java)
        }
        rcyElements.hasFixedSize()
        rcyElements.layoutManager = LinearLayoutManager(context)
        rcyElements.itemAnimator = DefaultItemAnimator()
        rcyElements.adapter = ElementsAdapter(vm.objectSet.elements, R.layout.dynamic_elements)
        (toolbarCreateRankSet as Toolbar).navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_back_icon, null)
        (toolbarCreateRankSet as Toolbar).setNavigationOnClickListener { (activity as MainActivity).moveToMain() }

        // Add a new empty element field to the objectSet
        add_new_element_btn.setOnClickListener {
            vm.objectSet.elements.add(ElementObject(""))
            (rcyElements.adapter as ElementsAdapter).notifyDataSetChanged()
        }

        // Starts a new ranking with the current objectSet and moves to the RankSetFragment for the new ranking
        btn_Create_Ranking.setOnClickListener {
            with(vm.objectSet) {
                this.name = lblElementName.text.toString()
                localUri = MainViewModel.imageUriString
            }
            vm.rankSet = RankedObjectSet()
            vm.rankSet.set = vm.objectSet
            vm.rankSet.startRanking()
            (activity as MainActivity).moveToRankSet()
        }

        // Save the ObjectSet being created if the name field and at least 1 element field is filled out
        save_btn.setOnClickListener {
            when {
                name_edit_view.text.isBlank() -> Toast.makeText(
                    context,
                    "Fill Out Name Field",
                    Toast.LENGTH_SHORT
                ).show()
                vm.objectSet.elements.isEmpty() -> Toast.makeText(
                    context,
                    "Add at least 1 Element",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    var filled = true
                    vm.objectSet.elements.forEach {
                        if (it.element.isBlank()) filled = false
                    }
                    if (filled) {
                        vm.saveSet(name_edit_view.text.toString())
                        (activity as MainActivity).moveToMain()
                    } else Toast.makeText(context, "Fill out all Element Names", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    /**
     * updates the CreateRankSetFragment's components with the viewModel's current objectSet
     */
    fun updateCreateRankSetView() {
        val file = File(vm.objectSet.localUri)
        set_image.clipToOutline = true
        if (file.exists()) {
            set_image.setImageURI(Uri.parse(vm.objectSet.localUri))
        } else {
            set_image.setImageResource(R.drawable.ic_launcher_foreground)
        }
        name_edit_view.setText(vm.objectSet.name)
        (rcyElements.adapter as ElementsAdapter).notifyDataSetChanged()
    }

    inner class ElementsAdapter(
        var elements: ArrayList<ElementObject>,
        private val itemLayout: Int
    ) :
        RecyclerView.Adapter<ElementsAdapter.ElementsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
            return ElementsViewHolder(view, MyCustomEditTextListener())
        }

        override fun getItemCount(): Int {
            return elements.size
        }

        override fun onBindViewHolder(holder: ElementsViewHolder, position: Int) {
            val element = elements[getItemViewType(position)]
            // update MyCustomEditTextListener every time we bind a new item
            // so that it knows what item in mDataset to update
            holder.myCustomEditTextListener.updatePosition(holder.adapterPosition)
            holder.updateElement(element)

            //Remove the element when the delete button is pressed
            holder.btnDelete.setOnClickListener {
                elements.removeAt(getItemViewType(position))
                notifyItemRemoved(getItemViewType(position))
            }
        }

        /**
         * Enable the TextWatcher when the fragment is active
         */
        override fun onViewAttachedToWindow(holder: ElementsViewHolder) {
            holder.enableTextWatcher()
            super.onViewAttachedToWindow(holder)
        }

        /**
         * Disable the TextWatcher when the fragment is inactive
         */
        override fun onViewDetachedFromWindow(holder: ElementsViewHolder) {
            holder.disableTextWatcher()
            super.onViewDetachedFromWindow(holder)
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        inner class ElementsViewHolder(
            itemView: View,
            var myCustomEditTextListener: MyCustomEditTextListener
        ) : RecyclerView.ViewHolder(itemView) {
            var btnDelete: ImageButton = itemView.findViewById(R.id.delete_attribute)
            private var lblElementName: TextView = itemView.findViewById(R.id.lblElementName)

            /**
             * This function will get called once for each item in the collection in the recyclerView
             * Paints a single row of the recyclerView with this event
             */
            fun updateElement(element: ElementObject) {
                lblElementName.text = element.element
            }

            fun enableTextWatcher() {
                lblElementName.addTextChangedListener(myCustomEditTextListener)
            }

            fun disableTextWatcher() {
                lblElementName.removeTextChangedListener(myCustomEditTextListener)
            }
        }

        // we make TextWatcher to be aware of the position it currently works with
        // this way, once a new item is attached in onBindViewHolder, it will
        // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
        inner class MyCustomEditTextListener : TextWatcher {
            private var position = 0
            fun updatePosition(position: Int) {
                this.position = position
            }

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                // no op
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {
                elements[position].element = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {
                // no op
            }
        }

    }
}