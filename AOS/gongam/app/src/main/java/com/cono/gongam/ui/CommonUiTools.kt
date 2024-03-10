package com.cono.gongam.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cono.gongam.R

@Composable
fun TopTitle(backgroundColor: Color, textColor: Color, leftText: String = "Main", centerText: String, dividerLineColor: Color, backPress: Boolean = false) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(color = backgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterStart)
            ) {
                Spacer(Modifier.width(16.dp))
                if (backPress) {
                    Text(
                        text = leftText,
                        color = textColor,
                        fontWeight = FontWeight(400),
                        fontSize = 17.sp,
                        modifier = Modifier
                            .clickable {
                                (context as? OnBackPressedDispatcherOwner)?.onBackPressedDispatcher?.onBackPressed()
                            }
                    )
                } else {
                    Text(
                        text = leftText,
                        color = textColor,
                        fontWeight = FontWeight(400),
                        fontSize = 17.sp,
                    )
                }

            }
            Text(
                text = centerText,
                color = textColor,
                fontWeight = FontWeight(600),
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .height(0.25.dp)
                .fillMaxWidth()
                .background(color = dividerLineColor)
        )
    }
}

@Composable
fun SpacedEdgeTextsWithCenterVertically(
    leftText: String, leftTextSize: TextUnit, leftTextColor: Color, leftTextWeight: FontWeight, setLeftUnderLine: Boolean = false,
    rightText: String, rightTextSize: TextUnit, rightTextColor: Color, rightTextWeight: FontWeight, setRightUnderLine: Boolean = false,
    horizontalPaddingVal: Dp = 19.dp
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = horizontalPaddingVal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = leftText,
            fontSize = leftTextSize,
            color = leftTextColor,
            fontWeight = leftTextWeight,
            textDecoration = if (setLeftUnderLine) TextDecoration.Underline else null
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = rightText,
            fontSize = rightTextSize,
            color = rightTextColor,
            fontWeight = rightTextWeight,
            textDecoration = if (setRightUnderLine) TextDecoration.Underline else null
        )
    }
}

@Composable
fun CircleTextButton(buttonText: String, btnOnClick: () -> Unit, buttonColor: Color) {
    // TODO :: 버튼 shadow 적용
    Button(
        onClick = {
            btnOnClick()
        },
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
    ) {
        Text(text = buttonText, fontSize = 18.sp, fontWeight = FontWeight(700), color = colorResource(id = R.color.white))
    }
}

@Composable
fun ButtonWithQuestionMark(text: String = "null") {
    val context = LocalContext.current
    val helpUrl = "https://generated-ambert-9f2.notion.site/6bf6e5145c344a8d96d99c635864bd1d?v=73a86a97a8014e309003edbd67ebc7fa&pvs=4"
    val termsUrl = "https://generated-ambert-9f2.notion.site/71588b8f9f32421fae736fcae9937a16?v=cccf1449f72f4f069d737d5daea1a672&pvs=4"

    Card(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                if (text == "도움말") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(helpUrl))
                    context.startActivity(intent)
                } else if (text == "이용약관") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl))
                    context.startActivity(intent)
                }
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp),
                painter = painterResource(id = R.drawable.img_question),
                contentDescription = null
            )
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(400),
                    color = Color.Black,
                ),
                modifier = Modifier.padding(start = 6.dp)
            )
        }
    }
}

// ------------------------------------ Previews ------------------------------------

@Preview
@Composable
fun PreviewButtonWithQuestionMark() {
    ButtonWithQuestionMark()
}

@Preview
@Composable
fun PreviewTopTitle() {
    TopTitle(backgroundColor = colorResource(id = R.color.white), textColor = colorResource(id = R.color.black), centerText = "랭킹", dividerLineColor = colorResource(
        id = R.color.gray_line2))
}

@Preview
@Composable
fun PreviewSpacedEdgeTextsWithCenterVertically() {
    SpacedEdgeTextsWithCenterVertically(
        leftText = "오늘 목표", leftTextSize = 24.sp, leftTextColor = colorResource(id = R.color.white), leftTextWeight = FontWeight(700),setLeftUnderLine = true,
        rightText = "99:99:99", rightTextSize = 18.sp, rightTextColor = colorResource(id = R.color.white), rightTextWeight = FontWeight(400)
    )
}