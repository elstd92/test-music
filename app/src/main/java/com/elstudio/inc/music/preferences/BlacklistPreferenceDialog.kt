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

package com.elstudio.inc.music.preferences

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.elstudio.inc.appthemehelper.common.prefs.supportv7.ATEDialogPreference
import com.elstudio.inc.music.R
import com.elstudio.inc.music.dialogs.BlacklistFolderChooserDialog
import com.elstudio.inc.music.extensions.colorControlNormal
import com.elstudio.inc.music.providers.BlacklistStore
import com.elstudio.inc.music.util.PreferenceUtil
import java.io.File
import java.util.*

class BlacklistPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : ATEDialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        icon?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                colorControlNormal(context),
                SRC_IN
            )
    }
}

class BlacklistPreferenceDialog : DialogFragment(), BlacklistFolderChooserDialog.FolderCallback {
    companion object {
        fun newInstance(): BlacklistPreferenceDialog {
            return BlacklistPreferenceDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val chooserDialog =
            childFragmentManager.findFragmentByTag("FOLDER_CHOOSER") as BlacklistFolderChooserDialog?
        chooserDialog?.setCallback(this)
        refreshBlacklistData()
        return MaterialDialog(requireContext()).show {
            title(R.string.blacklist)
            cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
            positiveButton(android.R.string.ok) {
                dismiss()
            }
            neutralButton(text = getString(R.string.clear_action)) {
                MaterialDialog(requireContext()).show {
                    title(com.elstudio.inc.music.R.string.clear_blacklist)
                    message(com.elstudio.inc.music.R.string.do_you_want_to_clear_the_blacklist)
                    cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
                    positiveButton(com.elstudio.inc.music.R.string.clear_action) {
                        BlacklistStore.getInstance(requireContext()).clear()
                        refreshBlacklistData()
                    }
                    negativeButton(android.R.string.cancel)
                }
            }
            negativeButton(R.string.add_action) {
                val dialog = BlacklistFolderChooserDialog.create()
                dialog.setCallback(this@BlacklistPreferenceDialog)
                dialog.show(childFragmentManager, "FOLDER_CHOOSER")
            }
            listItems(items = paths, waitForPositiveButton = false) { _, _, text ->
                MaterialDialog(requireContext()).show {
                    cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
                    title(com.elstudio.inc.music.R.string.remove_from_blacklist)
                    message(
                        text = HtmlCompat.fromHtml(
                            getString(
                                R.string.do_you_want_to_remove_from_the_blacklist,
                                text
                            ),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    )
                    positiveButton(com.elstudio.inc.music.R.string.remove_action) {
                        BlacklistStore.getInstance(requireContext())
                            .removePath(File(text.toString()))
                        refreshBlacklistData()
                    }
                    negativeButton(android.R.string.cancel)
                }
            }
            noAutoDismiss()
        }
    }

    private lateinit var paths: ArrayList<String>

    private fun refreshBlacklistData() {
        this.paths = BlacklistStore.getInstance(requireContext()).paths
        val dialog = dialog as MaterialDialog?
        dialog?.listItems(items = paths)
    }

    override fun onFolderSelection(dialog: BlacklistFolderChooserDialog, folder: File) {
        BlacklistStore.getInstance(requireContext()).addPath(folder)
        refreshBlacklistData()
    }
}
