package edu.uc.group.rankine.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
import edu.uc.group.rankine.ui.main.MainActivity
import edu.uc.group.rankine.ui.main.MainViewModel
import edu.uc.group.rankine.ui.main.MainViewModelFactory
import edu.uc.group.rankine.dto.RankedObjectSet
import kotlinx.android.synthetic.main.activity_rank_set.*

/**
 * A fragment used for ranking [RankedObjectSet]s
 */
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
        vmFactory = activity?.let { MainViewModelFactory() }!!
        activity.let {
            vm = ViewModelProvider(it!!.viewModelStore, vmFactory).get(MainViewModel::class.java)
        }
        (rankSetToolbar as Toolbar).navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_back_icon, null)
        (rankSetToolbar as Toolbar).setNavigationOnClickListener { (activity as MainActivity).moveToViewSelectedRankSetFragment() }
        updateRankSetView()

        // Save the current state of the ranking set
        btnSave.setOnClickListener {
            vm.saveRankToFirebase(vm.rankSet)
            (activity as MainActivity).moveToMain()
        }

        // Select the ElementObject on the left
        btnFirstElement.setOnClickListener {
            vm.rankSet.sortStep(false)
            updateRankSetView()
        }

        // Select the ElementObject on the right
        btnSecondElement.setOnClickListener {
            vm.rankSet.sortStep(true)
            updateRankSetView()
        }
    }

    /**
     * Updates the components for the RankSetFragment with data from the ViewModel's rankSet
     */
    fun updateRankSetView() {
        if (vm.rankSet.isRanking()) {
            txtFirstElement.text = vm.rankSet.leftElement.element
            txtSecondElement.text = vm.rankSet.rightElement.element
            btnFirstElement.isEnabled = true
            btnSecondElement.isEnabled = true
        } else {
            txtFirstElement.text = getString(R.string.RANKING)
            txtSecondElement.text = getString(R.string.COMPLETED)
            btnFirstElement.isEnabled = false
            btnSecondElement.isEnabled = false
        }
    }
}