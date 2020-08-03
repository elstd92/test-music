package com.elstudio.inc.music.fragments.songs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elstudio.inc.music.Result.Success
import com.elstudio.inc.music.model.Song
import com.elstudio.inc.music.providers.RepositoryImpl
import kotlinx.coroutines.launch

class SongsViewModel(application: Application) : AndroidViewModel(application) {
    var songs = MutableLiveData<List<Song>>()

    init {
        loadSongs()
    }

    fun loadSongs() = viewModelScope.launch {
        val result = RepositoryImpl(getApplication()).allSongs()
        if (result is Success) {
            songs.value = result.data
        } else {
            songs.value = listOf()
        }
    }
}