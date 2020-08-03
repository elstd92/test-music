package com.elstudio.inc.music.adapter.album

import android.app.ActivityOptions
import android.content.res.ColorStateList
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
import com.elstudio.inc.music.glide.AlbumGlideRequest
import com.elstudio.inc.music.glide.RetroMusicColoredTarget
import com.elstudio.inc.music.helper.MusicPlayerRemote
import com.elstudio.inc.music.helper.SortOrder
import com.elstudio.inc.music.helper.menu.SongsMenuHelper
import com.elstudio.inc.music.interfaces.CabHolder
import com.elstudio.inc.music.model.Album
import com.elstudio.inc.music.model.Song
import com.elstudio.inc.music.util.MusicUtil
import com.elstudio.inc.music.util.NavigationUtil
import com.elstudio.inc.music.util.PreferenceUtil
import me.zhanghai.android.fastscroll.PopupTextProvider

open class AlbumAdapter(
    protected val activity: AppCompatActivity,
    var dataSet: List<Album>,
    protected var itemLayoutRes: Int,
    cabHolder: CabHolder?
) : AbsMultiSelectAdapter<AlbumAdapter.ViewHolder, Album>(
    activity,
    cabHolder,
    R.menu.menu_media_selection
), PopupTextProvider {

    init {
        this.setHasStableIds(true)
    }

    fun swapDataSet(dataSet: List<Album>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false)
        return createViewHolder(view, viewType)
    }

    protected open fun createViewHolder(view: View, viewType: Int): ViewHolder {
        return ViewHolder(view)
    }

    private fun getAlbumTitle(album: Album): String? {
        return album.title
    }

    protected open fun getAlbumText(album: Album): String? {
        return album.artistName
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = dataSet[position]
        val isChecked = isChecked(album)
        holder.itemView.isActivated = isChecked
        holder.title?.text = getAlbumTitle(album)
        holder.text?.text = getAlbumText(album)
        holder.playSongs?.setOnClickListener {
            album.songs?.let { songs ->
                MusicPlayerRemote.openQueue(
                    songs,
                    0,
                    true
                )
            }
        }
        loadAlbumCover(album, holder)
    }

    protected open fun setColors(color: Int, holder: ViewHolder) {
        if (holder.paletteColorContainer != null) {
            holder.title?.setTextColor(
                MaterialValueHelper.getPrimaryTextColor(
                    activity,
                    ColorUtil.isColorLight(color)
                )
            )
            holder.text?.setTextColor(
                MaterialValueHelper.getSecondaryTextColor(
                    activity,
                    ColorUtil.isColorLight(color)
                )
            )
            holder.paletteColorContainer?.setBackgroundColor(color)
        }
        holder.mask?.backgroundTintList = ColorStateList.valueOf(color)
    }

    protected open fun loadAlbumCover(album: Album, holder: ViewHolder) {
        if (holder.image == null) {
            return
        }

        AlbumGlideRequest.Builder.from(Glide.with(activity), album.safeGetFirstSong())
            .checkIgnoreMediaStore(activity)
            .generatePalette(activity)
            .build()
            .into(object : RetroMusicColoredTarget(holder.image!!) {

                override fun onColorReady(color: Int) {
                    setColors(color, holder)
                }
            })
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id.toLong()
    }

    override fun getIdentifier(position: Int): Album? {
        return dataSet[position]
    }

    override fun getName(album: Album): String {
        return album.title!!
    }

    override fun onMultipleItemAction(
        menuItem: MenuItem, selection: ArrayList<Album>
    ) {
        SongsMenuHelper.handleMenuClick(activity, getSongList(selection), menuItem.itemId)
    }

    private fun getSongList(albums: List<Album>): ArrayList<Song> {
        val songs = ArrayList<Song>()
        for (album in albums) {
            songs.addAll(album.songs!!)
        }
        return songs
    }

    override fun getPopupText(position: Int): String {
        return getSectionName(position)
    }

    private fun getSectionName(position: Int): String {
        var sectionName: String? = null
        when (PreferenceUtil.getInstance(activity).albumSortOrder) {
            SortOrder.AlbumSortOrder.ALBUM_A_Z, SortOrder.AlbumSortOrder.ALBUM_Z_A -> sectionName =
                dataSet[position].title
            SortOrder.AlbumSortOrder.ALBUM_ARTIST -> sectionName = dataSet[position].artistName
            SortOrder.AlbumSortOrder.ALBUM_YEAR -> return MusicUtil.getYearString(
                dataSet[position].year
            )
        }

        return MusicUtil.getSectionName(sectionName)
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        init {
            setImageTransitionName(activity.getString(R.string.transition_album_art))
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
                    "${activity.getString(R.string.transition_album_art)}_${dataSet[layoutPosition].id}"
                )
                NavigationUtil.goToAlbumOptions(
                    activity,
                    dataSet[layoutPosition].id,
                    activityOptions
                )
            }
        }

        override fun onLongClick(v: View?): Boolean {
            toggleChecked(layoutPosition)
            return super.onLongClick(v)
        }
    }

    companion object {
        val TAG: String = AlbumAdapter::class.java.simpleName
    }
}
