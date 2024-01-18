package com.cono.gongam.ui.main.mainSubViews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cono.gongam.R
import com.cono.gongam.data.UserViewModel
import com.cono.gongam.ui.register.debugPlaceHolder
import com.cono.gongam.utils.TimeUtils

@Composable
fun TopView(profileImgUrl: String) {
    val userViewModel: UserViewModel = viewModel()
    val user = userViewModel.getCurrentUser()
    val studyTime = user?.todayStudyTime ?: 0
    val goalTime = user?.goalStudyTime ?: 0
    val diffTime = goalTime - studyTime
    val studiedThanGoal: Boolean = goalTime - studyTime < 0

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp),
        color = colorResource(id = R.color.main_gray)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(45.dp)) // 상태바로 잘리는 부분을 위한 spacer
            Spacer(modifier = Modifier.height(13.7.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ProfileImage(profileImgUrl = profileImgUrl)
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
                text = TimeUtils.convertSecondsToTime(studyTime),
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
                    text = TimeUtils.convertSecondsToTime(diffTime),
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
fun ProfileImage(profileImgUrl: String) {
    AsyncImage(
        model = profileImgUrl,
        placeholder = debugPlaceHolder(R.drawable.img_test_profile),
        contentDescription = "profileImage",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .width(30.dp)
            .height(30.dp)
            .clickable {
                // TODO :: clickable setting(popup)
            }
    )
}

// ------------------------------------ Previews ------------------------------------

@Preview
@Composable
fun PreviewTopView() {
    TopView(profileImgUrl = "")
}

@Preview
@Composable
fun PreviewProfileImage() {
    ProfileImage(profileImgUrl = "")
}