[![Coverage Status](https://s3.amazonaws.com/assets.coveralls.io/badges/coveralls_19.svg)

# MovieRama

MovieRama is an android application, built with [Jetpack Compose][compose]. The application provides
information about movies &amp; television shows and other forms of entertainment. It includes
information such as cast and crew, plot summaries, user reviews, ratings, and more.

### Note

This application is built using Jetpack Compose, which is a modern UI toolkit for building native
Android apps. However, it is still in the early stages of development, and may result in some
performance issues, such as lagging, when running the debug version of the app. It is recommended to
use the release version for optimal performance.

## Getting Started

If you've found this repository for the first time, welcome! The application uses an MVVM
architecture which you can read about [here](documentation/Architecture.md). You can also find some
screenshots defining the Aesthetics & UI of the application [here](documentation/Screenshots.md)

The [documentation folder](documentation) can also serve as a guide to getting familiar with this
project. This can help build an understanding before you join a live stream - but if you have any
questions, just ask in chat.

## Hiding Sensitive Information

This app uses Firebase Remote Config to securely store and retrieve API keys, allowing for easy
updates and management of sensitive information without the need for hard coding or additional
configuration. This ensures that the app remains secure and in compliance with best practices for
handling sensitive data.
