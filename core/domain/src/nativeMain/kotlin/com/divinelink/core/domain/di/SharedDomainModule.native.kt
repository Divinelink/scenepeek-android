package com.divinelink.core.domain.di

import com.divinelink.core.domain.BackupManager
import com.divinelink.core.domain.NativeBackupManager
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val sharedDomainModule = module {
  singleOf(::NativeBackupManager) { bind<BackupManager>() }
}
