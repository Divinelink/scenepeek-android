![Coverage](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/Divinelink/e1019c0e79b88046505566d98d96c710/raw/coverage.json)
<img src="https://github.com/Divinelink/scenepeek-android/actions/workflows/coverage_static_analysis.yml/badge.svg" alt="CI">
[![Join the community](https://img.shields.io/badge/matrix.org-join_community-teal?style=flat-square&logo=matrix)](https://matrix.to/#/#scenepeek:matrix.org)

<div align="center">
  <a href="https://f-droid.org/packages/com.divinelink.scenepeek/" target="_blank"><img src="https://f-droid.org/badge/get-it-on.png" height="75" alt="Get it on F-Droid"></a>
  <a href="https://play.google.com/store/apps/details?id=com.divinelink.scenepeek" target="_blank"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" height="75" alt="Get it on Google Play"></a>
</div>

<div align="center">
  <a href="https://apps.obtainium.imranr.dev/redirect?r=obtainium://add/https://github.com/Divinelink/scenepeek-android" target="_blank"><img alt='Get it on Obtainium' src='https://github.com/ImranR98/Obtainium/blob/main/assets/graphics/badge_obtainium.png' height='75'/></a>
  <a href="https://github.com/Divinelink/scenepeek-android/releases/latest" target="_blank"><img alt='Get it on GitHub' src='https://github.com/machiav3lli/oandbackupx/blob/034b226cea5c1b30eb4f6a6f313e4dadcbb0ece4/badge_github.png' height='75'/></a>
</div>

<div align="center">
  <p><strong>The iOS version is under review from Apple since 20/12/2025, until they decide to approve it and release it on the AppStore, you can get the iOS app on TestFlight.</strong></p>

<a href="https://testflight.apple.com/join/9bdUBYwY" target="_blank"><img alt='Get it on TestFlight' src='https://askyourself.app/assets/testflight.png' height='80'/></a>
</div>

<div align="center">

### Help translate the project!
[![Translation status](https://hosted.weblate.org/widget/scenepeek/287x66-grey.png)](https://hosted.weblate.org/engage/scenepeek/)

</div>

# ScenePeek

ScenePeek is an android application, built
with [Jetpack Compose](https://developer.android.com/compose). The application provides
information about movies &amp; television shows and other forms of entertainment. It includes
information such as cast and crew, plot summaries, user reviews, ratings, and more.

## Screenshots
|                                                                          Home                                                                           |                                                                     Details                                                                      |                                                                      Ratings                                                                      |                                                                            Lists                                                                            |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------:|
|        <img width="270" height="585" alt="Home Screen" src="https://github.com/user-attachments/assets/c2f72284-b133-4259-aa13-316bd105f05f" />         |   <img width="270" height="585" alt="Details Screen" src="https://github.com/user-attachments/assets/eea755e9-9d17-4dbb-9807-3e91395e3e28" />    |    <img width="270" height="585" alt="Ratings Screen" src="https://github.com/user-attachments/assets/c4210496-2b5a-4c5e-bc2a-4e42caad8919" />    |          <img width="270" height="585" alt="Lists Screen" src="https://github.com/user-attachments/assets/dfc789e5-04d3-4ad6-82ae-340431065347" />          |
|                                                                      Request Media                                                                      |                                                                   Edit Request                                                                   |                                                                   View Requests                                                                   |                                                                           Awards                                                                            |
| <img width="270" height="585" alt="Screenshot_20250927_182216" src="https://github.com/user-attachments/assets/ab884494-c983-40d0-bc28-aa54a44890ab" /> | <img width="270" height="585" alt="Edit Request Screen" src="https://github.com/user-attachments/assets/0e8b4293-253f-4633-bb71-c59d52614b6e" /> | <img width="270" height="585" alt="View Requests Screen" src="https://github.com/user-attachments/assets/27dd688a-a283-4e57-98ac-aa3fe2503873" /> | <img width="270" height="585" alt="Explore major award ceremonies" src="https://github.com/user-attachments/assets/af5da700-4b08-4b0e-a5ff-9afa4c6dc69b" /> ||

## Core Features

The following table outlines the main features of ScenePeek:

| Feature                   | Description                                                                                  | Status        |
|---------------------------|----------------------------------------------------------------------------------------------|---------------|
| TMDB Authentication       | Log in with TMDB account                                                                     | ✅ Implemented |
| Rate Movies & TV Shows    | Rate content directly through the app                                                        | ✅ Implemented |
| TMDB Watchlist            | Manage your TMDB watchlist                                                                   | ✅ Implemented |
| People Details            | View detailed information about cast and crew                                                | ✅ Implemented |
| Movie Details             | Comprehensive information about movies                                                       | ✅ Implemented |
| TV Show Details           | Detailed information about TV series                                                         | ✅ Implemented |
| Cast & Crew Information   | Explore the team behind movies and TV shows                                                  | ✅ Implemented |
| Seerr Authentication      | Log in to your Seerr account                                                                 | ✅ Implemented |
| Seerr Requests            | Request movies and TV shows via Seerr                                                        | ✅ Implemented |
| Additional Rating Sources | View IMDb & Trakt ratings for all content                                                    | ✅ Implemented |
| Discover Feed             | Discover media through advanced filtering                                                    | ✅ Implemented |
| TV Show Seasons           | Detailed information about individual seasons                                                | ✅ Implemented |
| Awards                    | Explore major film awards: **Oscars**, **Golden Globes**, **European Film Awards**, and more | ✅ Implemented |
| Request notifications     | Get notifications on media requests                                                          | 🚧 Planned    |

We are continuously working on improving and expanding these features to enhance the user
experience. Features marked as "Work in Progress" are actively being developed and will be available
in future updates.

## Getting Started

Welcome! The application uses an MVVM architecture which you can read
about [here](documentation/Architecture.md). The [documentation folder](documentation) can also
serve as a guide to getting familiar with this project.

This application is built with a thorough suite of unit and ui tests to ensure that all
functionality is working as intended. These tests are run automatically with every build, and are a
crucial part of our development process. They help us catch and fix bugs early on, and ensure that
new changes don't break existing functionality. By using unit and ui tests, we can have confidence
that the app is working as expected, and that it will continue to work correctly as we make updates
and improvements over time.

In addition to the tests, the development process includes a system of pull requests. By
reviewing the pull request history, you can get a detailed understanding of how the application was
implemented. This can be especially helpful if you're looking to learn more about how a specific
feature was developed or if you're trying to understand how the codebase is organized.

## Efficient Data Management

Our app implements a caching strategy to optimize performance and reduce API requests:

- We use [SqlDelight](https://cashapp.github.io/sqldelight/) to cache API responses locally. This
  allows for faster data retrieval and reduces the need for frequent network calls.
- By leveraging The Movie
  Database's ["changes" API](https://developer.themoviedb.org/reference/person-changes), we can
  update our local cache with only the data that has changed since our last request. This approach
  significantly decreases the number of API requests made, improving app performance and reducing
  server load.

This combination of local caching and selective updates ensures that our app remains responsive
while minimizing unnecessary network traffic.

## Caching Progress

The following table shows the current status of caching implementation for different data types:

| Data Type | Caching Implemented |
|-----------|---------------------|
| People    | ✅ Implemented       |
| Movie     | 🚧 Work in Progress |
| TV Shows  | 🚧 Work in Progress |

We are continuously working on improving our caching strategy to enhance app performance.

## API Key Security

We use [secrets-gradle-plugin](https://github.com/google/secrets-gradle-plugin) to keep our API
keys secure. This plugin allows us to store sensitive information like API keys in a local
properties file that is not committed to the repository.

### Setup

1. Create a `local.properties` file in the root project directory
2. Add your API keys to this file in the format: `PROPERTY_NAME=value`
3. In your app's `build.gradle`, reference these properties using `secrets.propertyName`
