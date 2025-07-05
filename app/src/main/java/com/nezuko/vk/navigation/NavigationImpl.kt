package com.nezuko.vk.navigation

import com.nezuko.domain.repository.Navigation
import javax.inject.Inject

class NavigationImpl @Inject constructor(
) : Navigation {
    override var navigateFromMainToMdReader: (Int) -> Unit = { println(123) }
    override var navigateFromReaderToMdWriter: (Int) -> Unit = { println(123) }
    override var navigateFromWriterToMdReader: (Int) -> Unit = { println(123) }
    override var navigateFromMainToMdWriter: (Int) -> Unit = { println(123)  }
    override var navigateBack: () -> Unit = { println(123) }
}