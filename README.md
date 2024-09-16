# SearchJob

Просмотр текущего документа на русском языке:
- [Русский](/docs/ru/README.md)

## Overview

This application was created as diploma project for Yandex Practicum course. It uses HeadHunter api. Below its main
functions:

- You can search vacancies by its names.
- You can apply filter to search such as country, region, industry, desired salary. You can exclude vacancies in which
  salary not specified.
- You can add vacancy to favorites or delete it from favorites. Vacancy from favorite perhaps to see without internet
  connection. But if you have the internet connection and vacancy has deleted from server you cannot watch it.

## Technology Stack

- Programming language: Kotlin
- Architecture: Clean Architecture, MVVM
- Network Requests: Retrofit
- Asynchronous programming: Kotlin Coroutines, Flow
- UI: XML
- Navigation: Single Activity + Jetpack Navigation, BottomNavigationView
- DI: Koin
- Data Storage: SharedPreference, Room
- Other: Glide, LiveData

## Installation

### From source code

If you compile this project from the source code, you will need to configure the `develop.properties` file.
To do this, create this file in the root directory. Then add

```properties
hhAccessToken=
```

and your token value. Without it your project won't work.

### From apk
System requirements: minSdk=26 (Android 8.0)

[<img src="/docs/img/get_it_on_github.png" alt="Get it on github" height="80">]()

## Possible changes
- Realise DI using Hilt or Dagger2 instead of Koin.
- Create UI using Jetpack Compose
- Implement support for multiple languages
- Implement background vacancies update in favorites
