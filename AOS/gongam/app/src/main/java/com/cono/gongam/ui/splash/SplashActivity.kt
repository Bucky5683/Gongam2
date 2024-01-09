package com.cono.gongam.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cono.gongam.ui.splash.ui.theme.GongamTheme
import com.cono.gongam.R
import com.cono.gongam.ui.login.LoginActivity
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                    LaunchedEffect(true) {
                        delay(3000) // 3초 지연
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish() // 현재 액티비티 종료
                    }
                    SplashImage()
                }
            }
        }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSplashImage() {
    SplashImage()
}