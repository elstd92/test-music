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

import com.elstudio.inc.music.Result.Success
import com.elstudio.inc.music.model.Album
import com.elstudio.inc.music.model.Artist
import com.elstudio.inc.music.mvp.Presenter
import com.elstudio.inc.music.mvp.PresenterImpl
import com.elstudio.inc.music.providers.interfaces.Repository
import com.elstudio.inc.music.rest.model.LastFmAlbum
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Elstudio on 20/08/17.
 */
interface AlbumDetailsView {

    fun album(album: Album)

    fun complete()

    fun loadArtistImage(artist: Artist)

    fun moreAlbums(albums: List<Album>)

    fun aboutAlbum(lastFmAlbum: LastFmAlbum)
}

interface AlbumDetailsPresenter : Presenter<AlbumDetailsView> {
    fun loadAlbum(albumId: Int)

    fun loadMore(artistId: Int)

    fun aboutAlbum(artist: String, album: String)

    class AlbumDetailsPresenterImpl @Inject constructor(
        private val repository: Repository
    ) : PresenterImpl<AlbumDetailsView>(), AlbumDetailsPresenter, CoroutineScope {

        private val job = Job()
        private lateinit var album: Album

        override fun loadMore(artistId: Int) {
            launch {
                when (val result = repository.artistById(artistId)) {
                    is Success -> withContext(Dispatchers.Main) { showArtistImage(result.data) }
                    is Error -> withContext(Dispatchers.Main) {}
                }
            }
        }

        override fun aboutAlbum(artist: String, album: String) {
            launch {
                when (val result = repository.albumInfo(artist, album)) {
                    is Success -> withContext(Dispatchers.Main) { view.aboutAlbum(result.data) }
                    is Error -> withContext(Dispatchers.Main) {}
                }
            }
        }

        private fun showArtistImage(artist: Artist) {
            view?.loadArtistImage(artist)

            artist.albums?.filter { it.id != album.id }?.let {
                if (it.isNotEmpty()) view?.moreAlbums(ArrayList(it))
            }
        }

        override fun loadAlbum(albumId: Int) {
            launch {
                when (val result = repository.albumById(albumId)) {
                    is Success -> withContext(Dispatchers.Main) {
                        album = result.data
                        view?.album(result.data)
                    }
                    is Error -> withContext(Dispatchers.Main) { view?.complete() }
                }
            }
        }

        override fun detachView() {
            super.detachView()
            job.cancel()
        }

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job
    }
}