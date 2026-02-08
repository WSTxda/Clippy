package com.wstxda.clippy.ui.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wstxda.clippy.R
import com.wstxda.clippy.cleaner.data.CleaningModule
import com.wstxda.clippy.cleaner.data.LinkActionResult
import com.wstxda.clippy.data.LinkProcessState
import com.wstxda.clippy.databinding.BottomSheetLinkItemBinding
import com.wstxda.clippy.ui.adapter.LinkItemAdapter
import com.wstxda.clippy.ui.animator.AnimationBottomSheet
import com.wstxda.clippy.utils.Constants
import com.wstxda.clippy.viewmodel.LinkCleanerViewModel
import kotlinx.coroutines.launch

class LinkCleanerBottomSheet : BaseBottomSheet<BottomSheetLinkItemBinding>() {

    private val viewModel: LinkCleanerViewModel by viewModels()
    private val linkAdapter by lazy { LinkItemAdapter() }
    private var wasFinishedWithToast: Boolean = false

    var onFinishedWithToast: (String?) -> Unit = {}

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        BottomSheetLinkItemBinding.inflate(inflater, container, false)

    override val topDivider: View get() = binding.topDivider
    override val bottomDivider: View get() = binding.bottomDivider
    override val titleTextView: TextView get() = binding.titleDialog
    override val titleResId: Int = R.string.app_name

    override fun setupViews(savedInstanceState: Bundle?) {
        val initialLinks = arguments?.getStringArrayList(Constants.ARG_LINKS) ?: emptyList()
        val cleanLinks = arguments?.getBoolean(Constants.ARG_CLEAN) ?: true
        viewModel.setInitialLinks(initialLinks)

        setupRecyclerView()
        setupUIElements(cleanLinks)
        observeState()
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isHideable = false
            isFitToContents = true
        }
    }

    private fun setupRecyclerView() {
        binding.textInputLink.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = linkAdapter
            itemAnimator = null
        }
    }

    private fun setupUIElements(clean: Boolean) {
        binding.buttonClearLink.isVisible = clean
        binding.listItemModules.root.isVisible = clean

        if (clean) {
            binding.listItemModules.switchApplyAll.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setCleanAllEnabled(isChecked)
            }
            setupModuleCheckboxes()
        }

        binding.buttonClearLink.setOnClickListener { viewModel.processLinks() }
        binding.buttonCopyLinkClipboard.setOnClickListener {
            handleActionResult(viewModel.getCopyResult())
        }
    }

    private fun setupModuleCheckboxes() {
        val checkboxMap = mapOf(
            binding.listItemModules.checkboxShorteners to CleaningModule.SHORTENERS,
            binding.listItemModules.checkboxTrackers to CleaningModule.TRACKERS,
            binding.listItemModules.checkboxRedirection to CleaningModule.REDIRECTION,
            binding.listItemModules.checkboxBuiltinRules to CleaningModule.BUILTIN
        )

        checkboxMap.forEach { (box, mod) ->
            box.setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateModuleSelection(mod, isChecked)
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    AnimationBottomSheet.beginDelayedTransition(binding.root as ViewGroup)
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: LinkProcessState) {
        linkAdapter.submitList(state.links)
        binding.buttonClearLink.isEnabled = !state.isGlobalProcessing

        with(binding.listItemModules) {
            if (switchApplyAll.isChecked != state.isCleanAllEnabled) {
                switchApplyAll.isChecked = state.isCleanAllEnabled
            }
            modulesContainer.isVisible = !state.isCleanAllEnabled

            val selected = state.selectedModules
            checkboxShorteners.isChecked = selected.contains(CleaningModule.SHORTENERS)
            checkboxTrackers.isChecked = selected.contains(CleaningModule.TRACKERS)
            checkboxRedirection.isChecked = selected.contains(CleaningModule.REDIRECTION)
            checkboxBuiltinRules.isChecked = selected.contains(CleaningModule.BUILTIN)
        }
    }

    private fun handleActionResult(result: LinkActionResult) {
        when (result) {
            is LinkActionResult.Success -> {
                copyToClipboard(result.text)
                finishWithToast(getString(R.string.copy_success))
            }
            is LinkActionResult.Error -> {
                finishWithToast(getString(result.errorRes))
            }
        }
    }

    private fun copyToClipboard(text: String) {
        requireContext().getSystemService<ClipboardManager>()?.setPrimaryClip(
            ClipData.newPlainText("link", text)
        )
    }

    private fun finishWithToast(message: String) {
        wasFinishedWithToast = true
        onFinishedWithToast(message)
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!wasFinishedWithToast) {
            onFinishedWithToast(null)
        }
    }

    override fun setupScrollListener() {
        binding.bottomSheetLinkItem.setOnScrollChangeListener { view, _, _, _, _ ->
            updateDividerVisibility(view.canScrollVertically(-1), view.canScrollVertically(1))
        }
    }
}