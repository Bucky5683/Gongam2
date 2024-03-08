package com.cono.gongam.ui.editinfo

import android.util.Log
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
import com.google.firebase.database.database

@Composable
fun EditInfoScreen(navController: NavController, userViewModel: UserViewModel, uid: String) {
    val userGoalTime = TimeUtils.convertSecondsToTimeInTriple(userViewModel.getCurrentUser()?.goalStudyTime ?: 0)

    var name by remember { mutableStateOf(userViewModel.getCurrentUser()?.name ?: "") }
    var email by remember { mutableStateOf(userViewModel.getCurrentUser()?.email ?: "") }

    var hours by remember { mutableStateOf(userGoalTime.first) }
    var minutes by remember { mutableStateOf(userGoalTime.second) }
    var seconds by remember { mutableStateOf(userGoalTime.third) }

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
            Spacer(modifier = Modifier.weight(1f))
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
            .padding(horizontal = 6.dp)
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