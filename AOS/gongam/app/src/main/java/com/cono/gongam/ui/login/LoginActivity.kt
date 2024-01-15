package com.cono.gongam.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cono.gongam.R
import com.cono.gongam.data.RankUser
import com.cono.gongam.data.User
import com.cono.gongam.ui.login.ui.theme.GongamTheme
import com.cono.gongam.ui.main.MainActivity
import com.cono.gongam.ui.register.RegisterActivity
import com.cono.gongam.ui.splash.SplashImage
import com.cono.gongam.utils.DateUtils
import com.cono.gongam.utils.SharedPreferencesUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// TODO :: 로그인 했던 계정 정보가 SharedPreferences에 있을 경우 MainActivity로 바로 이동(자동 로그인)

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
//    private val userViewModel: UserViewModel by viewModels()
//    @Inject
//    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                    LoginScreen(
                        onLoginSuccess =
                        { user ->
                            Toast.makeText(applicationContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()

                            val sharedPreferencesUtil = SharedPreferencesUtil(this)
                            sharedPreferencesUtil.saveUser(user)
                            Log.d("[LoginScreen]", "getUserInSP : ${sharedPreferencesUtil.getUser()}")

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        },
                        onRegisterSuccess =
                        { newUser ->
                            Log.d("[LoginScreen]", "newUser 정보 : ${newUser}")
                            Toast.makeText(applicationContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
//                            userViewModel.setCurrentUser(newUser)
//                            Log.d("[LoginScreen]", "getCurrentUser : ${userViewModel.getCurrentUser()}")
                            val sharedPreferencesUtil = SharedPreferencesUtil(this)
                            sharedPreferencesUtil.saveUser(newUser)
                            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: (lUser: User) -> Unit, onRegisterSuccess: (newUser: User) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
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
        GoogleLoginButton(onLoginSuccess = onLoginSuccess, onRegisterSuccess = onRegisterSuccess)
    }
}

@Composable
fun GoogleLoginButton(onLoginSuccess: (lUser: User) -> Unit, onRegisterSuccess: (newUser: User) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val response = IdpResponse.fromResultIntent(result.data)

        if (result.resultCode == Activity.RESULT_OK) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let {
                scope.launch {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.let { it ->
                        val uid = it.uid // used as a key
                        val email = it.email
                        val name = it.displayName
                        val profileImageUrl = it.photoUrl
                        val todayDate = DateUtils.getCurrentDate()
                        val user = User(email = email, lastUpdateDate = todayDate, name = name, profileImageURL = profileImageUrl.toString())

                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: uid: $uid")
                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: email: $email")
                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: name: $name")
                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: profileImageUrl: $profileImageUrl")

                        if (isNewUser(uid = uid)) { // 새로운 유저 -> DB에 삽입
                            Firebase.database.getReference("Users").child(uid).setValue(user)
                                .addOnSuccessListener {
                                    Log.d("[LoginScreen]", "RealtimeDB : $uid, $email 사용자 추가 완료")
                                }
                                .addOnFailureListener {
                                    Log.d("[LoginScreen]", "새 사용자 추가 실패: ${it.message}")
//                                    Toast.makeText(context, "오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                                }

                            val userInRank = RankUser(email = email, name = name, profileImageURL = profileImageUrl.toString())
                            Firebase.database.getReference("Rank").child(uid).setValue(userInRank)
                                .addOnSuccessListener {
                                    Log.d("[LoginScreen]", "RealtimeDB : Rank 사용자 추가 완료")
                                }
                                .addOnFailureListener {
                                    Log.d("[LoginScreen]", "Rank 추가 실패: ${it.message}")
                                }

                            onRegisterSuccess(user)
                        } else { // 이미 존재하는 유저
                            onLoginSuccess(user)
                        }
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

suspend fun isNewUser(uid: String): Boolean {
    val usersRef = Firebase.database.getReference("Users")
    var isNewUser = false

    // 해당 UID의 사용자 데이터가 있는지 확인
    val dataSnapshot = usersRef.child(uid).get().await()

    if (!dataSnapshot.exists()) {
        Log.d("[databasetest]", "데이터 존재하지 않음")
        isNewUser = true
    } else {
        Log.d("[databasetest]", "데이터 존재")
    }

    return isNewUser
}

// ------------------------------------ Previews ------------------------------------

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onLoginSuccess = {}, onRegisterSuccess = {})
}