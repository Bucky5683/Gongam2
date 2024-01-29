package com.cono.gongam.ui.main.mainSubViews

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cono.gongam.R
import com.cono.gongam.data.TodoScreen
import com.cono.gongam.ui.myreport.MyReportActivity
import com.cono.gongam.ui.ranking.RankingActivity

@Composable
fun ContentsTitleView(title: String, showMoreButton: Boolean, context: Context? = null, navController: NavController? = null) {
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
                    .padding(top = 15.dp, start = 40.dp, bottom = 2.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (showMoreButton) {
                Text(
                    text = "더보기 >",
                    modifier = Modifier.padding(end = 42.dp)
                        .clickable(
                            interactionSource = remember{ MutableInteractionSource() },
                            indication = null
                        )  {
                            if (title == "랭킹") {
                                navController?.navigate(TodoScreen.Ranking.name)
                            } else if (title == "마이 리포트"){
                                navController?.navigate(TodoScreen.MyReport.name)
                            }
                        },
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = colorResource(id = R.color.gray_scale1),
                )
            }

        }
    }
}

@Preview
@Composable
fun PreViewContentsTitleView() {
    ContentsTitleView(title = "테스트 타이틀", showMoreButton = true)
}
