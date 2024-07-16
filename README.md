
<h1 align="center">Diaryapp Note (compose) with MangoDB</h1></br>
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

[//]: # (<img src="https://user-images.githubusercontent.com/24237865/93901108-238eb000-fd31-11ea-9fac-c9ba1eca146c.gif" width="270"/>)

[//]: # (<img src="/preview/preview0.gif" width="270"/>)

[//]: # (<img src="/preview/preview1.gif" width="270"/>)
</p>

## Tech stack & Open-source libraries
- Minimum SDK level 24
- 100% [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Hilt for dependency injection.
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