package com.project.animeappassignment.presentation

private enum class Routs{
    HOME_SCREEN,DETAILS_SCREEN
}
sealed class Screen(val destination : String) {
    object HomeScreen: Screen(Routs.HOME_SCREEN.toString())
    object DetailsScreen : Screen(Routs.DETAILS_SCREEN.toString())
}