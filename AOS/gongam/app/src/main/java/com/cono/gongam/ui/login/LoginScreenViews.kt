package com.cono.gongam.ui.login

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cono.gongam.R
import com.cono.gongam.data.RankUser
import com.cono.gongam.data.RankingViewModel
import com.cono.gongam.data.StudyDatesViewModel
import com.cono.gongam.data.User
import com.cono.gongam.data.UserViewModel
import com.cono.gongam.ui.splash.SplashImage
import com.cono.gongam.utils.DateUtils
import com.cono.gongam.utils.SharedPreferencesUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(navController: NavController) {
    val userViewModel: UserViewModel = viewModel()
    val rankingViewModel: RankingViewModel = viewModel()
    val studyDatesViewModel: StudyDatesViewModel = viewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        SplashImage()
        Spacer(modifier = Modifier.weight(2f))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 113.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        GoogleLoginButton(
            onLoginSuccess = { user, uid ->
                saveSharedPreferences(context = navController.context, user, uid)
                loginOnSuccess(context = navController.context, navController)
            },
            onRegisterSuccess = { newUser, uid ->
                saveSharedPreferences(context = navController.context, newUser, uid)
                registerOnSuccess(context = navController.context, navController) },
            userViewModel = userViewModel,
            rankingViewModel = rankingViewModel,
            studyDatesViewModel = studyDatesViewModel
        )
    }
}

fun loginOnSuccess(context: Context, navController: NavController) {
    Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
    navController.navigate("Main") {
        popUpTo("Login") { inclusive = true }
    }
}

fun registerOnSuccess(context: Context, navController: NavController) {
    Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
    navController.navigate("Register") {
        popUpTo("Login") { inclusive = true }
    }
}

fun saveSharedPreferences(context: Context, user: User, uid: String) {
    val sharedPreferencesUtil = SharedPreferencesUtil(context)
    sharedPreferencesUtil.saveUser(user, uid)
    Log.d("[LoginScreen]", "getUserInSP : ${sharedPreferencesUtil.getUser()}")
}

@Composable
fun GoogleLoginButton(
    onLoginSuccess: (lUser: User, uid: String) -> Unit, onRegisterSuccess: (newUser: User, uid: String) -> Unit,
    userViewModel: UserViewModel, rankingViewModel: RankingViewModel, studyDatesViewModel: StudyDatesViewModel)
{
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifeCycleOwner = LocalLifecycleOwner.current

    val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        val response = IdpResponse.fromResultIntent(result.data)

        if (result.resultCode == Activity.RESULT_OK) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            currentUser?.let {
                scope.launch {
//                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser.let { it ->
                        val uid = it.uid // used as a key
                        val email = it.email
                        val name = it.displayName
                        val profileImageUrl = it.photoUrl
                        val todayDate = DateUtils.getCurrentDate()
                        val user = User(email = email, lastUpdateDate = todayDate, name = name, profileImageURL = profileImageUrl.toString())

                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: uid: $uid, email: $email, name: $name, profileImageUrl: $profileImageUrl")

                        if (isNewUser(uid = uid)) { // 새로운 유저 -> DB에 삽입
                            Firebase.database.getReference("Users").child(uid).setValue(user)
                                .addOnSuccessListener {
                                    Log.d("[LoginScreen]", "RealtimeDB : $uid, $email 사용자 추가 완료")
                                }
                                .addOnFailureListener {
                                    Log.d("[LoginScreen]", "새 사용자 추가 실패: ${it.message}")
                                    Toast.makeText(context, "오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                                }

                            val userInRank = RankUser(email = email, name = name, profileImageURL = profileImageUrl.toString())
                            Firebase.database.getReference("Rank").child(uid).setValue(userInRank)
                                .addOnSuccessListener {
                                    Log.d("[LoginScreen]", "RealtimeDB : Rank 사용자 추가 완료")
                                }
                                .addOnFailureListener {
                                    Log.d("[LoginScreen]", "Rank 추가 실패: ${it.message}")
                                }

                            onRegisterSuccess(user, uid)
                        } else { // 이미 존재하는 유저
                            val dataSnapshot = Firebase.database.getReference("Users").child(uid).get().await()
                            val userData = dataSnapshot.getValue(User::class.java)
                            user.timerStudyTime = userData?.timerStudyTime
                            user.stopwatchStudyTime = userData?.stopwatchStudyTime
                            user.todayStudyTime = userData?.timerStudyTime!! + userData.stopwatchStudyTime!!
                            user.goalStudyTime = userData.goalStudyTime

                            onLoginSuccess(user, uid)
                        }

                        initViewModels(userViewModel, rankingViewModel, studyDatesViewModel, user, lifeCycleOwner, uid)
                    }
                }
            }
        } else {
            Toast.makeText(context, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .background(Color.White)
            .clickable {
                val providers = arrayListOf(
                    AuthUI.IdpConfig
                        .GoogleBuilder()
                        .build()
                )

                val signInIntent = AuthUI
                    .getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build()

                startForResult.launch(signInIntent)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ), // Add shadow here
        shape = RoundedCornerShape(10.dp), // Adjust corner shape as needed
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(15.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_google),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "Google 로그인",
                fontSize = 17.sp,
                fontWeight = FontWeight(500),
                modifier = Modifier.align(Alignment.CenterVertically),
                color = colorResource(id = R.color.shadow_gray4)
            )
        }
    }

}


private fun initViewModels(
    userViewModel: UserViewModel,
    rankingViewModel: RankingViewModel,
    studyDatesViewModel: StudyDatesViewModel,
    user: User, lifeCycleOwner: LifecycleOwner,
    uid: String
)
{
    userViewModel.setCurrentUser(user)
    rankingViewModel.updateRankUserList()

    rankingViewModel.rankUserList.observe(lifeCycleOwner) {
        rankingViewModel.setUserRank(user.email ?: "")
        rankingViewModel.setStudyTimeAverage()
        studyDatesViewModel.updateStudyDates(uid)
    }
}

suspend fun isNewUser(uid: String): Boolean {
    val usersRef = Firebase.database.getReference("Users")
    var isNewUser = false

    // 해당 UID의 사용자 데이터가 있는지 확인
    val dataSnapshot = usersRef.child(uid).get().await()

    if (!dataSnapshot.exists()) {
        Log.d("[databasetest]", "data not exist")
        isNewUser = true
    } else {
        Log.d("[databasetest]", "data exist")
    }

    return isNewUser
}