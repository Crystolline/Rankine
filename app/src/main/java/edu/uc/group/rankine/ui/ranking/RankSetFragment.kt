package edu.uc.group.rankine.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.main.MainActivity
import edu.uc.group.rankine.ui.main.MainViewModel
import edu.uc.group.rankine.ui.main.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_rank_set.*

class RankSetFragment : Fragment() {
    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory

    companion object {
        fun newInstance() = RankSetFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_rank_set, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmFactory = activity?.let { MainViewModelFactory(it) }!!
        activity.let {
            vm = ViewModelProvider(it!!.viewModelStore, vmFactory).get(MainViewModel::class.java)
        }
        (rankSetToolbar as Toolbar).setNavigationOnClickListener { (activity as MainActivity).moveToMain() }
    }
}