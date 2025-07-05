package com.nezuko.domain.repository

interface Navigation {
    var navigateFromMainToMdReader: (Int) -> Unit
    var navigateFromWriterToMdReader: (Int) -> Unit
    var navigateFromMainToMdWriter: (Int) -> Unit
    var navigateFromReaderToMdWriter: (Int) -> Unit
    var navigateBack: () -> Unit
}