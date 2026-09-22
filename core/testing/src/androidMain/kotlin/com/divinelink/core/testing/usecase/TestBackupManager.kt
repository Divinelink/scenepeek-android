package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.BackupManager
import com.divinelink.core.fixtures.model.FooException
import com.divinelink.core.model.export.FileData

class TestBackupManager : BackupManager {
  private var folderPath: String? = null
  private var silentSaveResult: Result<Boolean> = Result.failure(FooException())
  private var saveFileResult: Result<Boolean> = Result.failure(FooException())
  private var readFile: FileData? = null

  override suspend fun getFolderPath(): String? = folderPath

  override suspend fun silentSave(
    fileData: FileData,
    path: String,
  ): Result<Boolean> = silentSaveResult

  override suspend fun saveFile(fileData: FileData): Result<Boolean> = saveFileResult

  override suspend fun readFile(extensions: Set<String>?): FileData? = readFile

  fun mockFolderPath(path: String?) {
    folderPath = path
  }

  fun mockSilentSave(result: Result<Boolean>) {
    silentSaveResult = result
  }

  fun mockSaveFileResult(result: Result<Boolean>) {
    saveFileResult = result
  }

  fun mockReadFile(file: FileData?) {
    readFile = file
  }
}
