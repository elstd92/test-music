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

package com.elstudio.inc.music.appwidgets

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.elstudio.inc.appthemehelper.util.MaterialValueHelper
import com.elstudio.inc.music.R
import com.elstudio.inc.music.activities.MainActivity
import com.elstudio.inc.music.appwidgets.base.BaseAppWidget
import com.elstudio.inc.music.glide.SongGlideRequest
import com.elstudio.inc.music.service.MusicService
import com.elstudio.inc.music.service.MusicService.*
import com.elstudio.inc.music.util.RetroUtil

class AppWidgetBig : BaseAppWidget() {
    private var target: Target<Bitmap>? = null // for cancellation

    /**
     * Initialize given widgets to default state, where we launch Music on default click and hide
     * actions if service not running.
     */
    override fun defaultAppWidget(context: Context, appWidgetIds: IntArray) {
        val appWidgetView = RemoteViews(
            context.packageName, com.elstudio.inc.music.R.layout.app_widget_big
        )

        appWidgetView.setViewVisibility(
            com.elstudio.inc.music.R.id.media_titles,
            View.INVISIBLE
        )
        appWidgetView.setImageViewResource(R.id.image, R.drawable.default_audio_art)
        appWidgetView.setImageViewBitmap(
            R.id.button_next, BaseAppWidget.createBitmap(
                RetroUtil.getTintedVectorDrawable(
                    context,
                    com.elstudio.inc.music.R.drawable.ic_skip_next_white_24dp,
                    MaterialValueHelper.getPrimaryTextColor(context, false)
                )!!, 1f
            )
        )
        appWidgetView.setImageViewBitmap(
            R.id.button_prev, BaseAppWidget.Companion.createBitmap(
                RetroUtil.getTintedVectorDrawable(
                    context,
                    com.elstudio.inc.music.R.drawable.ic_skip_previous_white_24dp,
                    MaterialValueHelper.getPrimaryTextColor(context, false)
                )!!, 1f
            )
        )
        appWidgetView.setImageViewBitmap(
            R.id.button_toggle_play_pause, BaseAppWidget.Companion.createBitmap(
                RetroUtil.getTintedVectorDrawable(
                    context,
                    com.elstudio.inc.music.R.drawable.ic_play_arrow_white_32dp,
                    MaterialValueHelper.getPrimaryTextColor(context, false)
                )!!, 1f
            )
        )

        linkButtons(context, appWidgetView)
        pushUpdate(context, appWidgetIds, appWidgetView)
    }

    /**
     * Update all active widget instances by pushing changes
     */
    override fun performUpdate(service: MusicService, appWidgetIds: IntArray?) {
        val appWidgetView = RemoteViews(
            service.packageName, com.elstudio.inc.music.R.layout.app_widget_big
        )

        val isPlaying = service.isPlaying
        val song = service.currentSong

        // Set the titles and artwork
        if (TextUtils.isEmpty(song.title) && TextUtils.isEmpty(song.artistName)) {
            appWidgetView.setViewVisibility(
                com.elstudio.inc.music.R.id.media_titles,
                View.INVISIBLE
            )
        } else {
            appWidgetView.setViewVisibility(
                com.elstudio.inc.music.R.id.media_titles,
                View.VISIBLE
            )
            appWidgetView.setTextViewText(com.elstudio.inc.music.R.id.title, song.title)
            appWidgetView.setTextViewText(
                com.elstudio.inc.music.R.id.text,
                getSongArtistAndAlbum(song)
            )
        }

        // Set correct drawable for pause state
        val playPauseRes =
            if (isPlaying) com.elstudio.inc.music.R.drawable.ic_pause_white_24dp else com.elstudio.inc.music.R.drawable.ic_play_arrow_white_32dp
        appWidgetView.setImageViewBitmap(
            R.id.button_toggle_play_pause, BaseAppWidget.createBitmap(
                RetroUtil.getTintedVectorDrawable(
                    service,
                    playPauseRes,
                    MaterialValueHelper.getPrimaryTextColor(service, false)
                )!!, 1f
            )
        )

        // Set prev/next button drawables
        appWidgetView.setImageViewBitmap(
            R.id.button_next, BaseAppWidget.Companion.createBitmap(
                RetroUtil.getTintedVectorDrawable(
                    service,
                    com.elstudio.inc.music.R.drawable.ic_skip_next_white_24dp,
                    MaterialValueHelper.getPrimaryTextColor(service, false)
                )!!, 1f
            )
        )
        appWidgetView.setImageViewBitmap(
            R.id.button_prev, BaseAppWidget.Companion.createBitmap(
                RetroUtil.getTintedVectorDrawable(
                    service,
                    com.elstudio.inc.music.R.drawable.ic_skip_previous_white_24dp,
                    MaterialValueHelper.getPrimaryTextColor(service, false)
                )!!, 1f
            )
        )

        // Link actions buttons to intents
        linkButtons(service, appWidgetView)

        // Load the album cover async and push the update on completion
        val p = RetroUtil.getScreenSize(service)
        val widgetImageSize = Math.min(p.x, p.y)
        val appContext = service.applicationContext
        service.runOnUiThread {
            if (target != null) {
                Glide.clear(target)
            }
            target = SongGlideRequest.Builder.from(Glide.with(appContext), song)
                .checkIgnoreMediaStore(appContext).asBitmap().build()
                .into(object : SimpleTarget<Bitmap>(widgetImageSize, widgetImageSize) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        glideAnimation: GlideAnimation<in Bitmap>
                    ) {
                        update(resource)
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        update(null)
                    }

                    private fun update(bitmap: Bitmap?) {
                        if (bitmap == null) {
                            appWidgetView.setImageViewResource(
                                R.id.image,
                                R.drawable.default_audio_art
                            )
                        } else {
                            appWidgetView.setImageViewBitmap(R.id.image, bitmap)
                        }
                        pushUpdate(appContext, appWidgetIds, appWidgetView)
                    }
                })
        }
    }

    /**
     * Link up various button actions using [PendingIntent].
     */
    private fun linkButtons(context: Context, views: RemoteViews) {
        val action = Intent(context, MainActivity::class.java).putExtra("expand", true)
        var pendingIntent: PendingIntent

        val serviceName = ComponentName(context, MusicService::class.java)

        // Home
        action.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        pendingIntent = PendingIntent.getActivity(context, 0, action, 0)
        views.setOnClickPendingIntent(R.id.clickable_area, pendingIntent)

        // Previous track
        pendingIntent = buildPendingIntent(context, ACTION_REWIND, serviceName)
        views.setOnClickPendingIntent(R.id.button_prev, pendingIntent)

        // Play and pause
        pendingIntent = buildPendingIntent(context, ACTION_TOGGLE_PAUSE, serviceName)
        views.setOnClickPendingIntent(R.id.button_toggle_play_pause, pendingIntent)

        // Next track
        pendingIntent = buildPendingIntent(context, ACTION_SKIP, serviceName)
        views.setOnClickPendingIntent(R.id.button_next, pendingIntent)


    }

    companion object {

        const val NAME: String = "app_widget_big"
        private var mInstance: AppWidgetBig? = null

        val instance: AppWidgetBig
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = AppWidgetBig()
                }
                return mInstance!!
            }

    }
}
