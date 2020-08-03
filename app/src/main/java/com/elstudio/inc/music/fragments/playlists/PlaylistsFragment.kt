package com.elstudio.inc.music.fragments.playlists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.elstudio.inc.music.R
import com.elstudio.inc.music.adapter.playlist.PlaylistAdapter
import com.elstudio.inc.music.fragments.base.AbsLibraryPagerRecyclerViewFragment
import com.elstudio.inc.music.interfaces.MainActivityFragmentCallbacks

class PlaylistsFragment :
    AbsLibraryPagerRecyclerViewFragment<PlaylistAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {

    lateinit var playlistViewModel: PlaylistViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)
        playlistViewModel.playlists.observe(viewLifecycleOwner, Observer { playlists ->
            if (playlists.isNotEmpty()) {
                adapter?.swapDataSet(playlists)
            } else {
                adapter?.swapDataSet(listOf())
            }
        })
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override val emptyMessage: Int
        get() = R.string.no_playlists

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireContext(), 1)
    }

    override fun createAdapter(): PlaylistAdapter {
        return PlaylistAdapter(
            mainActivity,
            ArrayList(),
            R.layout.item_list,
            mainActivity
        )
    }

    override fun onMediaStoreChanged() {
        super.onMediaStoreChanged()
        playlistViewModel.loadPlaylist()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.apply {
            removeItem(R.id.action_sort_order)
            removeItem(R.id.action_grid_size)
        }
    }

    companion object {
        @JvmField
        val TAG: String = PlaylistsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }
}
