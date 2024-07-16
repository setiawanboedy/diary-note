package com.example.util

import com.example.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    data object Authentication: Screen(route = "authentication_screen")
    data object Home: Screen(route = "home_screen")
    data object Write: Screen(route = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY={$WRITE_SCREEN_ARGUMENT_KEY}"){
        fun passDiaryappId(diaryappId: String) = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY=$diaryappId"

    }
}