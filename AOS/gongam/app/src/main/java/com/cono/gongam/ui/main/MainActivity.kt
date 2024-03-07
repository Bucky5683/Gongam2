package com.cono.gongam.ui.main

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cono.gongam.R
import com.cono.gongam.data.viewmodels.RankingViewModel
import com.cono.gongam.data.viewmodels.StudyDatesViewModel
import com.cono.gongam.data.TodoScreen
import com.cono.gongam.data.viewmodels.AIStopWatchViewModel
import com.cono.gongam.data.viewmodels.StopWatchViewModel
import com.cono.gongam.data.viewmodels.TimerViewModel
import com.cono.gongam.data.viewmodels.UserViewModel
//import com.cono.gongam.data.viewmodels.UserViewModelFactory
import com.cono.gongam.ui.login.LoginScreen
import com.cono.gongam.ui.main.mainSubViews.ContentsTitleView
import com.cono.gongam.ui.main.mainSubViews.MyReportView
import com.cono.gongam.ui.main.mainSubViews.RankingView
import com.cono.gongam.ui.main.mainSubViews.TimerView
import com.cono.gongam.ui.main.mainSubViews.TopView
import com.cono.gongam.ui.myreport.MyReportScreen
import com.cono.gongam.ui.ranking.RankingScreen
import com.cono.gongam.ui.register.RegisterScreen
import com.cono.gongam.ui.splash.SplashScreen
import com.cono.gongam.ui.theme.GongamTheme
import com.cono.gongam.ui.timer.StopWatchScreen
import com.cono.gongam.ui.timer.TimerScreen
import com.cono.gongam.utils.SharedPreferencesUtil
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferencesUtil : SharedPreferencesUtil
    private lateinit var userViewModel: UserViewModel

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferencesUtil(this)
//        userViewModel = ViewModelProvider(this, UserViewModelFactory(sharedPreferencesUtil.getUid())).get(UserViewModel::class.java)
        userViewModel = UserViewModel()

//        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = Color.White
                ) {
                    MyApp(sharedPreferencesUtil, activity = this, userViewModel = userViewModel)
                }
            }
        }

        observeDatabaseChanges()
    }

    private fun observeDatabaseChanges() {
        val userRef = Firebase.database.getReference("Users").child(sharedPreferencesUtil.getUid())

        userRef.child("stopwatchStudyTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userViewModel.currentUser.value?.stopwatchStudyTime = snapshot.getValue(Int::class.java) ?: 0
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        userRef.child("timerStudyTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userViewModel.currentUser.value?.timerStudyTime = snapshot.getValue(Int::class.java) ?: 0
            }

            override fun onCancelled(error: DatabaseError) {}

        })

        userRef.child("todayStudyTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userViewModel.currentUser.value?.todayStudyTime = snapshot.getValue(Int::class.java) ?: 0
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MyApp(sharedPreferencesUtil: SharedPreferencesUtil, activity: Activity, userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val uid = sharedPreferencesUtil.getUid()
//    val userViewModel: UserViewModel = viewModel()
//    val userViewModel = ViewModelProvider(activity, UserViewModelFactory(uid))
    val rankingViewModel: RankingViewModel = viewModel()
    val studyDatesViewModel: StudyDatesViewModel = viewModel()
    val timerViewModel: TimerViewModel = viewModel()
    val stopWatchViewModel: StopWatchViewModel = viewModel()

    NavHost(navController, startDestination = TodoScreen.Splash.name) {
        composable(route = TodoScreen.Splash.name) {
            SplashScreen(navController)
        }
        composable(route = TodoScreen.Login.name) {
            LoginScreen(navController, userViewModel, rankingViewModel, studyDatesViewModel)
        }
        composable(TodoScreen.Ranking.name) {
            RankingScreen(userViewModel, rankingViewModel)
        }
        composable(TodoScreen.MyReport.name) {
            MyReportScreen(studyDatesViewModel)
        }
        composable(TodoScreen.Timer.name) {
            TimerScreen(userViewModel, timerViewModel, uid)
        }
        composable(TodoScreen.StopWatch.name) {
            StopWatchScreen(userViewModel, stopWatchViewModel, uid)
        }
        composable("Register") {
            RegisterScreen(navController, userViewModel, uid)
        }
        composable(TodoScreen.Main.name) {
//            timerViewModel.updateSecondsInDatabase(uid)
//            stopWatchViewModel.updateSecondsInDatabase(uid)
            MainScreen(navController, userViewModel, rankingViewModel, studyDatesViewModel, uid, activity)
        }
    }
}

fun setStatusBarColor(context: Context, color: Color) {
    val window = (context as Activity).window
    window.statusBarColor = color.toArgb()
}

@Composable
fun MainScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    rankingViewModel: RankingViewModel,
    studyDatesViewModel: StudyDatesViewModel,
    uid: String,
    activity: Activity
) {
    val context: Context = LocalContext.current
    val sharedPreferences = SharedPreferencesUtil(context)
//    val currentUser by userViewModel.currentUser.observeAsState()
    val currentUser = userViewModel.currentUser.observeAsState()
    val thisWeekData by studyDatesViewModel.thisWeekStudyDate.observeAsState()
    val averageThisWeek by studyDatesViewModel.averageThisWeek.observeAsState()
    val rankUserList by rankingViewModel.rankUserList.observeAsState(initial = emptyList())
    val userRank by rankingViewModel.userRank.observeAsState(initial = "")
    val studyTimeAverage by rankingViewModel.studyTimeAverage.observeAsState(initial = 0)

    val statusBarColor = colorResource(id = R.color.main_gray)
    SideEffect {
        setStatusBarColor(context, statusBarColor)
    }

    rankingViewModel.updateRankUserList()
    if (rankUserList.isNotEmpty() && currentUser.value != null) {
        rankingViewModel.setUserRank(email = currentUser.value!!.email ?: "")
        rankingViewModel.setStudyTimeAverage()
    }
    studyDatesViewModel.updateStudyDates(sharedPreferences.getUid())

    Column(
        modifier = Modifier
            .background(
                color = Color.White
            )
            .verticalScroll(rememberScrollState())
    ) {
        currentUser.value?.let {
            TopView(uid, userViewModel, currentUser.value!!)
        }
        Spacer(modifier = Modifier.height(15.dp))
        TimerView(navController = navController, activity = activity)
        Spacer(modifier = Modifier.height(42.5.dp))
        if (rankUserList.isNotEmpty()) {
            Log.d("MainScreen", "rankUserList is not empty")
            currentUser.value?.let { RankingView(navController = navController, context = context, user = it, rankUserList = rankUserList, rankingViewModel = rankingViewModel, userRank = userRank, studyTimeAverage = studyTimeAverage) }
        }
        Spacer(modifier = Modifier.height(15.dp))
        currentUser.value?.let {
            MyReportView(navController = navController, user = it, thisWeekData = thisWeekData, averageThisWeek = averageThisWeek, context = context)
        }
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