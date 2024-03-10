package com.cono.gongam.ui.timer

import android.os.Build
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cono.gongam.R
import com.cono.gongam.data.viewmodels.TimerViewModel
import com.cono.gongam.data.User
import com.cono.gongam.data.viewmodels.UserViewModel
import com.cono.gongam.ui.SpacedEdgeTextsWithCenterVertically
import com.cono.gongam.ui.TopTitle
import com.cono.gongam.utils.StringUtil
import com.cono.gongam.utils.TimeUtils

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerScreen(
    userViewModel: UserViewModel, timerViewModel: TimerViewModel, uid: String
) {
//    val timerViewModel: TimerViewModel = viewModel()
    DisposableEffect(Unit) {
        onDispose {
            timerViewModel.updateSecondsInDatabase(uid)
            timerViewModel.setSumSeconds(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.main_gray))
    ) {
        TopTitle(backgroundColor = colorResource(id = R.color.main_gray), textColor = colorResource(id = R.color.white), centerText = "타이머", dividerLineColor = colorResource(id = R.color.gray_line), backPress = true)
        userViewModel.getCurrentUser()?.let {
            Timer(it, userViewModel, timerViewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Timer(
    user: User,
    userViewModel: UserViewModel,
    timerViewModel: TimerViewModel
) {
    val todayStudyTime = user.todayStudyTime ?: 0
    val goalStudyTime = user.goalStudyTime ?: 0
    val sumSeconds by timerViewModel.sumSeconds.observeAsState(initial = 0)
    val isStopped by timerViewModel.isStopped.observeAsState(initial = false)
    val remainingTime by timerViewModel.remainingTime.observeAsState(initial = Triple(0, 0, 0))
    if (remainingTime == Triple(0, 0, 0)) {
        timerViewModel.setIsStopped(true)
    }

    val remainGoalTime = if (goalStudyTime == 0 || goalStudyTime - sumSeconds - todayStudyTime < 0) 0 else goalStudyTime - sumSeconds - todayStudyTime

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.main_gray)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(75.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(67.dp))
            Text(text = "\uD83D\uDCDA\uD83D\uDE34\uD83D\uDCDA",
                fontSize = 48.sp,)
        }
        Spacer(modifier = Modifier.height(25.dp))
        if (isStopped) {
            TimerSpinner(timerViewModel = timerViewModel)
        } else {
            TimerTickingText(timerViewModel = timerViewModel)
        }
        Spacer(modifier = Modifier
            .height(50.dp)
            .background(color = colorResource(id = R.color.main_gray)))

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
            RemainingTimeText(remainGoalTime)
        }
        Spacer(modifier = Modifier.weight(1f))
        StartStopButton(timerViewModel = timerViewModel, remainingTime = remainingTime)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun StartStopButton(timerViewModel: TimerViewModel, remainingTime: Triple<Int, Int, Int>) {
//    val timerViewModel: TimerViewModel = viewModel()
//    val remainingTime by timerViewModel.remainingTime.observeAsState(initial = Triple(0, 0, 0))
    val isStopped by timerViewModel.isStopped.observeAsState(initial = false)

    if (remainingTime == Triple(0, 0, 0)) {
        timerViewModel.setIsStopped(true)
    }

    Button(
        onClick = {
            timerViewModel.setIsStopped(!isStopped)
            timerViewModel.startCountDown()
        },
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isStopped) colorResource(id = R.color.blue_scale2) else colorResource(id = R.color.red_scale2),
            contentColor = colorResource(id = R.color.white)
        ),
    ) {
        Text(text = if (isStopped) "START" else "STOP", fontSize = 18.sp, fontWeight = FontWeight(700))
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerSpinner(
    timerViewModel: TimerViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AndroidView(
                factory = {context ->
                    NumberPicker(context).apply {
                        minValue = 0
                        maxValue = 99
                        textColor = Color.White.toArgb()
                        textSize = 130f
                        value = timerViewModel.hour
                        selectionDividerHeight = 0
                        setOnValueChangedListener { _, _, newVal ->
                            timerViewModel.setHour(newVal)
                        }
                    }
                },
                modifier = Modifier
                    .width(65.dp)
                    .wrapContentHeight()
            )
            Text(
                text = "시간",
                fontWeight = FontWeight(700),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
        }

        Text(
            text = ":",
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight(700),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 22.dp)
                .width(35.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AndroidView(
                factory = {context ->
                    NumberPicker(context).apply {
                        minValue = 0
                        maxValue = 59
                        textColor = Color.White.toArgb()
                        textSize = 130f
                        value = timerViewModel.minute
                        selectionDividerHeight = 0
                        setOnValueChangedListener { _, _, newVal ->
                            timerViewModel.setMinute(newVal)
                        }
                    }
                },
                modifier = Modifier
                    .width(65.dp)
                    .wrapContentHeight()
            )
            Text(
                text = "분",
                fontWeight = FontWeight(700),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                modifier = Modifier.background(color = colorResource(id = R.color.main_gray))
            )
        }

        Text(
            text = ":",
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight(700),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 22.dp)
                .width(35.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AndroidView(
                factory = {context ->
                    NumberPicker(context).apply {
                        minValue = 0
                        maxValue = 59
                        textColor = Color.White.toArgb()
                        textSize = 130f
                        value = timerViewModel.second
                        selectionDividerHeight = 0
                        setOnValueChangedListener { _, _, newVal ->
                            timerViewModel.setSecond(newVal)
                        }
                    }
                },
                modifier = Modifier
                    .width(65.dp)
                    .wrapContentHeight()
            )
            Text(
                text = "초",
                fontWeight = FontWeight(700),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TimerTickingText(
    timerViewModel: TimerViewModel
) {
    val remainingTime by timerViewModel.remainingTime.observeAsState(initial = Triple(0, 0, 0))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TickingHMS(time = StringUtil.convertToTwoDigitString(remainingTime.first), timeText = "시간")
        TimeColonText()
        TickingHMS(time = StringUtil.convertToTwoDigitString(remainingTime.second), timeText = "분")
        TimeColonText()
        TickingHMS(time = StringUtil.convertToTwoDigitString(remainingTime.third), timeText = "초")
    }
}

@Composable
fun TickingHMS(time: String, timeText: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight(700),
            textAlign = TextAlign.Center,
            modifier = Modifier.width(64.dp)
        )
        Text(
            text = timeText,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight(700),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun TimeColonText() {
    Text(
        text = ":",
        color = Color.White,
        fontSize = 48.sp,
        fontWeight = FontWeight(700),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(bottom = 22.dp)
            .width(35.dp)
    )
}

@Composable
fun RemainingTimeText(remainGoalTime: Int) {
    Text(
        text = "목표까지 ${TimeUtils.convertSecondsToTimeInString(remainGoalTime)} 남았어요",
        color = colorResource(id = R.color.gray_scale1),
        fontWeight = FontWeight(700),
        textDecoration = TextDecoration.Underline
    )
}