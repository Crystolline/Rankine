package edu.uc.group.rankine

import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.dto.RankedObjectSet
import org.junit.Test

class RankingSetTest {
    lateinit var workingObjectSet: ObjectSet
    lateinit var rankedSet: RankedObjectSet

    fun rankSet() {
        givenSet()
        whenRankSet()
        thenSetRanked()
    }

    private fun thenSetRanked() {
        assert(rankedSet.getRankings(1) == ElementObject("1st Rank"))
        assert(rankedSet.getRankings(1) == ElementObject("2nd Rank"))
        assert(rankedSet.getRankings(1) == ElementObject("3rd Rank"))
        assert(rankedSet.getRankings(1) == ElementObject("4th Rank"))
        assert(rankedSet.isRanked())
    }

    private fun whenRankSet() {
        rankedSet = RankedObjectSet(workingObjectSet)
        rankedSet.getRankedElements()

    }


    private fun givenSet() {
        workingObjectSet = ObjectSet()
        with(workingObjectSet)
        {
            addElement(ElementObject("4th Rank"))
            addElement(ElementObject("3rd Rank"))
            addElement(ElementObject("2nd Rank"))
            addElement(ElementObject("1st Rank"))


        }
    }


}