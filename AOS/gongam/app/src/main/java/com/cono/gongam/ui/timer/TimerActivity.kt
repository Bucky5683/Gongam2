package com.cono.gongam.ui.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cono.gongam.R
import com.cono.gongam.ui.theme.GongamTheme

class TimerActivity : ComponentActivity() {
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