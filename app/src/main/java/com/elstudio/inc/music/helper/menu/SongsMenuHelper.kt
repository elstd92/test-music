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

package com.elstudio.inc.music.helper.menu

import androidx.fragment.app.FragmentActivity

import com.elstudio.inc.music.R
import com.elstudio.inc.music.dialogs.AddToPlaylistDialog
import com.elstudio.inc.music.dialogs.DeleteSongsDialog
import com.elstudio.inc.music.helper.MusicPlayerRemote
import com.elstudio.inc.music.model.Song
import java.util.*


object SongsMenuHelper {
    fun handleMenuClick(
        activity: FragmentActivity,
        songs: ArrayList<Song>,
        menuItemId: Int
    ): Boolean {
        when (menuItemId) {
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(songs)
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(songs)
                return true
            }
            R.id.action_add_to_playlist -> {
                AddToPlaylistDialog.create(songs)
                    .show(activity.supportFragmentManager, "ADD_PLAYLIST")
                return true
            }
            R.id.action_delete_from_device -> {
                DeleteSongsDialog.create(songs)
                    .show(activity.supportFragmentManager, "DELETE_SONGS")
                return true
            }
        }
        return false
    }
}
