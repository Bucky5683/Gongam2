package com.example.gongam.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.gongam.R
import com.example.gongam.ui.main.mainSubViews.ContentsTitleView
import com.example.gongam.ui.main.mainSubViews.MyReportView
import com.example.gongam.ui.main.mainSubViews.RankingView
import com.example.gongam.ui.main.mainSubViews.TimerView
import com.example.gongam.ui.main.mainSubViews.TopView
import com.example.gongam.ui.theme.GongamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = colorResource(id = R.color.main_gray)
                ) { PreviewMain() }
            }
        }

    }
}

// Compose main at here
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewMain() {
    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .background(
                color = Color.White
            )
            .verticalScroll(rememberScrollState())
    ) {
        TopView()
        Spacer(modifier = Modifier.height(15.dp))
        TimerView(navController)
        Spacer(modifier = Modifier.height(42.5.dp))
        RankingView()
        Spacer(modifier = Modifier.height(15.dp))
        MyReportView(-99, -99, -99, 99, 99, -99, -99)
        Spacer(modifier = Modifier.height(23.dp))
    }
}

// ------------------------------------ Previews ------------------------------------

@Preview
@Composable
fun PreivewTopView() {
    TopView()
}

@Preview
@Composable
fun PreviewTimerView() {
    ContentsTitleView("타이머", false)
}

@Preview(showBackground = true)
@Composable
fun PreviewRankingView() {
    RankingView()
}

//@Preview
//@Composable
//fun PreviewMyReportView() {
//    MyReportView()
//}