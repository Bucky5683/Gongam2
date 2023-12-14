package com.example.gongam.ui.main.mainSubViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gongam.R

@Composable
fun TimerView() {
    ContentsTitleView("타이머", false)
    Spacer(modifier = Modifier.height(13.5.dp))
    TimeButton(icon = "⏰", title = "타이머")
    TimeButton(icon = "⏱️", title = "스톱워치")
}

@Composable
fun TimeButton(icon: String, title: String) {
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
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 10.dp))
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

@Preview(showBackground = true)
@Composable
private fun PreviewTimeButton() {
    TimeButton(icon = "⏰", title = "타이머")
}