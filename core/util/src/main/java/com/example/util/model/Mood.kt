package com.example.util.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AngryColor
import com.example.ui.theme.AwfulColor
import com.example.ui.theme.BoredColor
import com.example.ui.theme.CalmColor
import com.example.ui.theme.DepressedColor
import com.example.ui.theme.DisappointedColor
import com.example.ui.theme.HappyColor
import com.example.ui.theme.HumorousColor
import com.example.ui.theme.LonelyColor
import com.example.ui.theme.MysteriousColor
import com.example.ui.theme.NeutralColor
import com.example.ui.theme.RomanticColor
import com.example.ui.theme.ShamefulColor
import com.example.ui.theme.SurprisedColor
import com.example.ui.theme.SuspiciousColor
import com.example.ui.theme.TenseColor
import com.example.util.R

enum class Mood(
    val icon: Int,
    val contentColor: Color,
    val containerColor: Color
) {
    Neutral(
        icon = R.drawable.neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor,
    ),
    Happy(
        icon = R.drawable.happy,
        contentColor = Color.Black,
        containerColor = HappyColor
    ),
    Angry(
        icon = R.drawable.angry,
        contentColor = Color.White,
        containerColor = AngryColor
    ),
    Bored(
        icon = R.drawable.bored,
        contentColor = Color.Black,
        containerColor = BoredColor
    ),
    Blush(
        icon = R.drawable.blush,
        contentColor = Color.Black,
        containerColor = CalmColor
    ),
    Depressed(
        icon = R.drawable.depressed,
        contentColor = Color.Black,
        containerColor = DepressedColor
    ),
    Disappointed(
        icon = R.drawable.dissapointed,
        contentColor = Color.White,
        containerColor = DisappointedColor
    ),
    Humorous(
        icon = R.drawable.humorous,
        contentColor = Color.Black,
        containerColor = HumorousColor
    ),
    Evil(
        icon = R.drawable.evil,
        contentColor = Color.White,
        containerColor = LonelyColor
    ),
    Kindness(
        icon = R.drawable.kindness,
        contentColor = Color.Black,
        containerColor = MysteriousColor
    ),
    Frustration(
        icon = R.drawable.frustration,
        contentColor = Color.White,
        containerColor = RomanticColor
    ),
    Sick(
        icon = R.drawable.sick,
        contentColor = Color.White,
        containerColor = ShamefulColor
    ),
    Sad(
        icon = R.drawable.sad,
        contentColor = Color.Black,
        containerColor = AwfulColor
    ),
    Surprised(
        icon = R.drawable.surprised,
        contentColor = Color.Black,
        containerColor = SurprisedColor
    ),
    Love(
        icon = R.drawable.love,
        contentColor = Color.Black,
        containerColor = SuspiciousColor
    ),
    Fear(
        icon = R.drawable.fearfull,
        contentColor = Color.Black,
        containerColor = TenseColor
    )
}