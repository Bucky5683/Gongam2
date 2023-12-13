package com.example.gongam

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.gongam.ui.theme.GongamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PreviewMain()
                }
            }
        }

    }
}

@Composable
fun TopView() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = colorResource(id = R.color.main_gray)
    ) {
        Column(
            verticalArrangement = Arrangement.Top, // 위로 정렬
            horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬
        ) {
            Spacer(modifier = Modifier.height(13.7.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_test_profile),
                    contentDescription = "profile Img",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp),
                )
                Spacer(modifier = Modifier.width(15.dp))
            }
            Text(
                text = "오늘_공부한_시간",
                color = Color.White,
                modifier = Modifier.padding(top = 28.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                textDecoration = TextDecoration.Underline,
            )
            Text(
                text = "99:99:99",
                color = Color.White,
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(700),
            )
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .padding(bottom = 52.dp)
                    .background(
                        color = colorResource(R.color.gray_scale1),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "\uD83D\uDD25",
                        modifier = Modifier
                            .padding(start = 6.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "목표까지",
                        color = colorResource(id = R.color.main_gray),
                        modifier = Modifier
                            .padding(start = 5.dp, top = 6.dp, bottom = 6.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight(400),
                    )
                    Text(
                        text = "99:99:99",
                        color = colorResource(id = R.color.main_gray),
                        modifier = Modifier
                            .padding(start = 5.dp, top = 6.dp, bottom = 6.dp, end = 5.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight(700),
                    )
                    Text(
                        text = "\uD83D\uDD25",
                        modifier = Modifier
                            .padding(end = 6.dp),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun ContentsTitleView(title: String, showMoreButton: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight(700),
                color = colorResource(id = R.color.gray_scale2),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(top = 30.dp, start = 40.dp, bottom = 2.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (showMoreButton) {
                Text(
                    text = "더보기 >",
                    modifier = Modifier.padding(end = 42.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = colorResource(id = R.color.gray_scale1),
                )
            }

        }
    }
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
                    spotColor = colorResource(id = R.color.shadow_gray),
                    ambientColor = colorResource(id = R.color.shadow_gray),
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .fillMaxWidth()
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 10.dp))
        ) {
            Row() {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                    ) {
                        Row() {
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
                    }
                    Spacer(modifier = Modifier.height(15.dp))
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

@Preview
@Composable
fun PreivewTopView() {
    TopView()
}

@Preview
@Composable
fun PreviewTimerButton() {
    TimeButton(icon = "⏰", title = "타이머")
}

@Preview
@Composable
fun PreviewTimerView() {
    ContentsTitleView("타이머", false)
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMain() {
    Column(
        modifier = Modifier
            .background(
                color = Color.White
            )
    ) {
        TopView()
        ContentsTitleView("타이머", false)
        Spacer(modifier = Modifier.height(13.5.dp))
        TimeButton(icon = "⏰", title = "타이머")
        TimeButton(icon = "⏱️", title = "스톱워치")
        Spacer(modifier = Modifier.height(42.5.dp))
        ContentsTitleView("랭킹", true)
    }
}