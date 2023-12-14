package com.example.gongam.ui.main.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gongam.R

@Composable
fun RankingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(37.dp))
        Box(
//                Modifier.fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .width(61.dp)
                    .height(215.dp)
                    .padding(start = 7.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.main_gray_70alpha),
                                colorResource(id = R.color.main_gray)
                            ),
                            startY = 0f,
                            endY = 215f
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {

            }
            Box(
                modifier = Modifier
                    .width(233.dp)
//                        .fillMaxHeight(),
            ) {

            }
            Box(
//                    Modifier.fillMaxHeight(),
            ) {
                // 평균 공부시간 ----------------------------------------
                Row(
                    modifier = Modifier
                        .height(215.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .shadow(
                                ambientColor = colorResource(id = R.color.shadow_gray2),
                                spotColor = colorResource(id = R.color.shadow_gray2),
                                elevation = 5.dp,
                                shape = RoundedCornerShape(size = 10.dp)
                            ),
                    ) {
                        Box(
                            modifier = Modifier
                                .width(68.dp)
                                .height(11.dp)
                                .background(
                                    color = colorResource(id = R.color.gray_scale1),
                                    shape = RoundedCornerShape(10.dp)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(11.dp))
                    Box(
                        modifier = Modifier
                            .shadow(
                                ambientColor = colorResource(id = R.color.shadow_gray2),
                                spotColor = colorResource(id = R.color.shadow_gray2),
                                elevation = 5.dp,
                                shape = RoundedCornerShape(size = 10.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .height(18.dp)
                                .background(
                                    color = colorResource(id = R.color.gray_scale1),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 0.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "평균 공부시간 : ",
                                    color = Color.White,
                                    fontWeight = FontWeight(500),
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center,
                                )
                                Text(
                                    text = "9999:99:99",
                                    color = Color.White,
                                    fontWeight = FontWeight(700),
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
                // 유저 등수 ----------------------------------------
                Column(
                    modifier = Modifier
                        .height(215.dp),
                ) {
                    // TODO :: 유저의 등수에 따라 Spacer 높이 조절
                    Spacer(modifier = Modifier.height(130.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.shadow(
                                ambientColor = colorResource(id = R.color.shadow_gray2),
                                spotColor = colorResource(id = R.color.shadow_gray2),
                                elevation = 5.dp,
                                shape = RoundedCornerShape(size = 10.dp)
                            ),
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(68.dp)
                                    .height(11.dp)
                                    .background(
                                        color = colorResource(id = R.color.blue_scale1),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.width(11.dp))
                        Box(
                            modifier = Modifier.shadow(
                                ambientColor = colorResource(id = R.color.shadow_gray2),
                                spotColor = colorResource(id = R.color.shadow_gray2),
                                elevation = 5.dp,
                                shape = RoundedCornerShape(size = 10.dp)
                            ),
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(46.dp)
                                    .background(
                                        color = colorResource(id = R.color.blue_scale1),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 0.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "OOO님의 등수",
                                        color = colorResource(id = R.color.main_gray),
                                        fontWeight = FontWeight(400),
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                    )
                                    Spacer(modifier = Modifier.width(13.dp))
                                    Text(
                                        text = "999+",
                                        color = colorResource(id = R.color.main_gray),
                                        fontWeight = FontWeight(700),
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "평균보다 ",
                fontSize = 12.sp,
                color = colorResource(id = R.color.main_gray),
                fontWeight = FontWeight(500)
            )
            Text(
                text = "999:99:99",
                fontSize = 15.sp,
                color = colorResource(id = R.color.blue_scale2),
                fontWeight = FontWeight(700)
            )
            Text(
                text = " 만큼 덜 공부했어요!",
                fontSize = 12.sp,
                color = colorResource(id = R.color.main_gray),
                fontWeight = FontWeight(500)
            )
        }
        Spacer(modifier = Modifier.height(31.dp))
    }

}
