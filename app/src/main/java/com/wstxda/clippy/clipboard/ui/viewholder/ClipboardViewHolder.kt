package com.wstxda.clippy.clipboard.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.listitem.ListItemViewHolder
import com.wstxda.clippy.clipboard.data.ClipboardCategory
import com.wstxda.clippy.clipboard.data.ClipboardLinkItem
import com.wstxda.clippy.databinding.ListItemClipboardLinkBinding

class ClipboardViewHolder private constructor(
    private val binding: ListItemClipboardLinkBinding,
    private val onCopy: (ClipboardLinkItem) -> Unit,
    private val onSave: (ClipboardLinkItem) -> Unit,
    private val onDelete: (ClipboardLinkItem) -> Unit
) : ListItemViewHolder(binding.root) {

    private var currentItem: ClipboardLinkItem? = null

    init {
        setupClickListeners()
    }

    fun bind(item: ClipboardLinkItem, position: Int, itemCount: Int) {
        super.bind()
        currentItem = item

        binding.linkText.text = item.url
        binding.listItemLayout.updateAppearance(position, itemCount)

        configureActions(item.category)
    }

    private fun configureActions(category: ClipboardCategory) {
        when (category) {
            ClipboardCategory.CURRENT -> {
                binding.saveButton.visibility = View.GONE
                binding.deleteButton.visibility = View.GONE
                binding.listItemCardView.isSwipeEnabled = false
            }

            ClipboardCategory.SAVED -> {
                binding.saveButton.visibility = View.GONE
                binding.deleteButton.visibility = View.VISIBLE
                binding.listItemCardView.isSwipeEnabled = true
            }

            ClipboardCategory.HISTORY -> {
                binding.saveButton.visibility = View.VISIBLE
                binding.deleteButton.visibility = View.VISIBLE
                binding.listItemCardView.isSwipeEnabled = true
            }
        }
    }

    private fun setupClickListeners() {
        binding.listItemCardView.setOnClickListener {
            currentItem?.let { onCopy(it) }
        }

        binding.saveButton.setOnClickListener {
            currentItem?.let { onSave(it) }
        }

        binding.deleteButton.setOnClickListener {
            currentItem?.let { onDelete(it) }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onCopy: (ClipboardLinkItem) -> Unit,
            onSave: (ClipboardLinkItem) -> Unit,
            onDelete: (ClipboardLinkItem) -> Unit
        ): ClipboardViewHolder {
            val binding = ListItemClipboardLinkBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ClipboardViewHolder(binding, onCopy, onSave, onDelete)
        }
    }
}