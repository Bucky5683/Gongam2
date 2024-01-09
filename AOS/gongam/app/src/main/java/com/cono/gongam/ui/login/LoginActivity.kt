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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
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
                    color = MaterialTheme.colorScheme.background
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
    SplashImage()
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Hello")
        GoogleLoginButton(onLoginSuccess = onLoginSuccess)
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

    Button(
        onClick = {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()

            startForResult.launch(signInIntent)
        }
    ) {
        Text("Google 로그인")
    }
}
