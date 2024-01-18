package com.cono.gongam.ui.timer

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cono.gongam.R
import com.cono.gongam.data.TimerViewModel
import com.cono.gongam.data.UserViewModel
import com.cono.gongam.ui.CircleTextButton
import com.cono.gongam.ui.SpacedEdgeTextsWithCenterVertically
import com.cono.gongam.ui.TopTitle
import com.cono.gongam.ui.theme.GongamTheme
import com.cono.gongam.utils.SharedPreferencesUtil
import com.cono.gongam.utils.TimeUtils

class TimerActivity : ComponentActivity() {
    private lateinit var sharedPreferencesUtil: SharedPreferencesUtil
    
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesUtil = SharedPreferencesUtil(this)

        setContent {
            GongamTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = colorResource(id = R.color.main_gray)
                ) {
                    TimerActivityScreen(sharedPreferencesUtil)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerActivityScreen(sharedPreferencesUtil: SharedPreferencesUtil) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.main_gray))
    ) {
        TopTitle(backgroundColor = colorResource(id = R.color.main_gray), textColor = colorResource(id = R.color.white), centerText = "타이머", dividerLineColor = colorResource(id = R.color.gray_line))
        TimerScreen(sharedPreferencesUtil)
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerScreen(sharedPreferencesUtil: SharedPreferencesUtil) {
    val timerViewModel: TimerViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    userViewModel.setCurrentUser(sharedPreferencesUtil.getUser())
    val user = userViewModel.getCurrentUser()
    val goalStudyTime = user?.goalStudyTime ?: 0
    val sumSeconds by timerViewModel.sumSeconds.observeAsState(initial = 0)
    val remainGoalTime = if (goalStudyTime == 0) 0 else if (goalStudyTime - sumSeconds < 0) 0 else goalStudyTime - sumSeconds

    var isTimerSpinnerVisible by remember { mutableStateOf(true) }
    var isStopped by remember { mutableStateOf(false) }

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
        if (isTimerSpinnerVisible) {
            TimerSpinner()
        } else {
            TimerTickingText()
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
                rightText = TimeUtils.convertSecondsToTime(goalStudyTime ?: 0), rightTextSize = 18.sp, rightTextColor = Color.White, rightTextWeight = FontWeight(400),
                horizontalPaddingVal = 0.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            RemainingTimeText(remainGoalTime)
        }
        Spacer(modifier = Modifier.weight(1f))
        StartStopButton(
            btnOnClick = {
                timerViewModel.startCountDown()
                isTimerSpinnerVisible = false
                isStopped = !isStopped
            })
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun StartStopButton(btnOnClick: () -> Unit) {
    var isButtonClicked by remember { mutableStateOf(false) }

    Button(
        onClick = {
            btnOnClick()
            isButtonClicked = !isButtonClicked
        },
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (!isButtonClicked) colorResource(id = R.color.blue_scale2) else colorResource(
                id = R.color.red_scale2
            ),
            contentColor = colorResource(id = R.color.white)
        ),
    ) {
        Text(text = if (!isButtonClicked) "START" else "STOP", fontSize = 18.sp, fontWeight = FontWeight(700))
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerSpinner() {
    val timerViewModel: TimerViewModel = viewModel()

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
fun TimerTickingText() {
    val timerViewModel: TimerViewModel = viewModel()
    val remainingTime by timerViewModel.remainingTime.observeAsState(initial = Triple(0, 0, 0))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TickingHMS(time = remainingTime.first, timeText = "시간")
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
        TickingHMS(time = remainingTime.second, timeText = "분")
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
        TickingHMS(time = remainingTime.third, timeText = "초")
    }
}

@Composable
fun TickingHMS(time: Int, timeText: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$time",
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
fun RemainingTimeText(remainGoalTime: Int) {
    Text(
        text = "목표까지 ${TimeUtils.convertSecondsToTime(remainGoalTime)} 남았어요",
        color = colorResource(id = R.color.gray_scale1),
        fontWeight = FontWeight(700),
        textDecoration = TextDecoration.Underline
    )
}

@Composable
fun SliderValue(value: Float, onValueChange: (Float) -> Unit, label: String) {
    Column {
        Text(text = label)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..59f,
            steps = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "$value")
    }
}

// ------------------------------------ Previews ------------------------------------

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewTimerActivityScreen() {
    TimerActivityScreen(SharedPreferencesUtil(LocalContext.current))
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
fun PreviewTimerComponent() {
    TimerSpinner()
}

@Preview @Composable
fun PreveiwTimerTickingText() {
    TimerTickingText()
}