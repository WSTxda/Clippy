package com.wstxda.clippy.ui.utils

import com.wstxda.clippy.R
import com.wstxda.clippy.data.TutorialItem

object TutorialItemList {
    fun getTutorialItems() = listOf(

        // Share menu tutorial
        TutorialItem(
            iconRes = R.drawable.ic_share,
            imageRes = R.drawable.tutorial_share_card,
            titleRes = R.string.tutorial_share_menu,
            summaryRes = R.string.tutorial_share_menu_summary

            // Select text tutorial
        ), TutorialItem(
            iconRes = R.drawable.ic_text_select,
            imageRes = R.drawable.tutorial_text_share_card,
            titleRes = R.string.tutorial_select_text_menu,
            summaryRes = R.string.tutorial_select_text_summary
        )
    )
}