package com.wstxda.clippy.data

import com.wstxda.clippy.cleaner.data.CleaningModule

data class LinkProcessState(

    val links: List<LinkItem> = emptyList(),
    val isCleanAllEnabled: Boolean = true,
    val selectedModules: Set<CleaningModule> = CleaningModule.entries.toSet(),
    val isGlobalProcessing: Boolean = false,
    val isFinished: Boolean = false
)