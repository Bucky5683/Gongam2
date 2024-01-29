package com.cono.gongam.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cono.gongam.R
import com.cono.gongam.data.RankingViewModel
import com.cono.gongam.data.StudyDatesViewModel
import com.cono.gongam.data.TodoScreen
import com.cono.gongam.data.User
import com.cono.gongam.data.UserViewModel
import com.cono.gongam.ui.login.LoginScreen
import com.cono.gongam.ui.main.mainSubViews.ContentsTitleView
import com.cono.gongam.ui.main.mainSubViews.MyReportView
import com.cono.gongam.ui.main.mainSubViews.RankingView
import com.cono.gongam.ui.main.mainSubViews.TimerView
import com.cono.gongam.ui.main.mainSubViews.TopView
import com.cono.gongam.ui.ranking.RankingScreen
import com.cono.gongam.ui.splash.SplashScreen
import com.cono.gongam.ui.theme.GongamTheme
import com.cono.gongam.utils.SharedPreferencesUtil

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferencesUtil : SharedPreferencesUtil
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferencesUtil(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.White
                ) {
                    MyApp()
                }
            }
        }

    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    val userViewModel: UserViewModel = viewModel()
    val rankingViewModel: RankingViewModel = viewModel()
    val studyDatesViewModel: StudyDatesViewModel = viewModel()

    NavHost(navController, startDestination = TodoScreen.Splash.name) {
        composable(route = TodoScreen.Splash.name) {
            SplashScreen(navController)
        }
        composable(route = TodoScreen.Login.name) {
            LoginScreen(navController, userViewModel, rankingViewModel, studyDatesViewModel)
        }
        composable(TodoScreen.Main.name) {
            MainScreen(navController, userViewModel, rankingViewModel, studyDatesViewModel)
        }
        composable(TodoScreen.Ranking.name) {
            RankingScreen(userViewModel, rankingViewModel, studyDatesViewModel)
        }
//        composable("Register") {
//            RegisterScreen(navController)
//        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    rankingViewModel: RankingViewModel,
    studyDatesViewModel: StudyDatesViewModel
) {
    val context: Context = LocalContext.current
    val sharedPreferences = SharedPreferencesUtil(context)
    val currentUser by userViewModel.currentUser.observeAsState()
    val thisWeekData by studyDatesViewModel.thisWeekStudyDate.observeAsState()
    val rankUserList by rankingViewModel.rankUserList.observeAsState(initial = emptyList())
    val userRank by rankingViewModel.userRank.observeAsState(initial = "")
    val studyTimeAverage by rankingViewModel.studyTimeAverage.observeAsState(initial = 0)

    rankingViewModel.updateRankUserList()
    if (rankUserList.isNotEmpty() && currentUser != null) {
        rankingViewModel.setUserRank(email = currentUser?.email ?: "")
        rankingViewModel.setStudyTimeAverage()
    }

    Column(
        modifier = Modifier
            .background(
                color = Color.White
            )
            .verticalScroll(rememberScrollState())
    ) {
        currentUser?.let {
            TopView(user = it)
        }
        Spacer(modifier = Modifier.height(15.dp))
        TimerView(context)
        Spacer(modifier = Modifier.height(42.5.dp))
        if (rankUserList.isNotEmpty()) {
            Log.d("MainScreen", "rankUserList is not empty")
            currentUser?.let { RankingView(navController = navController, context = context, user = it, rankUserList = rankUserList, rankingViewModel = rankingViewModel, userRank = userRank, studyTimeAverage = studyTimeAverage) }
        }
        Spacer(modifier = Modifier.height(15.dp))
        MyReportView(thisWeekData, context)
        Spacer(modifier = Modifier.height(23.dp))
    }
}

// ------------------------------------ Previews ------------------------------------

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun PreviewMain() {
//    MainScreen(profileImageUrl = "")
//}

//@Preview
//@Composable
//fun PreviewTopView() {
//    TopView(profileImgUrl = "")
//}

@Preview
@Composable
fun PreviewTimerView() {
    ContentsTitleView("타이머", false)
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewRankingView() {
//    val context = LocalContext.current
//    RankingView(context = context)
//}

//@Preview
//@Composable
//fun PreviewMyReportView() {
//    MyReportView()
//}