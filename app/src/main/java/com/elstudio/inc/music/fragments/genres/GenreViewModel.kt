package com.elstudio.inc.music.fragments.genres

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elstudio.inc.music.Result.Success
import com.elstudio.inc.music.model.Genre
import com.elstudio.inc.music.providers.RepositoryImpl
import kotlinx.coroutines.launch

class GenreViewModel(application: Application) : AndroidViewModel(application) {
    var genres = MutableLiveData<List<Genre>>()

    init {
        loadGenre()
    }

    fun loadGenre() = viewModelScope.launch {
        val result = RepositoryImpl(getApplication()).allGenres()
        if (result is Success) {
            genres.value = result.data
        } else {
            genres.value = listOf()
        }
    }
}