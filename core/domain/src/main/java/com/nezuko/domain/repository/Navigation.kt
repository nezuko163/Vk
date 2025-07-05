package com.nezuko.domain.repository

interface Navigation {
    var navigateToMdReader: (Int) -> Unit
    var navigateToMdWriter: (Int) -> Unit
    var navigateBack: () -> Unit
}