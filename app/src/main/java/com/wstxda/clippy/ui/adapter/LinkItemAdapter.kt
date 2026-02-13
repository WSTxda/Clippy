package com.wstxda.clippy.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.clippy.data.LinkItem
import com.wstxda.clippy.databinding.TextInputLinkContainerBinding
import androidx.core.net.toUri

class LinkItemAdapter : ListAdapter<LinkItem, LinkItemAdapter.LinkViewHolder>(LinkDiffCallback()) {

    var showCleanedLink: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val binding = TextInputLinkContainerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(getItem(position), showCleanedLink)
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

        fun bind(item: LinkItem, showCleanedLink: Boolean) {
            binding.inputOriginalLinkText.setText(item.inputUrl)

            val inputError = item.validationErrorRes?.let { binding.root.context.getString(it) }
            binding.inputOriginalLink.error = inputError
            binding.inputOriginalLink.isErrorEnabled = inputError != null

            binding.inputOriginalLink.setEndIconOnClickListener {
                openInBrowser(item.inputUrl)
            }

            binding.progressIndicatorModules.isVisible = item.isProcessing

            val hasResult = item.resultUrl != null
            binding.inputCleanedLink.isVisible = hasResult && showCleanedLink

            if (hasResult && showCleanedLink) {
                binding.inputCleanedLinkText.setText(item.resultUrl)
                binding.inputCleanedLink.error = null
                binding.inputCleanedLink.isErrorEnabled = false

                binding.inputCleanedLink.setEndIconOnClickListener {
                    openInBrowser(item.resultUrl)
                }
            }
        }

        private fun openInBrowser(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            binding.root.context.startActivity(intent)
        }
    }

    private class LinkDiffCallback : DiffUtil.ItemCallback<LinkItem>() {
        override fun areItemsTheSame(oldItem: LinkItem, newItem: LinkItem) =
            oldItem.inputUrl == newItem.inputUrl

        override fun areContentsTheSame(oldItem: LinkItem, newItem: LinkItem): Boolean {
            return oldItem.resultUrl == newItem.resultUrl && oldItem.isProcessing == newItem.isProcessing && oldItem.validationErrorRes == newItem.validationErrorRes
        }
    }
}