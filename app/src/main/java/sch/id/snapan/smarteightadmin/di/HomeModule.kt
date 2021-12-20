package sch.id.snapan.smarteightadmin.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import sch.id.snapan.smarteightadmin.repositories.DefaultAnnouncementRepository
import sch.id.snapan.smarteightadmin.repositories.DefaultAttendanceRepository
import sch.id.snapan.smarteightadmin.repositories.DefaultUserRepository
import sch.id.snapan.smarteightadmin.repositories.base.AnnouncementRepository
import sch.id.snapan.smarteightadmin.repositories.base.AttendanceRepository
import sch.id.snapan.smarteightadmin.repositories.base.UserRepository

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @ViewModelScoped
    @Provides
    fun provideAttendanceRepository() = DefaultAttendanceRepository() as AttendanceRepository

    @ViewModelScoped
    @Provides
    fun provideAnnouncementRepository() = DefaultAnnouncementRepository() as AnnouncementRepository

    @ViewModelScoped
    @Provides
    fun provideUserRepository() = DefaultUserRepository() as UserRepository
}