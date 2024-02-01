package com.cono.gongam.ui.ranking

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cono.gongam.R
import com.cono.gongam.data.RankUser
import com.cono.gongam.data.RankingViewModel
import com.cono.gongam.data.User
import com.cono.gongam.data.UserViewModel
import com.cono.gongam.ui.SpacedEdgeTextsWithCenterVertically
import com.cono.gongam.ui.TopTitle
import com.cono.gongam.ui.main.setStatusBarColor
import com.cono.gongam.ui.register.debugPlaceHolder
import com.cono.gongam.utils.TimeUtils

@Composable
fun RankingScreen(
    userViewModel: UserViewModel,
    rankingViewModel: RankingViewModel,
) {
    val rankUserList by rankingViewModel.rankUserList.observeAsState(initial = emptyList())
    val context: Context = LocalContext.current
    val statusBarColor = colorResource(id = R.color.white)
    SideEffect {
        setStatusBarColor(context, statusBarColor)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.white))
    ) {
        TopTitle(backgroundColor = colorResource(id = R.color.white), textColor = colorResource(id = R.color.black), centerText = "랭킹", dividerLineColor = colorResource(
            id = R.color.gray_line2), backPress = true)
        if (userViewModel.getCurrentUser() != null && rankUserList.isNotEmpty()) {
            MyGradeView(user = userViewModel.getCurrentUser()!!, userRank = rankingViewModel.getUserRank())
        }
        TitleThisWeekTop5()
        if (rankUserList.isNotEmpty()) {
            RankingCards(rankUserList)
        }
    }
}

@Composable
fun MyGradeView(user: User, userRank: String) {
    val totalStudyTime = (user.timerStudyTime!! + user.stopwatchStudyTime!!).toString()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(193.dp)
    ) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 22.dp, vertical = 30.dp)
            .background(colorResource(id = R.color.white)),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.main_gray)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                SpacedEdgeTextsWithCenterVertically(
                    leftText = "나의 등수", leftTextSize = 17.sp, leftTextColor = colorResource(id = R.color.white), leftTextWeight = FontWeight(400),
                    rightText = userRank, rightTextSize = 32.sp, rightTextColor = colorResource(id = R.color.white), rightTextWeight = FontWeight(600)
                )
                Spacer(modifier = Modifier.height(9.dp))
                SpacedEdgeTextsWithCenterVertically(
                    leftText = "이번 주 공부 시간", leftTextSize = 14.sp, leftTextColor = colorResource(id = R.color.white), leftTextWeight = FontWeight(400),
                    rightText = TimeUtils.convertSecondsToTimeInString(totalStudyTime.toInt()), rightTextSize = 14.sp, rightTextColor = colorResource(id = R.color.white), rightTextWeight = FontWeight(400)
                )
            }
        }
    }
}

@Composable
fun TitleThisWeekTop5() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 17.dp)
    ) {
        Spacer(modifier = Modifier.width(37.dp))
        Text(
            text = "이번 주 Top 5",
            color = colorResource(id = R.color.main_gray),
            fontSize = 18.sp,
            fontWeight = FontWeight(700),
        )
    }
}

@Composable
fun RankingCards(rankUserList: List<RankUser>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        rankUserList.forEachIndexed { index, user ->
            if (index < 5) {
                RankingUserCard(grade = index + 1, profileImgUrl = user.profileImageURL ?: "", name = user.name ?: "", studyTime = user.totalStudyTime ?: 0)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RankingUserCard(grade: Int, profileImgUrl: String, name: String, studyTime: Int) {
    val heightVal: Int = when (grade) {
        1 -> 88
        2, 3 -> 66
        4, 5 -> 49
        else -> 0
    }

    val paddingHorizontalVal: Int = when (grade) {
        1 -> 38
        2, 3 -> 42
        else -> 47
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp, horizontal = paddingHorizontalVal.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(heightVal.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            RankingCardContent(grade = grade, profileImgUrl = profileImgUrl, name = name, studyTime = studyTime)
        }
    }
}

@Composable
fun RankingCardContent(grade: Int, profileImgUrl: String, name: String, studyTime: Int) {
    val startImgPaddingVal: Int = when (grade) {
        1, 2, 3 -> 22
        else -> 18
    }
    val endGradePaddingVal: Int = when (grade) {
        1 -> 21
        2, 3 -> 20
        else -> 18
    }
    val profileImgSize: Int = when (grade) {
        1 -> 50
        2, 3 -> 40
        else -> 30
    }
    val startTextPaddingVal: Int = when (grade) {
        1 -> 10
        2, 3 -> 15
        else -> 20
    }
    val gradeText: String = when (grade) {
        1 -> "1st"
        2 -> "2nd"
        3 -> "3rd"
        4 -> "4th"
        else -> "5th"
    }
    val gradeTextSize: Int = when (grade) {
        1 -> 20
        2, 3 -> 16
        else -> 15
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(start = startImgPaddingVal.dp, end = endGradePaddingVal.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profileImgUrl,
            placeholder = debugPlaceHolder(debugPreview = R.drawable.img_test_profile),
            contentDescription = "profile Img",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(profileImgSize.dp)
                .height(profileImgSize.dp)
                .clip(shape = CircleShape),
        )
        Spacer(Modifier.width(startTextPaddingVal.dp))
        CardText(grade = grade, name = name, studyTime = studyTime)
        Spacer(Modifier.weight(1f))
        Text(text = gradeText, fontSize = gradeTextSize.sp, fontWeight = FontWeight(700), color = colorResource(
            id = R.color.main_gray
        ))
    }
}

@Composable
fun CardText(grade: Int, name: String, studyTime: Int) {
    when (grade) {
        1, 2, 3 -> {
            // 2줄
            Column {
                Text(text = name, fontSize = 15.sp, fontWeight = FontWeight(400), color = colorResource(
                    id = R.color.main_gray))
                Text(text = TimeUtils.convertSecondsToTimeInString(studyTime), fontSize = 15.sp, fontWeight = FontWeight(400), color = colorResource(
                    id = R.color.main_gray))
            }
        }
        else -> {
            // 1줄
            Row {
                Text(text = name, fontSize = 15.sp, fontWeight = FontWeight(400), color = colorResource(
                    id = R.color.main_gray))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = TimeUtils.convertSecondsToTimeInString(studyTime), fontSize = 15.sp, fontWeight = FontWeight(400), color = colorResource(
                    id = R.color.main_gray))
            }
        }
    }
}