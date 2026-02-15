package com.wstxda.clippy.clipboard.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.wstxda.clippy.clipboard.data.ClipboardLinkItem
import com.wstxda.clippy.clipboard.ui.viewholder.ClipboardViewHolder

class ClipboardAdapter(

    private val onCopy: (ClipboardLinkItem) -> Unit,
    private val onSave: (ClipboardLinkItem) -> Unit,
    private val onDelete: (ClipboardLinkItem) -> Unit
) : ListAdapter<ClipboardLinkItem, ClipboardViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClipboardViewHolder {
        return ClipboardViewHolder.create(parent, onCopy, onSave, onDelete)
    }

    override fun onBindViewHolder(holder: ClipboardViewHolder, position: Int) {
        holder.bind(getItem(position), position, itemCount)
    }

    class DiffCallback : DiffUtil.ItemCallback<ClipboardLinkItem>() {
        override fun areItemsTheSame(
            oldItem: ClipboardLinkItem, newItem: ClipboardLinkItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ClipboardLinkItem, newItem: ClipboardLinkItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}