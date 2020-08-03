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

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.elstudio.inc.appthemehelper.util.ATHUtil
import com.elstudio.inc.music.R
import com.elstudio.inc.music.glide.palette.BitmapPaletteTarget
import com.elstudio.inc.music.glide.palette.BitmapPaletteWrapper
import com.elstudio.inc.music.util.PreferenceUtil
import com.elstudio.inc.music.util.RetroColorUtil


abstract class RetroMusicColoredTarget(view: ImageView) : BitmapPaletteTarget(view) {

    protected val defaultFooterColor: Int
        get() = ATHUtil.resolveColor(getView().context, R.attr.colorControlNormal)

    protected val albumArtistFooterColor: Int
        get() = ATHUtil.resolveColor(getView().context, R.attr.colorControlNormal)

    abstract fun onColorReady(color: Int)

    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
        super.onLoadFailed(e, errorDrawable)
        onColorReady(defaultFooterColor)
    }

    override fun onResourceReady(
        resource: BitmapPaletteWrapper?,
        glideAnimation: GlideAnimation<in BitmapPaletteWrapper>?
    ) {
        super.onResourceReady(resource, glideAnimation)
        val defaultColor = defaultFooterColor

        resource?.let {
            onColorReady(
                if (PreferenceUtil.getInstance(getView().context).isDominantColor)
                    RetroColorUtil.getDominantColor(it.bitmap, defaultColor)
                else
                    RetroColorUtil.getColor(it.palette, defaultColor)
            )
        }
    }
}
