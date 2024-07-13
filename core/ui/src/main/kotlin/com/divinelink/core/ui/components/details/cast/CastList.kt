package com.divinelink.core.ui.components.details.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.R

@Composable
fun CastList(cast: List<Person>) {
  Column(
    modifier = Modifier
      .padding(top = MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    if (cast.isNotEmpty()) {
      Text(
        modifier = Modifier
          .padding(horizontal = MaterialTheme.dimensions.keyline_12),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        text = stringResource(id = R.string.details__cast_title),
      )

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = ListPaddingValues,
      ) {
        items(
          items = cast,
          key = { it.id },
        ) {
          CreditsItemCard(person = it)
        }
      }
    }
  }
}
