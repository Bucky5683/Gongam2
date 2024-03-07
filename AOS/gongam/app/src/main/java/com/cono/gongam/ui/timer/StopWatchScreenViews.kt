package com.cono.gongam.ui.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cono.gongam.R
import com.cono.gongam.data.viewmodels.StopWatchViewModel
import com.cono.gongam.data.User
import com.cono.gongam.data.viewmodels.TimerViewModel
import com.cono.gongam.data.viewmodels.UserViewModel
import com.cono.gongam.ui.SpacedEdgeTextsWithCenterVertically
import com.cono.gongam.ui.TopTitle
import com.cono.gongam.utils.TimeUtils


@Composable
fun StopWatchScreen(
    userViewModel: UserViewModel,
    stopWatchViewModel: StopWatchViewModel,
    uid: String
) {
//    val stopWatchViewModel: StopWatchViewModel = viewModel()
    DisposableEffect(Unit) {
        onDispose {
            stopWatchViewModel.updateSecondsInDatabase(uid)
            stopWatchViewModel.setSumSeconds(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopTitle(backgroundColor = colorResource(id = R.color.main_gray), textColor = Color.White, centerText = "스톱워치", dividerLineColor = colorResource(id = R.color.gray_line), backPress = true)
        userViewModel.getCurrentUser()?.let {
            StopWatch(it, stopWatchViewModel)
        }
    }
}

@Composable
fun StopWatch(
    user: User,
    stopWatchViewModel: StopWatchViewModel
) {
    val goalStudyTime = user.goalStudyTime ?: 0
    val sumSeconds by stopWatchViewModel.sumSeconds.observeAsState(initial = 0)
    val isStopped by stopWatchViewModel.isStopped.observeAsState(initial = false)

    val remainGoalTime = if (goalStudyTime == 0 || goalStudyTime - sumSeconds < 0) 0 else goalStudyTime - sumSeconds

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.main_gray)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(75.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(67.dp))
            Text(text = "\uD83D\uDCDA\uD83D\uDE47\uD83D\uDCDA",
                fontSize = 48.sp)
        }
        Spacer(modifier = Modifier.height(25.dp))
        StopWatchTickingText(sumSeconds)
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp),
            horizontalAlignment = Alignment.End
        ) {
            SpacedEdgeTextsWithCenterVertically(
                leftText = "오늘 목표", leftTextSize = 18.sp, leftTextColor = Color.White, leftTextWeight = FontWeight(700), setLeftUnderLine = true,
                rightText = TimeUtils.convertSecondsToTimeInString(goalStudyTime), rightTextSize = 18.sp, rightTextColor = Color.White, rightTextWeight = FontWeight(400),
                horizontalPaddingVal = 0.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            RemainingTimeText(remainGoalTime = remainGoalTime)
        }
        Spacer(modifier = Modifier.weight(1f))
        StartStopButton(stopWatchViewModel = stopWatchViewModel, isStopped = isStopped)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun StopWatchTickingText(
    sumSeconds: Int,
) {
    val sumSecondsTriple = TimeUtils.convertSecondsToTimeInTriple(sumSeconds)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TickingHMS(time = sumSecondsTriple.first, timeText = "시간")
        TimeColonText()
        TickingHMS(time = sumSecondsTriple.second, timeText = "분")
        TimeColonText()
        TickingHMS(time = sumSecondsTriple.third, timeText = "초")
    }
}

@Composable
fun StartStopButton(stopWatchViewModel: StopWatchViewModel, isStopped: Boolean) {
    Button(
        onClick = {
            stopWatchViewModel.setIsStopped(!isStopped)
            stopWatchViewModel.startCounting()
        },
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isStopped) colorResource(id = R.color.blue_scale2) else colorResource(id = R.color.red_scale2),
            contentColor = colorResource(id = R.color.white)
        )
    ) {
        Text(text = if (isStopped) "START" else "STOP", fontSize = 18.sp, fontWeight = FontWeight(700))
    }
}

// ------------------------------------ Previews ------------------------------------

@Preview
@Composable
fun PreviewStopWatch() {
    StopWatch(
        user = User(),
        stopWatchViewModel = StopWatchViewModel()
    )
}