package com.divinelink.core.fixtures.details.review

import com.divinelink.core.fixtures.LoremIpsum
import com.divinelink.core.model.details.review.Review

object ReviewFactory {

  fun Review_1() = Review(
    author = AuthorFactory.eledriel(),
    rating = 10,
    content = LoremIpsum(100),
    date = "13-02-2017",
  )

  fun Review_2() = Review(
    author = AuthorFactory.andreas(),
    rating = 6,
    content = LoremIpsum(80),
    date = "13-02-2018",
  )

  fun Review_3() = Review(
    author = AuthorFactory.john(),
    rating = 9,
    content = LoremIpsum(60),
    date = "13-02-2019",
  )

  fun all() = listOf(
    Review_1(),
    Review_2(),
    Review_3(),
  )

  fun ReviewList(range: IntProgression = 1..10): List<Review> = (range).map {
    Review(
      author = AuthorFactory.empty().copy(name = "authorName $it"),
      rating = it,
      content = "content $it",
      date = "date $it",
    )
  }

  fun full() = Review(
    author = AuthorFactory.jeffrey(),
    rating = 10,
    content = "**Lorem ipsum dolor sit amet**, *consectetur adipiscing elit*." +
      "\r\n\r\nInteger sodales " +
      "laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu" +
      "elit ut nunc convallis laoreet non ut libero. Suspendisse interdum placerat" +
      "risus vel ornare. Donec vehicula, turpis sed consectetur ullamcorper, ante" +
      "nunc egestas quam, ultricies adipiscing velit enim at nunc. Aenean id diam" +
      "neque. Praesent ut lacus sed justo viverra fermentum et ut sem. \n Fusce" +
      "convallis gravida lacinia. Integer semper dolor ut elit sagittis lacinia." +
      "Praesent sodales scelerisque eros at rhoncus. Duis posuere sapien vel ipsum" +
      "ornare interdum at eu quam. Vestibulum vel massa erat. Aenean quis sagittis" +
      "purus. Phasellus arcu purus, rutrum id consectetur non, bibendum at nibh.",
    date = "2022-10-22",
  )

  fun empty() = Review(
    author = AuthorFactory.jeffrey(),
    rating = null,
    content = "",
    date = null,
  )
}
