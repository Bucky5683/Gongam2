package com.cono.gongam.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cono.gongam.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("Login") {
            popUpTo("Splash") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_gongam),
            contentDescription = "splash Img",
            contentScale = ContentScale.None,
            modifier = Modifier.width(118.dp)
        )
    }
}

@Composable
fun SplashImage() {
    Image(
        painter = painterResource(id = R.drawable.img_gongam),
        contentDescription = "splash Img",
        contentScale = ContentScale.None,
        modifier = Modifier.width(118.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(navController = rememberNavController())
}