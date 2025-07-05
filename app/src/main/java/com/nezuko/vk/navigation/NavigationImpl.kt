package com.nezuko.vk.navigation

import com.nezuko.domain.repository.Navigation
import javax.inject.Inject

class NavigationImpl @Inject constructor(
) : Navigation {
    override var navigateToMdReader: (Int) -> Unit = { println(123) }
    override var navigateToMdWriter: (Int) -> Unit = { println(123)  }
    override var navigateBack: () -> Unit = { println(123) }
}