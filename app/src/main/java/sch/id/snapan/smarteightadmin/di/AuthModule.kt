package sch.id.snapan.smarteightadmin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import sch.id.snapan.smarteightadmin.repositories.DefaultAuthRepository
import sch.id.snapan.smarteightadmin.repositories.base.AuthRepository

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @ViewModelScoped
    @Provides
    fun provideAuthRepository() = DefaultAuthRepository() as AuthRepository
}