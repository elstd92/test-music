package com.elstudio.inc.music.fragments.base

import android.os.Bundle
import com.elstudio.inc.music.activities.MainActivity

open class AbsLibraryPagerFragment : AbsMusicServiceFragment() {

    val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }
}
