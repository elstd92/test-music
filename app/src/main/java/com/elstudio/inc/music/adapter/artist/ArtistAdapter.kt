package com.elstudio.inc.music.adapter.artist

import android.app.ActivityOptions
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.elstudio.inc.appthemehelper.util.ColorUtil
import com.elstudio.inc.appthemehelper.util.MaterialValueHelper
import com.elstudio.inc.music.R
import com.elstudio.inc.music.adapter.base.AbsMultiSelectAdapter
import com.elstudio.inc.music.adapter.base.MediaEntryViewHolder
import com.elstudio.inc.music.extensions.hide
import com.elstudio.inc.music.glide.ArtistGlideRequest
import com.elstudio.inc.music.glide.RetroMusicColoredTarget
import com.elstudio.inc.music.helper.menu.SongsMenuHelper
import com.elstudio.inc.music.interfaces.CabHolder
import com.elstudio.inc.music.model.Artist
import com.elstudio.inc.music.model.Song
import com.elstudio.inc.music.util.MusicUtil
import com.elstudio.inc.music.util.NavigationUtil
import me.zhanghai.android.fastscroll.PopupTextProvider
import java.util.*

class ArtistAdapter(
    val activity: AppCompatActivity,
    var dataSet: List<Artist>,
    var itemLayoutRes: Int,
    cabHolder: CabHolder?
) : AbsMultiSelectAdapter<ArtistAdapter.ViewHolder, Artist>(
    activity, cabHolder, R.menu.menu_media_selection
), PopupTextProvider {

    init {
        this.setHasStableIds(true)
    }

    fun swapDataSet(dataSet: List<Artist>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false)
        return createViewHolder(view)
    }

    private fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = dataSet[position]
        val isChecked = isChecked(artist)
        holder.itemView.isActivated = isChecked
        holder.title?.text = artist.name
        holder.text?.hide()
        loadArtistImage(artist, holder)
    }

    fun setColors(color: Int, holder: ViewHolder) {
        if (holder.paletteColorContainer != null) {
            holder.paletteColorContainer?.setBackgroundColor(color)
            holder.title?.setTextColor(
                MaterialValueHelper.getPrimaryTextColor(
                    activity, ColorUtil.isColorLight(
                        color
                    )
                )
            )
        }
        holder.imageContainerCard?.setCardBackgroundColor(color)
        holder.mask?.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun loadArtistImage(artist: Artist, holder: ViewHolder) {
        if (holder.image == null) {
            return
        }
        ArtistGlideRequest.Builder.from(Glide.with(activity), artist)
            .generatePalette(activity)
            .build()
            .into(object : RetroMusicColoredTarget(holder.image!!) {
                override fun onLoadCleared(placeholder: Drawable?) {
                    super.onLoadCleared(placeholder)
                    setColors(defaultFooterColor, holder)
                }

                override fun onColorReady(color: Int) {
                    setColors(color, holder)
                }
            })
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getIdentifier(position: Int): Artist? {
        return dataSet[position]
    }

    override fun getName(artist: Artist): String {
        return artist.name
    }

    override fun onMultipleItemAction(
        menuItem: MenuItem, selection: ArrayList<Artist>
    ) {
        SongsMenuHelper.handleMenuClick(activity, getSongList(selection), menuItem.itemId)
    }

    private fun getSongList(artists: List<Artist>): ArrayList<Song> {
        val songs = ArrayList<Song>()
        for (artist in artists) {
            songs.addAll(artist.songs) // maybe async in future?
        }
        return songs
    }

    override fun getPopupText(position: Int): String {
        return getSectionName(position)
    }

    private fun getSectionName(position: Int): String {
        return MusicUtil.getSectionName(dataSet[position].name)
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        init {
            setImageTransitionName(activity.getString(R.string.transition_artist_image))
            menu?.visibility = View.GONE
        }

        override fun onClick(v: View?) {
            super.onClick(v)
            if (isInQuickSelectMode) {
                toggleChecked(layoutPosition)
            } else {
                val activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    imageContainerCard ?: image,
                    "${activity.getString(R.string.transition_artist_image)}_${dataSet[layoutPosition].id}"
                )
                NavigationUtil.goToArtistOptions(
                    activity, dataSet[layoutPosition].id, activityOptions
                )
            }
        }

        override fun onLongClick(v: View?): Boolean {
            toggleChecked(layoutPosition)
            return super.onLongClick(v)
        }
    }
}
