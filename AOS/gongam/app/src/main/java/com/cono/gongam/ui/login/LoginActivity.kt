package com.cono.gongam.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import com.cono.gongam.data.User
import com.cono.gongam.data.UserViewModel
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// TODO :: 로그인 했던 계정 정보가 SharedPreferences에 있을 경우 MainActivity로 바로 이동

class LoginActivity : ComponentActivity() {
    private val viewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                    LoginScreen(
                        onLoginSuccess = {
                            Toast.makeText(applicationContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("[LoginScreen]", "사용자가 이미 데이터베이스에 존재합니다. 로그인 처리")
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        },
                        onRegisterSuccess = { user ->
                            Toast.makeText(applicationContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("[LoginScreen]", "사용자 데이터를 데이터베이스에 저장했습니다. 회원가입 화면으로 이동")
                            viewModel.setCurrentUser(user)
                            val sharedPreferencesUtil = SharedPreferencesUtil(this)
                            sharedPreferencesUtil.saveUser(user)
                            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                            finish()
                        })
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterSuccess: (user: User) -> Unit) {
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
fun GoogleLoginButton(onLoginSuccess: () -> Unit, onRegisterSuccess: (user: User) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val response = IdpResponse.fromResultIntent(result.data)

        if (result.resultCode == Activity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                scope.launch {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.let {
                        val uid = it.uid // used as a key
                        val email = it.email
                        val name = it.displayName
                        val profileImageUrl = it.photoUrl

                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: uid: $uid")
                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: email: $email")
                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: name: $name")
                        Log.d("[LoginScreen]", "GoogleLoginButton :: firebase getInstance() :: profileImageUrl: $profileImageUrl")

                        checkAndAddUserToDatabase(context = context, uid = uid, email = email!!, name = name!!, profileImgURL = profileImageUrl.toString(), onLoginSuccess, onRegisterSuccess)
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
        shape = RoundedCornerShape(10.dp), // You can adjust corner shape as needed
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
            Spacer(modifier = Modifier.width(15.dp)) // Add space between image and text
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

suspend fun checkAndAddUserToDatabase(context: Context, uid: String, email: String, name: String, profileImgURL: String, onLoginSuccess: () -> Unit, onRegisterSuccess: (user: User) -> Unit) {
    val usersRef = Firebase.database.getReference("Users")

    // 해당 UID의 사용자 데이터가 있는지 확인
    val dataSnapshot = usersRef.child(uid).get().await()

    if (!dataSnapshot.exists()) { // 새로운 사용자 추가 -> 회원가입 화면으로 이동
        val todayDate = DateUtils.getCurrentDate()
        Log.d("[LoginScreen]", "todayDate : $todayDate")
        val newUser = User(email = email, name = name, profileImageURL = profileImgURL, lastUpdateDate = todayDate)
        usersRef.child(uid).setValue(newUser)
            .addOnSuccessListener {
                Log.d("[LoginScreen]", "checkAndAddUserToDatabase :: $uid, $email 사용자 추가 완료")
                onRegisterSuccess(newUser)
            }
            .addOnFailureListener {
                Log.d("[LoginScreen]", "checkAndAddUserToDatabase :: 새 사용자 추가 실패: ${it.message}")
                Toast.makeText(context, "오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }
    }
    else { // 메인 화면으로 이동
        Log.d("[LoginScreen]", "checkAndAddUserToDatabase :: 이미 존재하는 사용자 :: $uid")
        onLoginSuccess()
    }
}

// ------------------------------------ Previews ------------------------------------
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onLoginSuccess = {}, onRegisterSuccess = {})
}