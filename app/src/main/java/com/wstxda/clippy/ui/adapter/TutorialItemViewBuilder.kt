package com.wstxda.clippy.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.wstxda.clippy.data.TutorialItem
import com.wstxda.clippy.databinding.ListItemTutorialBinding

object TutorialItemViewBuilder {

    fun build(context: Context, item: TutorialItem): View {
        val binding = ListItemTutorialBinding.inflate(LayoutInflater.from(context))
        binding.tutorialImage.setImageResource(item.imageRes)
        binding.tutorialTitle.setText(item.titleRes)
        binding.tutorialTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(item.iconRes, 0, 0, 0)
        binding.tutorialSummary.setText(item.summaryRes)
        return binding.root
    }
}