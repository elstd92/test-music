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

package com.elstudio.inc.music.providers.interfaces

import com.elstudio.inc.music.Result
import com.elstudio.inc.music.model.*
import com.elstudio.inc.music.rest.model.LastFmAlbum
import com.elstudio.inc.music.rest.model.LastFmArtist

/**
 * Created by Elstudio on 11/08/17.
 */

interface Repository {

    suspend fun allAlbums(): Result<ArrayList<Album>>

    suspend fun albumById(albumId: Int): Result<Album>

    suspend fun allSongs(): Result<ArrayList<Song>>

    suspend fun allArtists(): Result<ArrayList<Artist>>

    suspend fun allPlaylists(): Result<ArrayList<Playlist>>

    suspend fun allGenres(): Result<ArrayList<Genre>>

    suspend fun search(query: String?): Result<MutableList<Any>>

    suspend fun getPlaylistSongs(playlist: Playlist): Result<ArrayList<Song>>

    suspend fun getGenre(genreId: Int): Result<ArrayList<Song>>

    suspend fun recentArtists(): Result<Home>

    suspend fun topArtists(): Result<Home>

    suspend fun topAlbums(): Result<Home>

    suspend fun recentAlbums(): Result<Home>

    suspend fun favoritePlaylist(): Result<Home>

    suspend fun artistInfo(name: String, lang: String?, cache: String?): Result<LastFmArtist>

    suspend fun albumInfo(artist: String, album: String): Result<LastFmAlbum>

    suspend fun artistById(artistId: Int): Result<Artist>
}