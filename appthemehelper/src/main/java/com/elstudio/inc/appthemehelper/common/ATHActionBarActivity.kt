package com.elstudio.inc.appthemehelper.common

import androidx.appcompat.widget.Toolbar

import com.elstudio.inc.appthemehelper.util.ToolbarContentTintHelper

class ATHActionBarActivity : ATHToolbarActivity() {

    override fun getATHToolbar(): Toolbar? {
        return ToolbarContentTintHelper.getSupportActionBarView(supportActionBar)
    }
}
