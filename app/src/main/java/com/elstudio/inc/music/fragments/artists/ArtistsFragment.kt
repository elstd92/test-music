package com.elstudio.inc.music.fragments.artists

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.elstudio.inc.music.R
import com.elstudio.inc.music.adapter.artist.ArtistAdapter
import com.elstudio.inc.music.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import com.elstudio.inc.music.interfaces.MainActivityFragmentCallbacks
import com.elstudio.inc.music.util.PreferenceUtil

class ArtistsFragment :
    AbsLibraryPagerRecyclerViewCustomGridSizeFragment<ArtistAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {

    lateinit var artistViewModel: ArtistViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        artistViewModel = ViewModelProvider(this).get(ArtistViewModel::class.java)
        artistViewModel.artists.observe(viewLifecycleOwner, Observer { artists ->
            if (artists.isNotEmpty()) {
                adapter?.swapDataSet(artists)
            } else {
                adapter?.swapDataSet(listOf())
            }
        })
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override val emptyMessage: Int
        get() = R.string.no_artists

    override fun onMediaStoreChanged() {
        artistViewModel.loadArtists()
    }

    override fun setSortOrder(sortOrder: String) {
        artistViewModel.loadArtists()
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireActivity(), getGridSize())
    }

    override fun createAdapter(): ArtistAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return ArtistAdapter(
            mainActivity,
            dataSet,
            itemLayoutRes(),
            mainActivity
        )
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.getInstance(requireContext()).getArtistGridSize(requireActivity())
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setArtistGridSize(gridColumns)
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.getInstance(requireContext()).getArtistGridSizeLand(requireActivity())
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setArtistGridSizeLand(gridColumns)
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager?.spanCount = gridSize
        adapter?.notifyDataSetChanged()
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.getInstance(requireContext()).artistSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.getInstance(requireContext()).artistSortOrder = sortOrder
    }

    override fun setLayoutRes(layoutRes: Int) {
    }

    override fun loadLayoutRes(): Int {
        return PreferenceUtil.getInstance(requireContext()).artistGridStyle
    }

    override fun saveLayoutRes(layoutRes: Int) {
        PreferenceUtil.getInstance(requireContext()).artistGridStyle = layoutRes
    }

    companion object {
        @JvmField
        val TAG: String = ArtistsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): ArtistsFragment {
            return ArtistsFragment()
        }
    }
}