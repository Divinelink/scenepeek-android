package com.divinelink.core.domain

import com.divinelink.core.model.export.FileData
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.resolveBookmarkData
import io.github.vinceglb.filekit.writeString
import kotlin.io.encoding.Base64

interface BackupManager {
  suspend fun getFolderPath(): String?
  suspend fun silentSave(
    fileData: FileData,
    path: String,
  ): Result<Boolean>

  suspend fun saveFile(fileData: FileData): Result<Boolean>
  suspend fun readFile(extensions: Set<String>?): FileData?
}

suspend fun writeToSavedDirectory(
  fileData: FileData,
  path: String,
): Result<Boolean> = getSavedDirectory(path).fold(
  onSuccess = { directory ->
    val file = directory / "${fileData.name}.${fileData.extension}"
    file.writeString(fileData.content)
    Result.success(true)
  },
  onFailure = { Result.failure(it) },
)

private fun getSavedDirectory(cachedPath: String): Result<PlatformFile> = runCatching {
  val bytes = Base64.decode(cachedPath)
  val resolution = PlatformFile.resolveBookmarkData(bytes)
  val restored = resolution.file

  if (resolution.shouldRefresh || resolution.isStale) {
    error("Should refresh directory")
  }

  if (restored.exists()) {
    restored
  } else {
    error("Couldn't find directory")
  }
}
