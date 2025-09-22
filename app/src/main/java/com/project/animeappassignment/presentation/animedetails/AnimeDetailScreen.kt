package com.project.animeappassignment.presentation.animedetails

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.project.animeappassignment.model.Data
import com.project.animeappassignment.presentation.ui.theme.BackgroundColor
import com.project.animeappassignment.presentation.ui.theme.GradientColors
import com.project.animeappassignment.viewmodel.AnimeDetailsViewModel

@Composable
fun AnimeDetailScreen(
    animeDetailsViewModel: AnimeDetailsViewModel= hiltViewModel()
) {
    val animeDetailState = animeDetailsViewModel.state.value
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(left=0.dp, right = 0.dp, top = 0.dp, bottom = 0.dp)
    ) { innerPadding ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            animeDetailState.animeData?.let { anime->
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush =  Brush.linearGradient(colors = BackgroundColor))
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    AnimeDetailUI( animeDetailsViewModel,anime)
                }

            }

            if (animeDetailState.error.isNotEmpty()){
                Text(text=animeDetailState.error,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.Center)
                )
            }

            if (animeDetailState.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        }
    }
}

@Composable
fun AnimeDetailUI(animeDetailsViewModel:AnimeDetailsViewModel,data: Data?) {

    if(data == null) return

    val context=LocalContext.current
    val animeUiState = animeDetailsViewModel.uiState.collectAsState()
    val isConnected by animeDetailsViewModel.isConnected.collectAsState()
    val basicBackgroundModifier = Modifier
        .fillMaxWidth()
        .border(1.dp, color = Color.Transparent)
        .padding(8.dp)

    val topRoundedShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .systemBarsPadding()
            .safeContentPadding()
            .navigationBarsPadding()
            .padding(10.dp)
            .background(brush = Brush.linearGradient(colors = BackgroundColor)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isConnected) {
            Column(
                modifier =
                    Modifier.fillMaxWidth()
                    .wrapContentHeight()
            ) {
                data.images?.jpg?.image_url?.let { url ->
                    if(data.trailer?.youtube_id!=null && animeUiState.value.playTrailer){
                        val youtubeId = data.trailer?.youtube_id
                        VideoPlayer(
                            youtubeId,
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .aspectRatio(16 / 9f)
                        )
                    }
                    else if(animeUiState.value.loadingImage) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(url)
                                .crossfade(true)
                                .listener(
                                    onSuccess = { _, success ->
                                        Log.d("ImageLoading UI", " $success  Success ")
                                    },
                                    onError = { _, e ->
                                        Log.d(
                                            "ImageLoading UI",
                                            " ${e.throwable.localizedMessage}  Image loading failed"
                                        )
                                    }
                                )
                                .build(),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start=5.dp, end=5.dp)
                                .aspectRatio(14 / 12f),
                        )
                    }
                }
                if (data.trailer?.youtube_id != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .border(1.dp,color=Color.Transparent)
                            .padding(5.dp)
                            .height(45.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if(!animeUiState.value.playTrailer){
                            Button(onClick = {
                                animeDetailsViewModel.setTrailerMode()
                            }, modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(text = "Play Trailer")
                            }
                        }else{
                            Button(onClick = {
                               animeDetailsViewModel.setPosterMode()
                            }, modifier = Modifier.fillMaxHeight()
                            ) {
                                Text(text = "Poster")
                            }
                        }

                    }
                }
            }
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top=5.dp)
                .border(2.dp, color = Color.Transparent)
                .padding(top=8.dp),
            shape = topRoundedShape,

        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.linearGradient(colors = GradientColors))
            ){
                data.title?.let {
                    Text(
                        text = "TITLE: $it ",
                        fontSize = 18.sp,
                        modifier = basicBackgroundModifier,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
                data.episodes?.let {
                    Text(
                        text = "EPISODES : $it",
                        fontSize = 18.sp,
                        modifier = basicBackgroundModifier,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.White
                    )
                }
                data.rating?.let {
                    Text(
                        text = "RATINGS : $it ",
                        fontSize = 18.sp,
                        modifier = basicBackgroundModifier,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.White
                    )
                }
                data.genres?.let {
                    Column (
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f)
                    ){
                        Text(text = "GENRES : ",
                            style = TextStyle(fontWeight = FontWeight.Normal),
                            color = Color.White,
                            fontSize = 18.sp,
                            modifier =  Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp, start = 8.dp))
                        LazyRow(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(it) { genre ->
                                Column(
                                    Modifier
                                        .wrapContentWidth()
                                        .padding(bottom = 5.dp, top = 5.dp)
                                        .padding(5.dp)
                                ) {
                                    genre?.let {
                                        Box(
                                            modifier = Modifier.padding( 5.dp)
                                        ) {
                                            Text(
                                                text = "${it.name}",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Normal
                                                ),
                                                fontSize = 18.sp,
                                                color = Color.White
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }

                }
                data.synopsis?.let {
                    Text(
                        text = "SYNOPSIS : \n$it ",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
                            .padding(8.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Normal
                        ),
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }

        }

    }

}