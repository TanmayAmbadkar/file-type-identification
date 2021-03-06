package com.supercilex.robotscouter.feature.scouts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import com.supercilex.robotscouter.TeamSelectionListener
import com.supercilex.robotscouter.core.ValueSeeker
import com.supercilex.robotscouter.core.data.toBundle
import com.supercilex.robotscouter.core.model.Team
import com.supercilex.robotscouter.core.ui.animatePopReveal
import com.supercilex.robotscouter.core.ui.isInTabletMode
import org.jetbrains.anko.findOptional
import com.supercilex.robotscouter.R as RC

internal class TabletScoutListFragment : ScoutListFragmentBase() {
    private val noContentHint by ValueSeeker {
        requireActivity().findOptional<View>(RC.id.noTeamSelectedHint)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = requireActivity()
        if (!activity.isInTabletMode()) {
            val bundle = bundle
            removeFragment()
            (activity as TeamSelectionListener).onTeamSelected(bundle)
        }
    }

    override fun newViewModel() = object : AppBarViewHolderBase(
            this@TabletScoutListFragment,
            dataHolder.teamListener
    ) {
        init {
            toolbar.setOnMenuItemClickListener {
                // We need to be able to guarantee that our `onOptionsItemSelected`s are called
                // before that of TeamListActivity because of duplicate menu item ids
                forceRecursiveMenuItemSelection(it) ||
                        requireActivity().onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataHolder.teamListener.observe(viewLifecycleOwner) { select(it) }
        noContentHint?.animatePopReveal(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.integrated_scout_list_menu, viewHolder.toolbar.menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_move_window) {
            startActivity(ScoutListActivity.createIntent(bundle))
            removeFragment()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        select(null)
        noContentHint?.animatePopReveal(true)
    }

    override fun onTeamDeleted() = removeFragment()

    private fun select(team: Team?) {
        requireActivity().supportFragmentManager.fragments
                .filterIsInstance<TeamSelectionListener>()
                .firstOrNull()
                ?.onTeamSelected(team?.toBundle() ?: Bundle.EMPTY)
    }

    private fun removeFragment() {
        val parent = requireParentFragment()
        parent.requireFragmentManager().commit { remove(parent) }
    }

    companion object {
        const val TAG = "TabletScoutListFrag"

        fun newInstance(args: Bundle) = TabletScoutListFragment().apply { arguments = args }
    }
}
