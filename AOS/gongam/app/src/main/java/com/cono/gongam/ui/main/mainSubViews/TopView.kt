package com.cono.gongam.ui.main.mainSubViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cono.gongam.R

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
            Spacer(modifier = Modifier.height(45.dp)) // 상태바로 잘리는 부분을 위한 spacer
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