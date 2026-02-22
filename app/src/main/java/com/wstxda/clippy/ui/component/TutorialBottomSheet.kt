package com.wstxda.clippy.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.wstxda.clippy.R
import com.wstxda.clippy.databinding.BottomSheetTutorialBinding
import com.wstxda.clippy.fragment.TutorialFragment
import com.wstxda.clippy.utils.Constants

class TutorialBottomSheet : BaseBottomSheet<BottomSheetTutorialBinding>(),
    TutorialFragment.ScrollListener {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        BottomSheetTutorialBinding.inflate(inflater, container, false)

    override val topDivider: View get() = binding.dividerTop
    override val bottomDivider: View get() = binding.dividerBottom
    override val titleTextView: TextView get() = binding.bottomSheetTitle
    override val titleResId: Int get() = R.string.tutorial_title

    override fun setupContentFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(binding.tutorialFragmentContainer.id, TutorialFragment()).commit()
        }
    }

    override fun onScrollChanged(canScrollUp: Boolean, canScrollDown: Boolean) {
        updateDividerVisibility(canScrollUp, canScrollDown)
    }

    companion object {
        fun show(fragmentManager: FragmentManager) {
            TutorialBottomSheet().show(fragmentManager, Constants.TUTORIAL_BOTTOM_SHEET)
        }
    }
}