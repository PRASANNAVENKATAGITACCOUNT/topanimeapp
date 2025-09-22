package com.project.animeappassignment.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.project.animeappassignment.common.Constants.PARAM_ANIME_ID
import com.project.animeappassignment.presentation.animedetails.AnimeDetailScreen
import com.project.animeappassignment.presentation.homescreen.HomeScreen
import com.project.animeappassignment.ui.theme.AnimeAppAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeAppAssignmentTheme {
                Surface {
                    val systemUiController = rememberSystemUiController()
                    LaunchedEffect(systemUiController) {
                        systemUiController.setStatusBarColor(
                            color = Color.Transparent, // Or any other background color you want
                            darkIcons = false         // Set this to false for white icons
                        )
                    }
                    AnimeApp()
                }
            }
        }
    }
}


@Composable
fun AnimeApp() {
    val context: Context= LocalContext.current
    val navController: NavHostController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(left=0.dp, right = 0.dp, top = 0.dp, bottom = 0.dp)
    ) { innerPadding ->
        Column(
           modifier =  Modifier
               .padding(innerPadding)
        ) {
            NavHost(
                modifier= Modifier,
                navController=navController,
                startDestination = Screen.HomeScreen.destination
            ){
                composable(Screen.HomeScreen.destination){
                    HomeScreen(navController)
                }
                composable(
                    Screen.DetailsScreen.destination+"/{$PARAM_ANIME_ID}"
                ){
                    AnimeDetailScreen()
                }

            }

        }
    }
}
