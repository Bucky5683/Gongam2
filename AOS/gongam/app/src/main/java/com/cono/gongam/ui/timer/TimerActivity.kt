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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.chargemap.compose.numberpicker.ListItemPicker
import com.chargemap.compose.numberpicker.NumberPicker
import com.cono.gongam.R
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
        TimerTitle()
        GrayLine()
        TimerScreen()
    }
}

@Composable
fun TimerTitle() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(color = colorResource(id = R.color.main_gray))
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.CenterStart)
        ) {
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Main",
                color = Color.White,
                fontWeight = FontWeight(400),
                fontSize = 17.sp,
            )
        }
        Text(
            text = "타이머",
            color = Color.White,
            fontWeight = FontWeight(600),
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun GrayLine() {
    Box(
        modifier = Modifier
            .height(0.25.dp)
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.gray_line))
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.main_gray))
    ) {
        Spacer(modifier = Modifier.height(75.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(67.dp))
            Text(text = "\uD83D\uDCDA\uD83D\uDE34\uD83D\uDCDA",
                fontSize = 48.sp,)
        }
        Spacer(modifier = Modifier.height(25.dp))
        TimerComponent()
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TimerComponent() {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    var second by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
//        NumberPicker(value = hour, onValueChange = {hour = it}, range = 0..99,
//            dividersColor = colorResource(id = R.color.transparent),
//            textStyle = TextStyle(
//                color = Color.White,
//                fontSize = 48.sp,
//                fontWeight = FontWeight(700),),
//            modifier = Modifier.padding(30.dp)
//        )
        AndroidView(
            factory = {context ->
                NumberPicker(context).apply {
                    minValue = 0
                    maxValue = 10
                    textColor = Color.White.toArgb()
                    textSize = 48f
                }
            }
        )
//        SliderValue(value = hour.toFloat(), onValueChange = { hour = it.toInt() }, label = "Hour")
//        Slider(value = minute.toFloat(), onValueChange = { minute = it.toInt() })
//        Slider(value = second.toFloat(), onValueChange = { second = it.toInt() })
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