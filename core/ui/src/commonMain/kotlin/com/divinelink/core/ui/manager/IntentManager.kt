package com.divinelink.core.ui.manager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.model.export.FileData
import io.github.vinceglb.filekit.PlatformFile

@Immutable
interface IntentManager {
  val packageName: String

  fun startActivity(uri: String): Boolean

  fun shareText(text: String)

  fun shareErrorReport(throwable: Throwable)

  fun launchEmail(
    email: String,
    subject: String?,
    body: String?,
  )

  fun navigateToAppSettings()

  suspend fun saveFile(fileData: FileData): Result<Boolean>

  suspend fun readFile(extensions: Set<String>?): FileData?
}

@Composable
expect fun rememberIntentManager(buildConfigProvider: BuildConfigProvider): IntentManager
