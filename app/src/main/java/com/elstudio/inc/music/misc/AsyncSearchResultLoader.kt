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

package com.elstudio.inc.music.misc

import android.content.Context
import android.text.TextUtils
import com.elstudio.inc.music.R
import com.elstudio.inc.music.loaders.AlbumLoader
import com.elstudio.inc.music.loaders.ArtistLoader
import com.elstudio.inc.music.loaders.SongLoader
import java.util.*

internal class AsyncSearchResultLoader(context: Context, private val query: String) :
    WrappedAsyncTaskLoader<List<Any>>(context) {

    override fun loadInBackground(): List<Any>? {
        val results = ArrayList<Any>()
        if (!TextUtils.isEmpty(query)) {
            val songs = SongLoader.getSongs(context, query.trim { it <= ' ' })
            if (!songs.isEmpty()) {
                results.add(context.resources.getString(R.string.songs))
                results.addAll(songs)
            }

            val artists = ArtistLoader.getArtists(context, query.trim { it <= ' ' })
            if (!artists.isEmpty()) {
                results.add(context.resources.getString(R.string.artists))
                results.addAll(artists)
            }

            val albums = AlbumLoader.getAlbums(context, query.trim { it <= ' ' })
            if (!albums.isEmpty()) {
                results.add(context.resources.getString(R.string.albums))
                results.addAll(albums)
            }
        }
        return results
    }
}