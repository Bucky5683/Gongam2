package com.cono.gongam.ui.main.mainSubViews

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cono.gongam.R
import com.cono.gongam.data.TodoScreen
import com.cono.gongam.ui.timer.AIStopWatchActivity

@Composable
fun TimerView(navController: NavController, activity: Activity) {
    Column(
       modifier = Modifier.background(color = Color.White)
    ) {
        ContentsTitleView("타이머", false)
        Spacer(modifier = Modifier.height(13.5.dp))
        TimerButton(icon = "⏰", title = "타이머", navController = navController, activity = activity)
        TimerButton(icon = "⏱️", title = "스톱워치", navController = navController, activity = activity)
        TimerButton(icon = "\uD83D\uDC64", title = "AI 스톱워치", navController = navController, activity = activity)
        Spacer(modifier = Modifier.height(13.5.dp))
    }
}

@Composable
fun TimerButton(icon: String, title: String, navController: NavController, activity: Activity) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.padding(top = 7.5.dp, bottom = 7.5.dp, start = 40.dp, end = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .height(48.dp)
                .shadow(
                    elevation = 10.dp,
                    spotColor = colorResource(id = R.color.shadow_gray3),
                    ambientColor = colorResource(id = R.color.shadow_gray3),
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(size = 10.dp))
                .clickable {
                    if (title == "타이머") {
                        navController.navigate(TodoScreen.Timer.name)
                    } else if (title == "스톱워치") {
                        navController.navigate(TodoScreen.StopWatch.name)
                    } else {
//                        context.startActivity(Intent(context, AIStopWatchActivity::class.java))
//                        val intent = Intent(context, AIStopWatchActivity::class.java)
//                        (context as Activity).startActivityForResult(intent, REQUEST_CODE_AI_STOPWATCH)
                        val intent = Intent(activity, AIStopWatchActivity::class.java)
                        activity.startActivity(intent)
                    }
                }
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        Text(
                            text = icon,
                            modifier = Modifier
                                .padding(start = 20.dp),
                            fontWeight = FontWeight(400),
                        )
                        Text(
                            text = title,
                            modifier = Modifier
                                .padding(start = 10.dp),
                            fontWeight = FontWeight(400),
                            color = colorResource(id = R.color.main_gray)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .width(80.dp)
                        .fillMaxHeight()
                        .background(colorResource(id = R.color.main_gray)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "GO",
                        color = Color.White,
                    )
                    Image(
                        painter = painterResource(id = R.drawable.img_button_go),
                        contentDescription = "Go Button",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(15.dp)
                            .height(15.dp)
                            .padding(start = 5.dp),
                    )
                }
            }

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun PreviewTimeButton() {
//    TimeButton(icon = "⏰", title = "타이머")
//}