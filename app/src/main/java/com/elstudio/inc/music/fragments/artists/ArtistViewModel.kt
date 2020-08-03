package com.elstudio.inc.music.fragments.artists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elstudio.inc.music.Result
import com.elstudio.inc.music.model.Artist
import com.elstudio.inc.music.providers.RepositoryImpl
import kotlinx.coroutines.launch

class ArtistViewModel(application: Application) : AndroidViewModel(application) {
    var artists = MutableLiveData<List<Artist>>()

    init {
        loadArtists()
    }

    fun loadArtists() = viewModelScope.launch {
        val result = RepositoryImpl(getApplication()).allArtists()
        if (result is Result.Success) {
            artists.value = result.data
        } else {
            artists.value = listOf()
        }
    }
}