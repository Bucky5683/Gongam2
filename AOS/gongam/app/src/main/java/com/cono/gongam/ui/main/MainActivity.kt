package com.cono.gongam.ui.main

import android.content.Context
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
import com.cono.gongam.R
import com.cono.gongam.data.RankingViewModel
import com.cono.gongam.data.User
import com.cono.gongam.data.UserViewModel
import com.cono.gongam.ui.main.mainSubViews.ContentsTitleView
import com.cono.gongam.ui.main.mainSubViews.MyReportView
import com.cono.gongam.ui.main.mainSubViews.RankingView
import com.cono.gongam.ui.main.mainSubViews.TimerView
import com.cono.gongam.ui.main.mainSubViews.TopView
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
                    color = colorResource(id = R.color.main_gray)
                ) {
                    MainScreen(profileImageUrl = sharedPreferencesUtil.getUser().profileImageURL ?: "")
                }
            }
        }

    }
}

// Compose main at here
@Composable
fun MainScreen(profileImageUrl: String) {
    val context: Context = LocalContext.current
    val sharedPreferences = SharedPreferencesUtil(context)
    val user: User = sharedPreferences.getUser()

    val rankingViewModel: RankingViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    rankingViewModel.updateRankUserList()
    userViewModel.setCurrentUser(user)

    val rankUserList by rankingViewModel.rankUserList.observeAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .background(
                color = Color.White
            )
            .verticalScroll(rememberScrollState())
    ) {
        TopView(profileImgUrl = profileImageUrl)
        Spacer(modifier = Modifier.height(15.dp))
        TimerView(context)
        Spacer(modifier = Modifier.height(42.5.dp))
        if (rankUserList.isNotEmpty()) {
            rankingViewModel.setUserRank(user.email ?: "")
            rankingViewModel.setStudyTimeAverage()
            RankingView(context = context)
        }
        Spacer(modifier = Modifier.height(15.dp))
        MyReportView(-99, -99, -99, 99, 99, -99, -99)
        Spacer(modifier = Modifier.height(23.dp))
    }
}

// ------------------------------------ Previews ------------------------------------

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewMain() {
    MainScreen(profileImageUrl = "")
}

@Preview
@Composable
fun PreviewTopView() {
    TopView(profileImgUrl = "")
}

@Preview
@Composable
fun PreviewTimerView() {
    ContentsTitleView("타이머", false)
}

@Preview(showBackground = true)
@Composable
fun PreviewRankingView() {
    val context = LocalContext.current
    RankingView(context = context)
}

//@Preview
//@Composable
//fun PreviewMyReportView() {
//    MyReportView()
//}