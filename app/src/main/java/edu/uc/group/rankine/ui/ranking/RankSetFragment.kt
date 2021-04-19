package edu.uc.group.rankine.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.main.MainActivity

class RankSetFragment : Fragment() {

    companion object {
        fun newInstance() = RankSetFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rank_set_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.rankSetToolbar)


        toolbar?.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_back_icon, null)
        toolbar!!.setNavigationOnClickListener { (activity as MainActivity).moveToMain() }
    }
}