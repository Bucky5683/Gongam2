package com.cono.gongam.ui.timer

import android.os.Build
import android.os.Bundle
import android.widget.NumberPicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.chargemap.compose.numberpicker.ListItemPicker
import com.chargemap.compose.numberpicker.NumberPicker
import com.cono.gongam.R
import com.cono.gongam.ui.CircleTextButton
import com.cono.gongam.ui.SpacedEdgeTextsWithCenterVertically
import com.cono.gongam.ui.TopTitle
import com.cono.gongam.ui.theme.GongamTheme

class TimerActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GongamTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = colorResource(id = R.color.main_gray)
                ) {
                    PreviewTimer()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewTimer() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.main_gray))
    ) {
        TopTitle(backgroundColor = colorResource(id = R.color.main_gray), textColor = colorResource(id = R.color.white), centerText = "타이머", dividerLineColor = colorResource(id = R.color.gray_line))
        TimerScreen()
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerScreen() {
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
        TimerComponent()
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
                rightText = "99:99:99", rightTextSize = 18.sp, rightTextColor = Color.White, rightTextWeight = FontWeight(400),
                horizontalPaddingVal = 0.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            RemainingTimeText(99, 99, 99)
        }
        Spacer(modifier = Modifier.weight(1f))
        CircleTextButton(buttonText = "START", nextBtnOnClick = {}, buttonColor = colorResource(id = R.color.blue_scale2))
        Spacer(modifier = Modifier.weight(1f))
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerComponent() {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    var second by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        NumberPicker(value = hour, onValueChange = {hour = it}, range = 0..99,
//            dividersColor = colorResource(id = R.color.transparent),
//            textStyle = TextStyle(
//                color = Color.White,
//                fontSize = 48.sp,
//                fontWeight = FontWeight(700),),
//            modifier = Modifier.padding(30.dp)
//        )
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
fun RemainingTimeText(h: Int, m: Int, s: Int) {
    Row() {
        Text(
            text = "목표까지 $h:$m:$s 남았어요",
            color = colorResource(id = R.color.gray_scale1),
            fontWeight = FontWeight(700),
            textDecoration = TextDecoration.Underline
        )
    }
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
@Preview
@Composable
fun PreviewTimerComponent() {
    TimerComponent()
}