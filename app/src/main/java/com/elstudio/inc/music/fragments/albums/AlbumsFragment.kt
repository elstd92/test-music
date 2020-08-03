package com.elstudio.inc.music.fragments.albums

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.elstudio.inc.music.R
import com.elstudio.inc.music.adapter.album.AlbumAdapter
import com.elstudio.inc.music.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import com.elstudio.inc.music.interfaces.MainActivityFragmentCallbacks
import com.elstudio.inc.music.util.PreferenceUtil

class AlbumsFragment :
    AbsLibraryPagerRecyclerViewCustomGridSizeFragment<AlbumAdapter, GridLayoutManager>(),
    MainActivityFragmentCallbacks {

    lateinit var albumViewModel: AlbumViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumViewModel = ViewModelProvider(this).get(AlbumViewModel::class.java)
        albumViewModel.albums.observe(viewLifecycleOwner, Observer { albums ->
            if (albums.isNotEmpty())
                adapter?.swapDataSet(albums)
            else
                adapter?.swapDataSet(listOf())
        })
    }

    override val emptyMessage: Int
        get() = R.string.no_albums

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireActivity(), getGridSize())
    }

    override fun createAdapter(): AlbumAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return AlbumAdapter(
            mainActivity,
            dataSet,
            itemLayoutRes(),
            mainActivity
        )
    }

    override fun setGridSize(gridSize: Int) {
        layoutManager?.spanCount = gridSize
        adapter?.notifyDataSetChanged()
    }

    override fun loadSortOrder(): String {
        return PreferenceUtil.getInstance(requireContext()).albumSortOrder
    }

    override fun saveSortOrder(sortOrder: String) {
        PreferenceUtil.getInstance(requireContext()).albumSortOrder = sortOrder
    }

    override fun loadGridSize(): Int {
        return PreferenceUtil.getInstance(requireContext()).getAlbumGridSize(requireContext())
    }

    override fun saveGridSize(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setAlbumGridSize(gridColumns)
    }

    override fun loadGridSizeLand(): Int {
        return PreferenceUtil.getInstance(requireContext()).getAlbumGridSizeLand(requireContext())
    }

    override fun saveGridSizeLand(gridColumns: Int) {
        PreferenceUtil.getInstance(requireContext()).setAlbumGridSizeLand(gridColumns)
    }

    override fun onMediaStoreChanged() {
        albumViewModel.getAlbums()
    }

    override fun setSortOrder(sortOrder: String) {
        albumViewModel.getAlbums()
    }

    override fun setLayoutRes(layoutRes: Int) {
    }

    override fun loadLayoutRes(): Int {
        return PreferenceUtil.getInstance(requireContext()).albumGridStyle
    }

    override fun saveLayoutRes(layoutRes: Int) {
        PreferenceUtil.getInstance(requireContext()).albumGridStyle = layoutRes
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    companion object {
        @JvmField
        var TAG: String = AlbumsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): AlbumsFragment {
            return AlbumsFragment()
        }
    }
}
