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

package com.elstudio.inc.music.dagger.module

import android.content.Context
import com.elstudio.inc.music.mvp.presenter.*
import com.elstudio.inc.music.mvp.presenter.AlbumDetailsPresenter.AlbumDetailsPresenterImpl
import com.elstudio.inc.music.mvp.presenter.AlbumsPresenter.AlbumsPresenterImpl
import com.elstudio.inc.music.mvp.presenter.ArtistDetailsPresenter.ArtistDetailsPresenterImpl
import com.elstudio.inc.music.mvp.presenter.ArtistsPresenter.ArtistsPresenterImpl
import com.elstudio.inc.music.mvp.presenter.GenreDetailsPresenter.GenreDetailsPresenterImpl
import com.elstudio.inc.music.mvp.presenter.GenresPresenter.GenresPresenterImpl
import com.elstudio.inc.music.mvp.presenter.HomePresenter.HomePresenterImpl
import com.elstudio.inc.music.mvp.presenter.PlaylistSongsPresenter.PlaylistSongsPresenterImpl
import com.elstudio.inc.music.mvp.presenter.PlaylistsPresenter.PlaylistsPresenterImpl
import com.elstudio.inc.music.mvp.presenter.SearchPresenter.SearchPresenterImpl
import com.elstudio.inc.music.mvp.presenter.SongPresenter.SongPresenterImpl
import com.elstudio.inc.music.providers.RepositoryImpl
import com.elstudio.inc.music.providers.interfaces.Repository
import dagger.Module
import dagger.Provides

/**
 * Created by Elstudio on 2019-12-30.
 */

@Module
class PresenterModule {

    @Provides
    fun providesRepository(context: Context): Repository {
        return RepositoryImpl(context)
    }

    @Provides
    fun providesAlbumsPresenter(presenter: AlbumsPresenterImpl): AlbumsPresenter {
        return presenter
    }

    @Provides
    fun providesAlbumDetailsPresenter(presenter: AlbumDetailsPresenterImpl): AlbumDetailsPresenter {
        return presenter
    }

    @Provides
    fun providesArtistDetailsPresenter(presenter: ArtistDetailsPresenterImpl): ArtistDetailsPresenter {
        return presenter
    }

    @Provides
    fun providesArtistsPresenter(presenter: ArtistsPresenterImpl): ArtistsPresenter {
        return presenter
    }

    @Provides
    fun providesGenresPresenter(presenter: GenresPresenterImpl): GenresPresenter {
        return presenter
    }

    @Provides
    fun providesGenreDetailsPresenter(presenter: GenreDetailsPresenterImpl): GenreDetailsPresenter {
        return presenter
    }

    @Provides
    fun providesHomePresenter(presenter: HomePresenterImpl): HomePresenter {
        return presenter
    }

    @Provides
    fun providesPlaylistSongPresenter(presenter: PlaylistSongsPresenterImpl): PlaylistSongsPresenter {
        return presenter
    }

    @Provides
    fun providesPlaylistsPresenter(presenter: PlaylistsPresenterImpl): PlaylistsPresenter {
        return presenter
    }

    @Provides
    fun providesSearchPresenter(presenter: SearchPresenterImpl): SearchPresenter {
        return presenter
    }

    @Provides
    fun providesSongPresenter(presenter: SongPresenterImpl): SongPresenter {
        return presenter
    }
}