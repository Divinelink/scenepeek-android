package com.divinelink.core.domain

import com.divinelink.core.model.export.FileData
import io.github.vinceglb.filekit.AndroidFile
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.bookmarkData
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readString
import io.github.vinceglb.filekit.writeString
import kotlin.io.encoding.Base64

internal class AndroidBackupManager : BackupManager {
  override suspend fun saveFile(fileData: FileData): Result<Boolean> = runCatching {
    val file = FileKit.openFileSaver(
      suggestedName = fileData.name,
      defaultExtension = fileData.extension,
    )

    file?.writeString(fileData.content)
    file != null
  }

  override suspend fun readFile(extensions: Set<String>?): FileData? {
    val openFile = FileKit
      .openFilePicker(FileKitType.File(extensions = extensions))
      ?.androidFile

    return when (openFile) {
      is AndroidFile.FileWrapper -> FileData(
        extension = openFile.file.extension,
        name = openFile.file.name,
        content = openFile.file.readText(),
      )
      is AndroidFile.UriWrapper -> {
        val path = openFile.uri

        val file = PlatformFile(path)
        val content = file.readString()

        FileData(
          extension = file.extension,
          name = file.name,
          content = content,
        )
      }
      null -> null
    }
  }

  override suspend fun getFolderPath(): String? {
    val directory = FileKit.openDirectoryPicker() ?: return null
    val bookmark = directory.bookmarkData()
    return Base64.encode(bookmark.bytes)
  }

  override suspend fun silentSave(
    fileData: FileData,
    path: String,
  ): Result<Boolean> = writeToSavedDirectory(
    path = path,
    fileData = fileData,
  )
}
