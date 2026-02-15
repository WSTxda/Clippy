package com.wstxda.clippy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.wstxda.clippy.R
import com.wstxda.clippy.clipboard.ui.adapter.ClipboardAdapter
import com.wstxda.clippy.databinding.FragmentClipboardBinding
import com.wstxda.clippy.viewmodel.ClipboardViewModel
import kotlinx.coroutines.launch

class ClipboardFragment : Fragment() {

    private var _binding: FragmentClipboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClipboardViewModel by activityViewModels()

    private lateinit var currentAdapter: ClipboardAdapter
    private lateinit var savedAdapter: ClipboardAdapter
    private lateinit var historyAdapter: ClipboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClipboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupButtons()
        observeViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    private fun setupRecyclerViews() {
        // Current clipboard RecyclerView
        currentAdapter = ClipboardAdapter(onCopy = { item ->
            viewModel.copyToClipboard(item.url)
            showToast(getString(R.string.copy_success_clipboard))
        }, onSave = { }, onDelete = { })

        binding.currentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currentAdapter
            setHasFixedSize(true)
        }

        // Saved links RecyclerView
        savedAdapter = ClipboardAdapter(onCopy = { item ->
            viewModel.copyToClipboard(item.url)
            showToast(getString(R.string.copy_success_clipboard))
        }, onSave = { }, onDelete = { item ->
            viewModel.deleteFromSaved(item)
            showToast(getString(R.string.clipboard_link_deleted_message))
        })

        binding.savedRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = savedAdapter
            setHasFixedSize(true)
        }

        // History RecyclerView
        historyAdapter = ClipboardAdapter(onCopy = { item ->
            viewModel.copyToClipboard(item.url)
            showToast(getString(R.string.copy_success_clipboard))
        }, onSave = { item ->
            viewModel.saveLink(item)
            showToast(getString(R.string.clipboard_link_saved_message))
        }, onDelete = { item ->
            viewModel.deleteFromHistory(item)
            showToast(getString(R.string.clipboard_link_deleted_message))
        })

        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupButtons() {
        binding.deleteHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            showToast(getString(R.string.clipboard_history_cleared_message))
        }

        binding.clearClipboardButton.setOnClickListener {
            viewModel.clearClipboard()
            showToast(getString(R.string.clipboard_cleared_message))
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe current clipboard
                launch {
                    viewModel.currentClipboard.collect { current ->
                        val items = current?.let { listOf(it) } ?: emptyList()
                        binding.currentCategory.isVisible = items.isNotEmpty()
                        currentAdapter.submitList(items) {
                            updateEmptyState()
                        }
                    }
                }

                // Observe saved links
                launch {
                    viewModel.savedLinks.collect { saved ->
                        binding.savedCategory.isVisible = saved.isNotEmpty()
                        savedAdapter.submitList(saved) {
                            updateEmptyState()
                        }
                    }
                }

                // Observe history
                launch {
                    viewModel.historyLinks.collect { history ->
                        binding.historyCategory.isVisible = history.isNotEmpty()
                        historyAdapter.submitList(history) {
                            updateEmptyState()
                        }
                    }
                }
            }
        }
    }

    private fun updateEmptyState() {
        val hasAnyContent =
            binding.currentCategory.isVisible || binding.savedCategory.isVisible || binding.historyCategory.isVisible

        binding.emptyStateLayout.isVisible = !hasAnyContent
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}