package com.cono.gongam.ui.main.mainSubViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cono.gongam.R

@Composable
fun MyReportView(sunH: Int, monH: Int, tueH: Int, wedH: Int, thuH: Int, friH: Int, satH: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        ContentsTitleView(title = "마이 리포트", showMoreButton = true)
        Spacer(modifier = Modifier.height(29.dp))
        WeekStudyHours(sunH, monH, tueH, wedH, thuH, friH, satH)
        Spacer(modifier = Modifier.height(20.dp))
        SetMyReportAverageText()
        Spacer(modifier = Modifier.height(22.dp))
    }
}

@Composable
private fun SetMyReportAverageText() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "이번 주에 평균 ",
            fontSize = 12.sp,
            fontWeight = FontWeight(500),
            color = colorResource(id = R.color.main_gray),
        )
        // TODO :: 평균값 할당
        Text(
            text = "99:99:99",
            fontSize = 15.sp,
            fontWeight = FontWeight(700),
            color = colorResource(id = R.color.blue_scale2),
        )
        Text(
            text = " 만큼 공부했어요!",
            fontSize = 12.sp,
            fontWeight = FontWeight(500),
            color = colorResource(id = R.color.main_gray),
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun WeekStudyHours(sunH: Int, monH: Int, tueH: Int, wedH: Int, thuH: Int, friH: Int, satH: Int) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            DayStudyHoursView("S", sunH)
            Spacer(modifier = Modifier.width(8.dp))
            DayStudyHoursView("M", monH)
            Spacer(modifier = Modifier.width(8.dp))
            DayStudyHoursView("T", tueH)
            Spacer(modifier = Modifier.width(8.dp))
            DayStudyHoursView("W", wedH)
            Spacer(modifier = Modifier.weight(1f))
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))
            DayStudyHoursView("T", thuH)
            Spacer(modifier = Modifier.width(8.dp))
            DayStudyHoursView("F", friH)
            Spacer(modifier = Modifier.width(8.dp))
            DayStudyHoursView("S", satH)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun DayStudyHoursView(day: String, hours: Int) {
    Box(
        modifier = Modifier
            .width(58.dp)
            .height(58.dp)
    ) {
        Image(
            // 이미지에 상단0, 좌4, 우4, 하단8 여백 있음
            painter = painterResource(id = R.drawable.img_main_day_study_hours), contentDescription = "day hours",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(58.dp)
                .height(58.dp)
        )
        Column(
            modifier = Modifier
                .width(58.dp)
                .height(50.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = day,
                fontSize = 15.sp,
                fontWeight = FontWeight(700),
                color = colorResource(id = R.color.gray_scale2),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = if (hours < 0) {
                    "- ${-1*hours}h"
                } else {
                    "+ ${hours}h"
                  },
                fontSize = 10.sp,
                fontWeight = FontWeight(400),
                color = if (hours < 0) colorResource(id = R.color.blue_scale2) else colorResource(id = R.color.red_scale1),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

// ------------------------------------ Previews ------------------------------------

@Preview
@Composable
private fun PreviewMyReportView() {
    MyReportView(-99, -99, -99, 99, 99, -99, -99)
}

@Preview(showBackground = true)
@Composable
private fun PreviewDayStudyHoursView() {
    DayStudyHoursView("S", -99)
}