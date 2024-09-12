package com.divinelink.core.testing.factories.model.change

import com.divinelink.core.model.change.Change
import com.divinelink.core.model.change.ChangeType
import com.divinelink.core.model.change.Changes

object ChangeSample {

  fun biography() = Change(
    key = ChangeType.BIOGRAPHY,
    items = PersonChangesSample.biography(),
  )

  fun tiktokId() = Change(
    key = ChangeType.TIKTOD_ID,
    items = PersonChangesSample.tiktokId(),
  )

  fun alsoKnownAs() = Change(
    key = ChangeType.ALSO_KNOWN_AS,
    items = PersonChangesSample.alsoKnownAs(),
  )

  fun youtubeId() = Change(
    key = ChangeType.YOUTUBE_ID,
    items = PersonChangesSample.youtubeId(),
  )

  fun facebookId() = Change(
    key = ChangeType.FACEBOOK_ID,
    items = PersonChangesSample.facebookId(),
  )

  fun name() = Change(
    key = ChangeType.NAME,
    items = PersonChangesSample.name(),
  )

  fun primary() = Change(
    key = ChangeType.PRIMARY,
    items = PersonChangesSample.primary(),
  )

  fun imdbId() = Change(
    key = ChangeType.IMDB_ID,
    items = PersonChangesSample.imdbId(),
  )

  fun instagramId() = Change(
    key = ChangeType.INSTAGRAM_ID,
    items = PersonChangesSample.instagramId(),
  )

  fun twitterId() = Change(
    key = ChangeType.TWITTER_ID,
    items = PersonChangesSample.twitterId(),
  )

  fun birthday() = Change(
    key = ChangeType.BIRTHDAY,
    items = PersonChangesSample.birthday(),
  )

  fun placeOfBirth() = Change(
    key = ChangeType.PLACE_OF_BIRTH,
    items = PersonChangesSample.placeOfBirth(),
  )

  fun gender() = Change(
    key = ChangeType.GENDER,
    items = PersonChangesSample.gender(),
  )

  fun changes() = Changes(
    listOf(
      biography(),
      tiktokId(),
      alsoKnownAs(),
      youtubeId(),
      facebookId(),
      name(),
      primary(),
      imdbId(),
      instagramId(),
      twitterId(),
      birthday(),
      placeOfBirth(),
      gender(),
    ),
  )
}
