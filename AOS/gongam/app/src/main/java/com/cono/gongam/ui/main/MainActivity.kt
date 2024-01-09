package com.cono.gongam.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.cono.gongam.R
import com.cono.gongam.ui.main.mainSubViews.ContentsTitleView
import com.cono.gongam.ui.main.mainSubViews.MyReportView
import com.cono.gongam.ui.main.mainSubViews.RankingView
import com.cono.gongam.ui.main.mainSubViews.TimerView
import com.cono.gongam.ui.main.mainSubViews.TopView
import com.cono.gongam.ui.theme.GongamTheme
import com.cono.gongam.ui.timer.PreviewTimer
import com.cono.gongam.ui.timer.TimerActivity

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
                ) {
                    val navController = rememberNavController()
                    // NavHost 블록 안의 람다함수에서 navigation graph를 생성하며
                    // NavHost를 생성하기 위해서는 navController가 필요함
                    // NavHost에는 composable 함수들을 넣을 수 있으며, 이 함수들의 구조에 따라 Navigation Graph가 생성됨

                    NavHost(navController = navController, startDestination = "timer") {
                        composable("timer") {
//                            TimerActivity()
                            PreviewTimer()
                        }
                    }
//                    val previewContent = @Composable {
//                        PreviewMain(navController)
//                    }

                    PreviewMain(navController)
                }
            }
        }

    }
}

// Compose main at here
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewMain(navController: NavController = rememberNavController()) {
//    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .background(
                color = Color.White
            )
            .verticalScroll(rememberScrollState())
    ) {
        TopView()
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = { navController.navigate("timer") },
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)) {
        }
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