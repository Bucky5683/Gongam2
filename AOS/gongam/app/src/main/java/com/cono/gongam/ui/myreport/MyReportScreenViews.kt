package com.cono.gongam.ui.myreport

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cono.gongam.R
import com.cono.gongam.data.StudyDates
import com.cono.gongam.data.viewmodels.StudyDatesViewModel
import com.cono.gongam.ui.TopTitle
import com.cono.gongam.ui.main.setStatusBarColor
import com.cono.gongam.utils.TimeUtils
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf

@Composable
fun MyReportScreen(
    studyDatesViewModel: StudyDatesViewModel
) {
//    val sharedPreferencesUtil = SharedPreferencesUtil(context = LocalContext.current)
//    val studyDatesViewModel: StudyDatesViewModel = viewModel()
//    studyDatesViewModel.updateStudyDates(sharedPreferencesUtil.getUid())
    val thisWeekStudyDate by studyDatesViewModel.thisWeekStudyDate.observeAsState()
    val context: Context = LocalContext.current
    val statusBarColor = colorResource(id = R.color.white)
    SideEffect {
        setStatusBarColor(context, statusBarColor)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        TopTitle(backgroundColor = colorResource(id = R.color.white), textColor = colorResource(id = R.color.black), centerText = "마이 리포트", dividerLineColor = colorResource(id = R.color.gray_line2), backPress = true)
        DateRangeText()
        if (thisWeekStudyDate != null) {
            val thisWeekStudyDataWithDay = studyDatesViewModel.getThisWeekStudyDataWithDay()
            WeekChart(thisWeekStudyDataWithDay)
            DayBlocks(thisWeekStudyDataWithDay)
        }
    }
}

@Composable
fun WeekChart(thisWeekStudyDataWithDay: MutableList<Pair<Int, StudyDates>>) {
    val bottomAxisLabel = listOf("S", "M", "T", "W", "T", "F", "S")
    val temp: MutableList<Int> = List(7) {0}.toMutableList()
    thisWeekStudyDataWithDay.forEach {
        if (it.second.totalStudyTime > 0) {
            temp[it.first] = it.second.totalStudyTime
        }
    }
    val chartEntryModel = entryModelOf(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6])

    Chart(
        chart = columnChart(
            columns = listOf(
                lineComponent(
                    color = colorResource(id = R.color.gray_scale1),
                    thickness = 20.dp,
                )
            ),
            spacing = 0.dp,
            innerSpacing = 0.dp,
        ),
        model = chartEntryModel,
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                bottomAxisLabel[value.toInt()]
            },
            guideline = LineComponent(color = 0),
        ),
        modifier = Modifier.padding(horizontal = 37.dp)
//            .height(300.dp)
    )
}

@Composable
fun DateRangeText() {
    Text(
        text = "12월 25일 ~ 12월 31일",
        color = Color.Black,
        fontWeight = FontWeight(700),
        fontSize = 15.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp)
    )
}

@Composable
fun DayBlock(dayString: String, totalStudyHours: String, timerStudyHours: String, stopWatchStudyHours: String) {
    Card(
        modifier = Modifier
            .padding(horizontal = 46.dp, vertical = 5.dp)
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = colorResource(id = R.color.blue_scale1),
                )
        ) {
            Text(text = dayString,
                style = TextStyle(
                    fontSize = 20.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(700),
                    color = colorResource(id = R.color.main_gray),
                ),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .width(20.dp))
            Spacer(modifier = Modifier.width(30.dp))
            Text(text = totalStudyHours,
                style = TextStyle(
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(700),
                    color = colorResource(id = R.color.main_gray),
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Column {
                val textStyle = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(700),
                    color = colorResource(id = R.color.main_gray),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "타이머", style = textStyle)
                Text(text = "스탑워치", style = textStyle)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                val textStyle = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight(400),
                    color = colorResource(id = R.color.main_gray),
                )
                Text(text = timerStudyHours, style = textStyle,
                    modifier = Modifier.width(53.dp))
                Text(text = stopWatchStudyHours, style = textStyle,
                    modifier = Modifier.width(53.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
fun DayBlocks(thisWeekStudyDataWithDay: MutableList<Pair<Int, StudyDates>>) {
    val totalStudyHoursArray: MutableList<String> = List(7) {"-"}.toMutableList()
    val timerStudyHoursArray: MutableList<String> = List(7) {"-"}.toMutableList()
    val stopWatchStudyHoursArray: MutableList<String> = List(7) {"-"}.toMutableList()

    thisWeekStudyDataWithDay.forEach {
        val dayInt = it.first
        val totalStudyHours = TimeUtils.convertSecondsToTimeInString(it.second.totalStudyTime)
        totalStudyHoursArray[dayInt] = totalStudyHours
        val timerStudyHours = TimeUtils.convertSecondsToTimeInString(it.second.timerStudyTime)
        timerStudyHoursArray[dayInt] = timerStudyHours
        val stopWatchStudyHours = TimeUtils.convertSecondsToTimeInString(it.second.stopwatchStudyTime)
        stopWatchStudyHoursArray[dayInt] = stopWatchStudyHours
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        DayBlock("S", totalStudyHoursArray[0], timerStudyHoursArray[0], stopWatchStudyHoursArray[0])
        DayBlock("M", totalStudyHoursArray[1], timerStudyHoursArray[1], stopWatchStudyHoursArray[1])
        DayBlock("T", totalStudyHoursArray[2], timerStudyHoursArray[2], stopWatchStudyHoursArray[2])
        DayBlock("W", totalStudyHoursArray[3], timerStudyHoursArray[3], stopWatchStudyHoursArray[3])
        DayBlock("T", totalStudyHoursArray[4], timerStudyHoursArray[4], stopWatchStudyHoursArray[4])
        DayBlock("F", totalStudyHoursArray[5], timerStudyHoursArray[5], stopWatchStudyHoursArray[5])
        DayBlock("S", totalStudyHoursArray[6], timerStudyHoursArray[6], stopWatchStudyHoursArray[6])
        Spacer(modifier = Modifier.height(50.dp))
    }
}
