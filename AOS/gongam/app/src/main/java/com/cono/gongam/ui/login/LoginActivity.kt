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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cono.gongam.R
import com.cono.gongam.ui.login.ui.theme.GongamTheme
import com.cono.gongam.ui.main.MainActivity
import com.cono.gongam.ui.splash.SplashImage
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                ) {
                    LoginScreen(onLoginSuccess = { user ->
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    })
                }
            }
        }
    }
}


@Composable
fun LoginScreen(onLoginSuccess: (FirebaseUser) -> Unit) {
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
        GoogleLoginButton(onLoginSuccess = onLoginSuccess)
//        Spacer(modifier = Modifier.height())
//        GoogleSignIn
    }
}

@Composable
fun GoogleLoginButton(onLoginSuccess: (FirebaseUser) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val response = IdpResponse.fromResultIntent(result.data)

        if (result.resultCode == Activity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                onLoginSuccess(it)
                Toast.makeText(context, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()

                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.let {
                    val uid = it.uid
                    val email = it.email
                    val displayName = it.displayName

                    Log.d("GoogleLoginTest", "UID: $uid")
                    Log.d("GoogleLoginTest", "Email: $email")
                    Log.d("GoogleLoginTest", "DisplayName: $displayName")

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

// ------------------------------------ Previews ------------------------------------
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onLoginSuccess = {})
}