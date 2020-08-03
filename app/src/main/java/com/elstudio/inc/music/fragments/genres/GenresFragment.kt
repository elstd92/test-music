/*
 * Copyright (c) 2019 El Studio.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.elstudio.inc.music.fragments.genres

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elstudio.inc.music.App
import com.elstudio.inc.music.R
import com.elstudio.inc.music.adapter.GenreAdapter
import com.elstudio.inc.music.fragments.base.AbsLibraryPagerRecyclerViewFragment
import com.elstudio.inc.music.interfaces.MainActivityFragmentCallbacks

class GenresFragment : AbsLibraryPagerRecyclerViewFragment<GenreAdapter, LinearLayoutManager>(),
    MainActivityFragmentCallbacks {

    lateinit var genreViewModel: GenreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.musicComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genreViewModel = ViewModelProvider(this).get(GenreViewModel::class.java)
        genreViewModel.genres.observe(viewLifecycleOwner, Observer { genres ->
            if (genres.isNotEmpty()) {
                adapter?.swapDataSet(genres)
            } else {
                adapter?.swapDataSet(listOf())
            }
        })
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun createLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }

    override fun createAdapter(): GenreAdapter {
        val dataSet = if (adapter == null) ArrayList() else adapter!!.dataSet
        return GenreAdapter(mainActivity, dataSet, R.layout.item_list_no_image)
    }

    override val emptyMessage: Int
        get() = R.string.no_genres

    override fun onMediaStoreChanged() {
        genreViewModel.loadGenre()
    }

    companion object {
        @JvmField
        val TAG: String = GenresFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): GenresFragment {
            return GenresFragment()
        }
    }
}