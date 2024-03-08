package com.cono.gongam.ui.register

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.cono.gongam.R
import com.cono.gongam.data.TodoScreen
import com.cono.gongam.data.viewmodels.UserViewModel
import com.cono.gongam.ui.CircleTextButton
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel, uid: String) {

    var name by remember { mutableStateOf(userViewModel.getCurrentUser()?.name ?: "") }
    var email by remember { mutableStateOf(userViewModel.getCurrentUser()?.email ?: "") }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            EditableProfileImage(userViewModel, uid)
            Spacer(modifier = Modifier.height(12.dp))
            InputTextTitle("닉네임")
            CustomTextField(value = name,
                onValueChange = { name = it }
            )
            InputTextTitle("이메일")
            CustomTextField(value = email,
                onValueChange = { email = it }
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Spacer(modifier = Modifier.weight(1f))
            CircleTextButton(buttonText = "Next",
                btnOnClick = {
                    navController.navigate(TodoScreen.Main.name)
                },
                buttonColor = colorResource(id = R.color.main_gray))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun EditableProfileImage(userViewModel: UserViewModel, uid: String) {
    val userProfileURL by userViewModel.userProfileURL.observeAsState(initial = "")

    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedImageUri ->
            userViewModel.viewModelScope.launch {
                userViewModel.uploadImageToFirebase(selectedImageUri)
                userViewModel.setProfileImageURLToFirebase(uid = uid, selectedImageUrl = selectedImageUri.toString())
            }
        }
    }

    Log.d("ProfileImage :: ", "url : ${userViewModel.getProfileImageURL()}")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 122.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            model = userProfileURL,
            placeholder = debugPlaceHolder(R.drawable.img_test_profile),
            contentDescription = "profile_Img",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable {
                    getContent.launch("image/*")
                }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            Spacer(modifier = Modifier.weight(17f))
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_edit_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()
                )
                Text(
                    text = "✏️",
                    fontSize = 15.sp,
                    fontWeight = FontWeight(700),
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 7.dp)
                )
            }
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}

@Composable
fun debugPlaceHolder(@DrawableRes debugPreview: Int) =
    if (LocalInspectionMode.current) {
        painterResource(id = debugPreview)
    } else {
        null
    }

@Composable
fun InputTextTitle(title: String) {
    Text(
        text = title, fontSize = 15.sp, fontWeight = FontWeight(700), color = Color.Black,
        modifier = Modifier.padding(top = 11.dp, start = 38.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var text by remember { mutableStateOf(value) }

    Column(
        modifier = Modifier.padding(vertical = 9.dp)
    ) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                onValueChange(it)
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 38.dp),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedTextColor = colorResource(id = R.color.main_gray),
                unfocusedTextColor = colorResource(id = R.color.main_gray),
                containerColor = colorResource(id = R.color.gray_scale1),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}