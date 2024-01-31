package com.cono.gongam.ui.main.mainSubViews

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.cono.gongam.R
import com.cono.gongam.data.UserViewModel
import com.cono.gongam.ui.ButtonWithQuestionMark
import com.cono.gongam.ui.register.debugPlaceHolder
import com.cono.gongam.utils.TimeUtils
import kotlinx.coroutines.launch

@Composable
fun TopView(userViewModel: UserViewModel, uid: String) {
    val user = userViewModel.getCurrentUser()
    val studyTime = user?.todayStudyTime ?: 0
    val goalTime = user?.goalStudyTime ?: 0
    val diffTime = goalTime - studyTime
    val studiedThanGoal: Boolean = goalTime - studyTime < 0

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
//            .height(290.dp),
        color = colorResource(id = R.color.main_gray)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Spacer(modifier = Modifier.height(45.dp)) // 상태바로 잘리는 부분을 위한 spacer
            Spacer(modifier = Modifier.height(13.7.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ProfileImage(userViewModel = userViewModel, uid)
                Spacer(modifier = Modifier.width(15.dp))
            }
            Text(
                text = "오늘 공부한 시간",
                color = Color.White,
                modifier = Modifier.padding(top = 28.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                textDecoration = TextDecoration.Underline,
            )
            Text(
                text = TimeUtils.convertSecondsToTimeInString(studyTime),
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(700),
            )
            GoalText(studiedThanGoal, diffTime)
        }
    }
}

