[![codecov](https://codecov.io/gh/Divinelink/movierama-android/graph/badge.svg?token=FPANRF2HZ5)](https://codecov.io/gh/Divinelink/movierama-android)
<img src="https://github.com/Divinelink/movierama-android/actions/workflows/coverage_static_analysis.yml/badge.svg" alt="CI">

# MovieRama

MovieRama is an android application, built
with [Jetpack Compose](https://developer.android.com/compose). The application provides
information about movies &amp; television shows and other forms of entertainment. It includes
information such as cast and crew, plot summaries, user reviews, ratings, and more.

## Core Features

The following table outlines the main features of our app:

| Feature                   | Description                                   | Status              |
|---------------------------|-----------------------------------------------|---------------------|
| TMDB Authentication       | Log in with TMDB account                      | ✅ Implemented       |
| Rate Movies & TV Shows    | Rate content directly through the app         | ✅ Implemented       |
| TMDB Watchlist            | Manage your TMDB watchlist                    | ✅ Implemented       |
| People Details            | View detailed information about cast and crew | ✅ Implemented       |
| Movie Details             | Comprehensive information about movies        | ✅ Implemented       |
| TV Show Details           | Detailed information about TV series          | ✅ Implemented       |
| Cast & Crew Information   | Explore the team behind movies and TV shows   | ✅ Implemented       |
| Jellyseerr Authentication | Log in to your Jellyseerr account             | ✅ Implemented       |
| Jellyseerr Requests       | Request movies and TV shows via Jellyseerr    | ✅ Implemented       |
| Discover Feed             | Personalized content recommendations          | 🚧 Work in Progress |
| TV Show Seasons           | Detailed information about individual seasons | 🚧 Work in Progress |

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

## Preview

https://github.com/user-attachments/assets/5cde896d-c2ce-4ea7-819b-52884a914efc

