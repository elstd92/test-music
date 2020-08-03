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

package com.elstudio.inc.music.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.module.GlideModule
import com.elstudio.inc.music.glide.artistimage.ArtistImage
import com.elstudio.inc.music.glide.artistimage.Factory
import com.elstudio.inc.music.glide.audiocover.AudioFileCover
import com.elstudio.inc.music.glide.audiocover.AudioFileCoverLoader
import java.io.InputStream

class RetroMusicGlideModule : GlideModule {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
    }

    override fun registerComponents(context: Context, glide: Glide) {
        glide.register(
            AudioFileCover::class.java,
            InputStream::class.java,
            AudioFileCoverLoader.Factory()
        )
        glide.register(ArtistImage::class.java, InputStream::class.java, Factory(context))
    }
}
