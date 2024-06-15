package com.divinelink.watchlist

import androidx.lifecycle.ViewModel
import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.model.media.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
  repository: AccountRepository
) : ViewModel() {

  fun onMediaClick(media: MediaItem.Media) {
    // TODO
  }
}
