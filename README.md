
<h1 align="center">Journal Note (compose) with MangoDB</h1></br>
<p align="center">  
Journal Note app using compose and Hilt based on modern Android tech-stacks and MVVM architecture. Fetching data from the network and integrating persisted data in the <a href="https://mongodb.com" target="_blank"> MongoDB </a> via repository pattern.<br> Declarative UI version using compose.
</p>
</br>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=24"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/setiawanboedy"><img alt="Profile" src="https://img.shields.io/badge/Github-Setiawanboedy-2ea44f"/></a> 
</p>

## Download
Go to the [Releases](https://github.com/setiawanboedy/diary-note/releases) to download the latest APK.

## Screenshots
<p align="center">

<img src="https://github.com/setiawanboedy/journal-note/blob/modular/screenshot/Screenshot_20240716_164546.png" width="270"/>

<img src="https://github.com/setiawanboedy/journal-note/blob/modular/screenshot/Screenshot_20240716_164643.png" width="270"/>

<img src="https://github.com/setiawanboedy/journal-note/blob/modular/screenshot/Screenshot_20240716_164715.png" width="270"/>
<img src="https://github.com/setiawanboedy/journal-note/blob/modular/screenshot/Screenshot_20240716_164723.png" width="270"/>
</p>

## Tech stack & Open-source libraries
- Minimum SDK level 24
- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Hilt for dependency injection.
- Room for database persist
- Firebase Storage for Image Storage
- JetPack
    - Compose - A modern toolkit for building native Android UI.
    - Lifecycle - dispose observing data when lifecycle state changes.
    - ViewModel - UI related data holder, lifecycle aware.
    - Room Persistence - construct database.
    - App Startup - Provides a straightforward, performant way to initialize components at application startup.
- Architecture
    - MVVM Architecture (Declarative View - ViewModel - Model)
    - Repository pattern
- Material3 Design & Animations
- [OneTap](https://github.com/JakeWharton/timber) - OneTapCompose for google login.
- [Firebase](https://firebase.google.com/docs/android/setup) - OneTapCompose for google login.
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - Persisted database library.
- [Retrofit2](https://github.com/square/retrofit) - construct the REST APIs and paging network data.
- [MongoDb](https://github.com/mongodb/stitch-android-sdk) - construct the REST APIs and paging network data.