@Composable
fun GoalText(studiedThanGoal: Boolean, diffTime: Int) {

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .padding(bottom = 52.dp)
            .background(
                color = colorResource(R.color.gray_scale1),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "\uD83D\uDD25",
                modifier = Modifier
                    .padding(start = 20.dp),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = if (!studiedThanGoal) "목표까지" else "목표 달성!",
                color = colorResource(id = R.color.main_gray),
                modifier = Modifier
                    .padding(start = 5.dp, top = 6.dp, bottom = 6.dp),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(400),
            )
            if (!studiedThanGoal) {
                Text(
                    text = TimeUtils.convertSecondsToTimeInString(diffTime),
                    color = colorResource(id = R.color.main_gray),
                    modifier = Modifier
                        .padding(start = 5.dp, top = 6.dp, bottom = 6.dp),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(700),
                )
            }
            Text(
                text = "\uD83D\uDD25",
                modifier = Modifier
                    .padding(start = 5.dp, end = 20.dp),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ProfileImage(userViewModel: UserViewModel, uid: String) {
    var showPopup by remember { mutableStateOf(false) }
    val userProfileURLState = remember { mutableStateOf("") }
    val userProfileURL by userViewModel.userProfileURL.observeAsState(initial = "")
    userProfileURLState.value = userProfileURL

    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedImageUri ->
            userViewModel.viewModelScope.launch {
                userViewModel.uploadImageToFirebase(selectedImageUri)
                Log.d("ImageUrlTest", "currentProfileImageUrl : $userProfileURL")
                userProfileURLState.value = userViewModel.getProfileImageURL() ?: ""
                userViewModel.setProfileImageURLToFirebase(uid = uid, selectedImageUrl = selectedImageUri.toString())
            }
        }
    }

    Column {
//        AsyncImage(
//            model = userProfileURLState.value,
//            placeholder = debugPlaceHolder(R.drawable.img_test_profile),
//            contentDescription = "profileImage",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .clip(CircleShape)
//                .width(30.dp)
//                .height(30.dp)
//                .clickable {
//                    showPopup = true
//                }
//        )
        CoilImage(
            model = userProfileURLState.value,
            modifier = Modifier
                .clip(CircleShape)
                .width(30.dp).height(30.dp)
                .clickable {
                    showPopup = true
                }
        )
        if (showPopup) {
            Popup(
                onDismissRequest = { showPopup = false },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .width(270.dp)
                        .height(234.dp)
                        .background(Color.White)
                        .padding(top = 12.dp, start = 22.dp, end = 17.dp, bottom = 23.dp)
                ) {
                    Row{
                        Column(
                            modifier = Modifier.height(80.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = userViewModel.getCurrentUser()?.name ?: "",
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(700),
                                    color = Color.Black,
                                )
                            )
                            Text(
                                text = userViewModel.getCurrentUser()?.email ?: "",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight(400),
                                    color = Color.Black,
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(text = "목표 공부시간", fontSize = 12.sp, fontWeight = FontWeight(400), color = Color.Black, textDecoration = TextDecoration.Underline)
                                Text(text = "99:99:99", fontSize = 12.sp, fontWeight = FontWeight(400), color = Color.Black,
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .height(80.dp)
                                .width(83.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
//                            AsyncImage(
//                                model = userProfileURLState.value,
//                                placeholder = debugPlaceHolder(R.drawable.img_test_profile),
//                                contentDescription = "profileImage",
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier
//                                    .clip(CircleShape)
//                                    .width(80.dp)
//                                    .height(80.dp)
//                                    .clickable {
//                                        getContent.launch("image/*")
//                                    }
//                            )
                            CoilImage(
                                model = userProfileURLState.value,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .width(80.dp).height(80.dp)
                                    .clickable {
                                        getContent.launch("image/*")
                                    }
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    Image(
                                        painter = painterResource(id = R.drawable.img_edit_profile),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Text(
                                        text = "✏️",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(700),
                                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 7.dp)
                                    )
                                }

                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    ButtonWithQuestionMark("도움말")
                    Spacer(modifier = Modifier.height(8.dp))
                    ButtonWithQuestionMark("이용약관")
                }
            }
        }
    }
}

@Composable
fun ProfileEditPopup(showPopup: Boolean = true, userViewModel: UserViewModel) {
    var show = showPopup

    var currentProfileImageUrl by remember { mutableStateOf(userViewModel.getProfileImageURL()) }
    Popup(
        onDismissRequest = { show = false },
        properties = PopupProperties(focusable = true),
    ) {
        Column(
            modifier = Modifier
                .width(270.dp)
                .height(234.dp)
                .background(Color.White)
                .padding(top = 12.dp, start = 22.dp, end = 17.dp, bottom = 23.dp)
        ) {
            Row{
                Column(
                    modifier = Modifier.height(80.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "유저닉네임",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(700),
                            color = Color.Black,
                        )
                    )
                    Text(
                        text = "gjgj3686@gmail.com",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            color = Color.Black,
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(text = "목표 공부시간", fontSize = 12.sp, fontWeight = FontWeight(400), color = Color.Black, textDecoration = TextDecoration.Underline)
                        Text(text = "99:99:99", fontSize = 12.sp, fontWeight = FontWeight(400), color = Color.Black,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .width(83.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    AsyncImage(
                        model = currentProfileImageUrl,
                        placeholder = debugPlaceHolder(R.drawable.img_test_profile),
                        contentDescription = "profileImage",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .width(80.dp)
                            .height(80.dp)
                            .clickable {
                                // TODO :: 갤러리 연결 및 이미지 변경
                            }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.img_edit_profile),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                text = "✏️",
                                fontSize = 15.sp,
                                fontWeight = FontWeight(700),
                                modifier = Modifier.padding(vertical = 4.dp, horizontal = 7.dp)
                            )
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonWithQuestionMark("도움말")
            Spacer(modifier = Modifier.height(8.dp))
            ButtonWithQuestionMark("이용약관")
        }
    }
}

@Composable
fun CoilImage(
    model: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .build()
    val request = ImageRequest.Builder(context)
        .data(model).build()
    val painter = rememberAsyncImagePainter(request, imageLoader)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}

//@Composable
//fun ViewImageFromDatabase(imageUrl: String) {
//    val painter: Painter? = imageUrl.let {
//        val storageRef = FirebaseStorage.getInstance().reference.child(it)
//        val downloadUrl = storageRef.downloadUrl.await()
//        ImagePainter(downloadUrl)
//    }
//    painter?.let {
//        Image(painter = it, contentDescription = null)
//    }
//}

// ------------------------------------ Previews ------------------------------------

@Preview
@Composable
fun PreviewPopup() {
    ProfileEditPopup(userViewModel = UserViewModel())
}

//@Preview
//@Composable
//fun PreviewTopView() {
//    TopView(profileImgUrl = "")
//}

@Preview
@Composable
fun PreviewProfileImage() {
//    ProfileImage(userViewModel = UserViewModel())
}