package com.divinelink.core.domain

import com.divinelink.core.model.export.FileData
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.bookmarkData
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.nameWithoutExtension
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlin.io.encoding.Base64

internal class NativeBackupManager : BackupManager {

  override suspend fun saveFile(fileData: FileData): Result<Boolean> = runCatching {
    val file = FileKit.openFileSaver(
      suggestedName = fileData.name,
      defaultExtension = fileData.extension,
    )

    file?.writeString(fileData.content)
    file != null
  }

  override suspend fun readFile(extensions: Set<String>?): FileData? {
    val openFile = FileKit.openFilePicker(
      FileKitType.File(extensions = extensions),
    )
    if (openFile == null) return null

    return FileData(
      extension = openFile.extension,
      name = openFile.nameWithoutExtension,
      content = openFile.readString(),
    )
  }

  override suspend fun getFolderPath(): String? {
    val directory = FileKit.openDirectoryPicker() ?: return null
    val bookmark = directory.bookmarkData()
    return Base64.encode(bookmark.bytes)
  }

  override suspend fun silentSave(
    fileData: FileData,
    path: String,
  ): Result<Boolean> = writeToSavedDirectory(fileData, path)
}
