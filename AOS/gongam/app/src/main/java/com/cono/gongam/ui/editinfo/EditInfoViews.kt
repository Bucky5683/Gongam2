package com.cono.gongam.ui.editinfo

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cono.gongam.R
import com.cono.gongam.data.TodoScreen
import com.cono.gongam.data.viewmodels.UserViewModel
import com.cono.gongam.ui.CircleTextButton
import com.cono.gongam.ui.register.CustomTextField
import com.cono.gongam.ui.register.EditableProfileImage
import com.cono.gongam.ui.register.InputTextTitle
import com.cono.gongam.utils.StringUtil
import com.cono.gongam.utils.TimeUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

@Composable
fun EditInfoScreen(navController: NavController, userViewModel: UserViewModel, uid: String) {
    val userGoalTime = TimeUtils.convertSecondsToTimeInTriple(userViewModel.getCurrentUser()?.goalStudyTime ?: 0)

    var name by remember { mutableStateOf(userViewModel.getCurrentUser()?.name ?: "") }
    var email by remember { mutableStateOf(userViewModel.getCurrentUser()?.email ?: "") }

    var hours by remember { mutableStateOf(userGoalTime.first) }
    var minutes by remember { mutableStateOf(userGoalTime.second) }
    var seconds by remember { mutableStateOf(userGoalTime.third) }

    val context = LocalContext.current

//    val goalStudyTime by remember { mutableIntStateOf(hours.toInt() * 3600 + minutes.toInt() * 60 + seconds.toInt()) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            EditableProfileImage(userViewModel = userViewModel, uid = uid)
            Spacer(modifier = Modifier.height(12.dp))
            InputTextTitle("닉네임")
            CustomTextField(
                value = name,
                onValueChange = { name = it }
            )
            InputTextTitle("이메일")
            CustomTextField(
                value = email,
                onValueChange = { email = it }
            )
            InputTextTitle("목표 공부시간")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomNumberInput(defaultVal = hours, onNumberEntered = { hours = it})
                Text(":", fontSize = 40.sp, fontWeight = FontWeight(700))
                CustomNumberInput(defaultVal = minutes, onNumberEntered = { minutes = it})
                Text(":", fontSize = 40.sp, fontWeight = FontWeight(700))
                CustomNumberInput(defaultVal = seconds, onNumberEntered = { seconds = it})
            }

        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            CircleTextButton(buttonText = "Done",
                btnOnClick = {
                    val goalStudyTime = hours.toInt() * 3600 + minutes.toInt() * 60 + seconds.toInt()
                    val userRef = Firebase.database.getReference("Users").child(uid)
                    val rankRef = Firebase.database.getReference("Rank").child(uid)

                    userRef.child("goalStudyTime").setValue(goalStudyTime)
                    userRef.child("name").setValue(name)
                    userRef.child("email").setValue(email)

                    rankRef.child("name").setValue(name)
                    rankRef.child("email").setValue(email)

                    userViewModel.getCurrentUser()?.goalStudyTime = goalStudyTime
                    userViewModel.getCurrentUser()?.name = name
                    userViewModel.getCurrentUser()?.email = email

                    navController.navigate(TodoScreen.Main.name)
                },
                buttonColor = colorResource(id = R.color.main_gray))
        }
        Column(
            modifier = Modifier
                .weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "회원 탈퇴",
                fontSize = 15.sp,
                fontWeight = FontWeight(900),
                color = Color.Red,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user?.uid

                    if (uid != null) {
                        val rankRef = Firebase.database.getReference("Rank").child(uid)
                        val studyDataesRef = Firebase.database.getReference("StudyDataes").child(uid)
                        val usersRef = Firebase.database.getReference("Users").child(uid)

                        rankRef.removeValue()
                            .addOnSuccessListener {
                                Log.d("DeleteAccount", "Rank 데이터 삭제 성공")
                            }
                            .addOnFailureListener { e ->
                                Log.d("DeleteAccount", "Rank 데이터 삭제 중 오류 발생: ${e.message}")
                            }

                        studyDataesRef.removeValue()
                            .addOnSuccessListener {
                                Log.d("DeleteAccount", "StudyDataes 데이터 삭제 성공")
                            }
                            .addOnFailureListener { e ->
                                Log.d("DeleteAccount", "StudyDataes 데이터 삭제 중 오류 발생: ${e.message}")
                            }

                        usersRef.removeValue()
                            .addOnSuccessListener {
                                Log.d("DeleteAccount", "Users 데이터 삭제 성공")
                            }
                            .addOnFailureListener { e ->
                                Log.d("DeleteAccount", "Users 데이터 삭제 중 오류 발생: ${e.message}")
                            }
                    }

                    user?.delete()
                        ?.addOnSuccessListener {
                            Log.d("DeleteAccount", "계정 삭제 성공")
                            Toast.makeText(context, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
                            navController.navigate(TodoScreen.Login.name)
                        }
                        ?.addOnFailureListener { e ->
                            Toast.makeText(context, "회원 탈퇴 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            Log.d("DeleteAccount", "계정 삭제 중 오류 발생: ${e.message}")
                        }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomNumberInput(defaultVal: String, onNumberEntered: (String) -> Unit) {
    var text by remember { mutableStateOf(defaultVal) }
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier
            .padding(horizontal = 0.dp)
            .width(85.dp),
        value = text,
        onValueChange = {
            if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                text = it
                if(it.isNotEmpty()) {
                    onNumberEntered(it)
                }
            }
        },
        textStyle = TextStyle(
            color = colorResource(id = R.color.main_gray),
            fontSize = 48.sp,
            fontWeight = FontWeight(700),

        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                text = StringUtil.convertToTwoDigitString(text)
                focusManager.clearFocus()
            },
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

}


@Composable
@Preview(showBackground = true)
fun PreviewEditInfoViews() {
    EditInfoScreen(navController = NavController(LocalContext.current), userViewModel = UserViewModel(), uid = "")
}