package com.wstxda.clippy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.clippy.data.LinkItem
import com.wstxda.clippy.databinding.TextInputLinkContainerBinding

class LinkItemAdapter : ListAdapter<LinkItem, LinkItemAdapter.LinkViewHolder>(LinkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val binding = TextInputLinkContainerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LinkViewHolder(
        private val binding: TextInputLinkContainerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            setupReadOnlyFields()
        }

        private fun setupReadOnlyFields() {
            listOf(binding.inputOriginalLinkText, binding.inputCleanedLinkText).forEach {
                it.apply {
                    isFocusable = false
                    isClickable = false
                    isLongClickable = false
                    isCursorVisible = false
                }
            }
        }

        fun bind(item: LinkItem) {
            binding.inputOriginalLinkText.setText(item.inputUrl)

            val error = item.validationErrorRes?.let { binding.root.context.getString(it) }
                ?: item.validationError
            binding.inputOriginalLink.error = error
            binding.inputOriginalLink.isErrorEnabled = error != null

            binding.progressIndicatorModules.isVisible = item.isProcessing

            val hasResult = item.resultUrl != null
            binding.inputCleanedLink.isVisible = hasResult

            if (hasResult) {
                binding.inputCleanedLinkText.setText(item.resultUrl)
            }
        }
    }

    private class LinkDiffCallback : DiffUtil.ItemCallback<LinkItem>() {
        override fun areItemsTheSame(oldItem: LinkItem, newItem: LinkItem) =
            oldItem.originalUrl == newItem.originalUrl

        override fun areContentsTheSame(oldItem: LinkItem, newItem: LinkItem) = oldItem == newItem
    }
}