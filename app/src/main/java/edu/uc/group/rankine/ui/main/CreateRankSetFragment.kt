package edu.uc.group.rankine.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ElementObject
import kotlinx.android.synthetic.main.activity_create_rank_set.*
import kotlinx.android.synthetic.main.dynamic_elements.*


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
        vmFactory = activity?.let { MainViewModelFactory(it) }!!
        vm = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        rcyElements.hasFixedSize()
        rcyElements.layoutManager = LinearLayoutManager(context)
        rcyElements.itemAnimator = DefaultItemAnimator()
        rcyElements.adapter = ElementsAdapter(vm.objectSet.elements, R.layout.dynamic_elements)

        add_new_element_btn.setOnClickListener {
            vm.objectSet.elements.add(ElementObject(""))
            (rcyElements.adapter as ElementsAdapter).notifyDataSetChanged()
        }

        save_btn.setOnClickListener {/*
            val dynamicFieldUtil = DynamicFieldUtil(requireActivity())
            val getAllViewChildren = GetAllViewChildren()
            val scrollContainer =
                    requireActivity().findViewById<LinearLayout>(R.id.scroll_Container)
            val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(scrollContainer!!)*/
            when {
                name_edit_view.text.isBlank() -> Toast.makeText(context, "Fill Out Name Field", Toast.LENGTH_SHORT).show()
                vm.objectSet.elements.isEmpty() -> Toast.makeText(context, "Add at least 1 Element", Toast.LENGTH_SHORT).show()
                else -> {
                    var filled = true
                    vm.objectSet.elements.forEach {
                        if (it.element.isBlank()) filled = false
                    }
                    if (filled) {
                        vm.create(name_edit_view.text.toString())
                        (activity as MainActivity).moveToMain()
                    } else Toast.makeText(context, "Fill out all Element Names", Toast.LENGTH_SHORT).show()
                }
            }
        }

        back_btn.setOnClickListener {
            (activity as MainActivity).moveToMain()
        }

    }

    inner class ElementsAdapter(val elements: ArrayList<ElementObject>, val itemLayout: Int) : RecyclerView.Adapter<ElementsAdapter.ElementsViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
            return ElementsViewHolder(view, MyCustomEditTextListener())
        }

        override fun getItemCount(): Int {
            return elements.size
        }

        override fun onBindViewHolder(holder: ElementsViewHolder, position: Int) {
            val element = elements.get(getItemViewType(position))
            // update MyCustomEditTextListener every time we bind a new item
            // so that it knows what item in mDataset to update
            holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
            holder.updateElement(element)

            holder.btnDelete.setOnClickListener {
                elements.removeAt(getItemViewType(position))
                notifyItemRemoved(getItemViewType(position))
            }
        }

        override fun onViewAttachedToWindow(holder: ElementsViewHolder) {
            holder.enableTextWatcher()
            super.onViewAttachedToWindow(holder)
        }

        override fun onViewDetachedFromWindow(holder: ElementsViewHolder) {
            holder.disableTextWatcher()
            super.onViewDetachedFromWindow(holder)
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        inner class ElementsViewHolder(itemView: View, var myCustomEditTextListener: MyCustomEditTextListener) : RecyclerView.ViewHolder(itemView) {
            var btnDelete: ImageButton = itemView.findViewById(R.id.delete_attribute)
            var lblElementName: TextView = itemView.findViewById(R.id.lblElementName)

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
                elements.get(position).element = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {
                // no op
            }
        }

    }
}