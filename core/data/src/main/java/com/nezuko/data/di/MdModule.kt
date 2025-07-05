package com.nezuko.data.di

import com.nezuko.data.impl.MdParserRepositoryImpl
import com.nezuko.data.md.MdRendererImpl
import com.nezuko.data.md.render.ImageViewRender
import com.nezuko.data.md.render.Render
import com.nezuko.data.md.render.SpacerViewRender
import com.nezuko.data.md.render.TableViewRender
import com.nezuko.data.md.render.TextViewRender
import com.nezuko.data.md.utils.ImageLoader
import com.nezuko.data.md.utils.ImageLoaderImpl
import com.nezuko.data.md.utils.TextStyler
import com.nezuko.data.md.utils.TextStylerImpl
import com.nezuko.domain.repository.md.MdParserRepository
import com.nezuko.domain.repository.md.MdRenderer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MdModule {

    @Binds
    @Singleton
    abstract fun bindImageLoader(
        impl: ImageLoaderImpl
    ): ImageLoader

    @Binds
    @Singleton
    abstract fun bindTextStyler(
        impl: TextStylerImpl
    ): TextStyler

    @Binds
    @Singleton
    abstract fun bindMdRenderer(
        impl: MdRendererImpl
    ): MdRenderer

    @Binds
    @Singleton
    abstract fun bindMdParser(
        impl: MdParserRepositoryImpl
    ): MdParserRepository
}
