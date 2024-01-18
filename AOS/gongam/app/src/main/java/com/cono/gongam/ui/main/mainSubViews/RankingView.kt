package com.cono.gongam.ui.main.mainSubViews

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cono.gongam.R
import com.cono.gongam.data.RankingViewModel
import com.cono.gongam.data.User
import com.cono.gongam.data.UserViewModel
import com.cono.gongam.ui.ranking.RankingActivity
import com.cono.gongam.utils.TimeUtils


// TODO : 유저의 평균값 -> 주 단위의 값으로 변경 필요
@Composable
fun RankingView(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContentsTitleView("랭킹", true, context = context)
        Spacer(modifier = Modifier.height(37.dp))
        VerticalGraph(context)
        Spacer(modifier = Modifier.height(12.dp))
        SetCompareAverageText()
        Spacer(modifier = Modifier.height(31.dp))
    }
}

@Composable
private fun VerticalGraph(context: Context) {
    val userViewModel: UserViewModel = viewModel()
    val rankingViewModel: RankingViewModel = viewModel()
    val user: User? = userViewModel.getCurrentUser()
    val rankUserList by rankingViewModel.rankUserList.observeAsState(initial = emptyList())
    val isBelowAverage = user?.timerStudyTime!! + user.stopwatchStudyTime!! < rankingViewModel.getStudyTimeAverage()

    Box(
        modifier = Modifier
            .height(215.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                val intent = Intent(context, RankingActivity::class.java)
                context.startActivity(intent)
            }
    ){
        DrawGrayGraph()
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            if (rankUserList.isNotEmpty()) {
                if (isBelowAverage) {
                    Spacer(modifier = Modifier.weight(1f))
                    DrawAverageStudyTimes()
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        DrawUserStudyTimes(isBelowAverage = true)
                        Spacer(modifier = Modifier.weight(5f))
                    }
                }
                else {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        DrawUserStudyTimes(isBelowAverage = false)
                        Spacer(modifier = Modifier.weight(5f))
                    }
                    DrawAverageStudyTimes()
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun DrawGrayGraph() {
    Box( // 회색 그라데이션 이미지
        modifier = Modifier
            .width(61.dp)
            .fillMaxHeight()
            .padding(start = 7.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.main_gray_70alpha),
                        colorResource(id = R.color.main_gray)
                    ),
                    startY = 0f,
                    endY = 215f
                ),
                shape = RoundedCornerShape(10.dp)
            )
    )
}

@Composable
private fun DrawAverageStudyTimes() {
    val rankingViewModel: RankingViewModel = viewModel()

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // 평균 공부시간 bar shadow
            modifier = Modifier
                .shadow(
                    ambientColor = colorResource(id = R.color.shadow_gray2),
                    spotColor = colorResource(id = R.color.shadow_gray2),
                    elevation = 5.dp,
                    shape = RoundedCornerShape(size = 10.dp)
                ),
        ) {
            Box( // 평균 공부시간 bar
                modifier = Modifier
                    .width(68.dp)
                    .height(11.dp)
                    .background(
                        color = colorResource(id = R.color.gray_scale1),
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }
        Spacer(modifier = Modifier.width(11.dp))
        Box( // 평균 공부시간 text bar shadow
            modifier = Modifier
                .shadow(
                    ambientColor = colorResource(id = R.color.shadow_gray2),
                    spotColor = colorResource(id = R.color.shadow_gray2),
                    elevation = 5.dp,
                    shape = RoundedCornerShape(size = 10.dp)
                )
        ) {
            Box( // 평균 공부시간 text bar
                modifier = Modifier
                    .height(18.dp)
                    .background(
                        color = colorResource(id = R.color.gray_scale1),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "평균 공부시간 : ",
                        color = Color.White,
                        fontWeight = FontWeight(500),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = TimeUtils.convertSecondsToTime(rankingViewModel.getStudyTimeAverage()),
                        color = Color.White,
                        fontWeight = FontWeight(700),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawUserStudyTimes(isBelowAverage: Boolean) {
    val userViewModel: UserViewModel = viewModel()
    val rankingViewModel: RankingViewModel = viewModel()
    val user: User? = userViewModel.getCurrentUser()
    val userRank = rankingViewModel.getUserRank()

    val boxColor = if (isBelowAverage) colorResource(id = R.color.blue_scale1) else colorResource(id = R.color.red_scale1)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box( // 유저 공부시간 bar shadow
            modifier = Modifier.shadow(
                ambientColor = colorResource(id = R.color.shadow_gray2),
                spotColor = colorResource(id = R.color.shadow_gray2),
                elevation = 5.dp,
                shape = RoundedCornerShape(size = 10.dp)
            ),
        ) {
            Box( // 유저 공부시간 bar shadow
                modifier = Modifier
                    .width(68.dp)
                    .height(11.dp)
                    .background(
                        color = boxColor,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }
        Spacer(modifier = Modifier.width(11.dp))
        Box( // 유저 공부시간 text bar shadow
            modifier = Modifier.shadow(
                ambientColor = colorResource(id = R.color.shadow_gray2),
                spotColor = colorResource(id = R.color.shadow_gray2),
                elevation = 5.dp,
                shape = RoundedCornerShape(size = 10.dp)
            ),
        ) {
            Box( // 유저 공부시간 text bar
                modifier = Modifier
                    .height(46.dp)
                    .background(
                        color = boxColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${user?.name}님의 등수",
                        color = colorResource(id = R.color.main_gray),
                        fontWeight = FontWeight(400),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.width(13.dp))
                    Text(
                        text = if (userRank.toInt() <= 999) userRank else "999+",
                        color = colorResource(id = R.color.main_gray),
                        fontWeight = FontWeight(700),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun SetCompareAverageText() {
    val userViewModel: UserViewModel = viewModel()
    val rankingViewModel: RankingViewModel = viewModel()
    val user = userViewModel.getCurrentUser()
    val diffFromAverage = (user?.timerStudyTime!! + user.stopwatchStudyTime!!) - rankingViewModel.getStudyTimeAverage()
    val less: Boolean = diffFromAverage < 0
    if (diffFromAverage < 0) diffFromAverage * (-1)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "평균보다 ",
            fontSize = 12.sp,
            color = colorResource(id = R.color.main_gray),
            fontWeight = FontWeight(500)
        )
        Text(
            text = TimeUtils.convertSecondsToTime(diffFromAverage),
            fontSize = 15.sp,
            color = if (less) colorResource(id = R.color.blue_scale2) else colorResource(id = R.color.red_scale2),
            fontWeight = FontWeight(700)
        )
        Text(
            text = if (less) " 만큼 덜 공부했어요!" else " 만큼 더 공부했어요!",
            fontSize = 12.sp,
            color = colorResource(id = R.color.main_gray),
            fontWeight = FontWeight(500)
        )
    }
}

// ------------------------------------ Previews ------------------------------------

@Preview
@Composable
private fun PreviewDrawUserStudyTimes() {
    DrawUserStudyTimes(isBelowAverage = true)
}

@Preview(showBackground = true)
@Composable
private fun PreviewRankingViewUnderAverage() {
    RankingView(context = LocalContext.current)
}

@Preview(showBackground = true)
@Composable
private fun PreviewRankingViewAboveAverage() {
    RankingView(context = LocalContext.current)
}

//@Preview(showBackground = true)
//@Composable
//private fun PreivewRankingView() {
//    RankingView()
//}
