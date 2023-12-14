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
import com.example.gongam.R
import com.example.gongam.ui.main.kit.ContentsTitleView
import com.example.gongam.ui.main.kit.MyReportView
import com.example.gongam.ui.main.kit.RankingView
import com.example.gongam.ui.main.kit.TimeButton
import com.example.gongam.ui.main.kit.TopView
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
    Column(
        modifier = Modifier
            .background(
                color = Color.White
            )
            .verticalScroll(rememberScrollState())
    ) {
        TopView()
        Spacer(modifier = Modifier.height(15.dp))
        ContentsTitleView("타이머", false)
        Spacer(modifier = Modifier.height(13.5.dp))
        TimeButton(icon = "⏰", title = "타이머")
        TimeButton(icon = "⏱️", title = "스톱워치")
        Spacer(modifier = Modifier.height(42.5.dp))
        ContentsTitleView("랭킹", true)
        RankingView()
        Spacer(modifier = Modifier.height(15.dp))
        MyReportView()
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
fun PreviewTimerButton() {
    TimeButton(icon = "⏰", title = "타이머")
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