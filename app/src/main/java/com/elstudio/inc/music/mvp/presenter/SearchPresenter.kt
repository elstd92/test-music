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

package com.elstudio.inc.music.mvp.presenter

import com.elstudio.inc.music.Result.Error
import com.elstudio.inc.music.Result.Success
import com.elstudio.inc.music.mvp.BaseView
import com.elstudio.inc.music.mvp.Presenter
import com.elstudio.inc.music.mvp.PresenterImpl
import com.elstudio.inc.music.providers.interfaces.Repository
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Elstudio on 20/08/17.
 */

interface SearchView : BaseView {

    fun showData(data: MutableList<Any>)
}

interface SearchPresenter : Presenter<SearchView> {

    fun search(query: String?)

    class SearchPresenterImpl @Inject constructor(
        private val repository: Repository
    ) : PresenterImpl<SearchView>(), SearchPresenter, CoroutineScope {

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        private var job: Job = Job()

        override fun detachView() {
            super.detachView()
            job.cancel()
        }

        override fun search(query: String?) {
            launch {
                when (val result = repository.search(query)) {
                    is Success -> withContext(Dispatchers.Main) { view?.showData(result.data) }
                    is Error -> withContext(Dispatchers.Main) { view?.showEmptyView() }
                }
            }
        }
    }
}


