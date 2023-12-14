package com.example.gongam.ui.main.kit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gongam.ui.main.kit.ContentsTitleView

@Composable
fun MyReportView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ContentsTitleView(title = "마이 리포트", showMoreButton = true)

    }
}