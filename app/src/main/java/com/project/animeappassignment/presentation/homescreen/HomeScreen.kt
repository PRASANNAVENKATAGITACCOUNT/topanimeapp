package com.project.animeappassignment.presentation.homescreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.project.animeappassignment.common.Constants.BASE_URL
import com.project.animeappassignment.domain.remote.JikanAPI
import com.project.animeappassignment.model.Data
import com.project.animeappassignment.presentation.Screen
import com.project.animeappassignment.presentation.ui.theme.BackgroundColor
import com.project.animeappassignment.presentation.ui.theme.GradientColors
import com.project.animeappassignment.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.contracts.contract


@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel= hiltViewModel()
    ) {
    val topAnimeState = homeScreenViewModel.state.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(left=0.dp, right = 0.dp, top = 0.dp, bottom = 0.dp)
    ) { innerPadding ->
        val isConnected = homeScreenViewModel.isConnected.collectAsState()

        LaunchedEffect(isConnected.value) {
            if (isConnected.value) {
                homeScreenViewModel.getTopAnimeAPI()
                homeScreenViewModel.hasloadedData=true
            } else {
                homeScreenViewModel.getTopAnimeLocal()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            topAnimeState.topAnime?.data?.let { data ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = Brush.linearGradient(colors = BackgroundColor))
                        .systemBarsPadding()
                        .safeContentPadding()
                        .navigationBarsPadding()
                        .padding(top = 15.dp)
                ) {
                    items(data){anime : Data->
                       AnimeUI(homeScreenViewModel,anime){anime_id->
                           navController.navigate(route = Screen.DetailsScreen.destination+"/${anime_id}")
                       }
                    }
                }
            }
            if (topAnimeState.error.isNotEmpty()){
                Text(text=topAnimeState.error,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.Center),
                    color=Color.White
                )
            }

            if (topAnimeState.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimeUI(homeScreenViewModel: HomeScreenViewModel,data: Data?, onClickAnime:(Long)-> Unit) {
    val windowSizeClass : WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isOrientationLandscape = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)


    var imageRequest: ImageRequest? = null
    val context=LocalContext.current

    val titleFontSize = if(isOrientationLandscape) 18.sp else 16.sp
    val commonTextFontSize = if(isOrientationLandscape) 16.sp else 12.sp

    data?.images?.jpg?.image_url?.let {url->
        Log.d("vgbnm", "AnimeUI: $url")
        imageRequest = ImageRequest.Builder(context)
        .data(url)
        .crossfade(true)
        .listener(
            onSuccess = { _, success ->
                Log.d("ImageLoading", " $success  Success ")
            },
            onError = { _, e ->
                Log.d("ImageLoading", " ${e.throwable.localizedMessage}  Image loading failed")
            }
        )
        .build()
    }
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .border(2.dp, color = Color.Transparent)
            .padding(8.dp)
            .clickable {
                data?.let {
                    onClickAnime(it.mal_id)
                }
            },

        ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(brush = Brush.linearGradient(colors = GradientColors))
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(160.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                val isConnected by homeScreenViewModel.isConnected.collectAsState()
                if(isConnected){
                    imageRequest?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .padding(2.dp)
                                .border(1.dp, color = Color.Transparent)
                                .padding(start = 2.dp)
                            ,
                        )
                    }
                }

                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .border(1.dp, color = Color.Transparent)
                        .padding(start = 5.dp)
                ){
                    data?.title?.let { text ->
                        Text(
                            text="Title:  $text",
                            fontSize= titleFontSize,
                            style=TextStyle(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                    data?.rating?.let {
                        Text(
                            text = "Ratings:  $it",
                            fontSize= commonTextFontSize,
                            style=TextStyle(
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color.White
                        )
                    }
                    data?.episodes?.let {
                        Text("Episodes: $it",
                            fontSize= commonTextFontSize,
                            style=TextStyle(
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color.White
                        )
                    }
                }

            }

        }

    }

}