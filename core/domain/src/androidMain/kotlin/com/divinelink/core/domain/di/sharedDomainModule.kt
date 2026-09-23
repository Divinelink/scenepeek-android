package com.divinelink.core.domain.di

import com.divinelink.core.domain.AndroidBackupManager
import com.divinelink.core.domain.BackupManager
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val sharedDomainModule = module {
  singleOf(::AndroidBackupManager) { bind<BackupManager>() }
}
