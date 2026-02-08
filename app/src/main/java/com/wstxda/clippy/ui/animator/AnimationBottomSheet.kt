package com.wstxda.clippy.ui.animator

import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.wstxda.clippy.utils.Constants

object AnimationBottomSheet {

    fun beginDelayedTransition(container: ViewGroup, duration: Long = Constants.DEFAULT_DURATION) {
        val transition = TransitionSet().apply {
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(Fade())
            addTransition(ChangeBounds())
            this.duration = duration
            this.interpolator = FastOutSlowInInterpolator()
        }
        TransitionManager.beginDelayedTransition(container, transition)
    }
}