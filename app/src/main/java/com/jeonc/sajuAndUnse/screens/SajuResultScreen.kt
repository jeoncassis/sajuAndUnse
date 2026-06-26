package com.jeonc.sajuAndUnse.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.AdManager
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.data.Gender
import com.jeonc.sajuAndUnse.data.SajuCalculator
import com.jeonc.sajuAndUnse.data.SajuInput
import com.jeonc.sajuAndUnse.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SajuResultScreen(
    year: Int, month: Int, day: Int, hour: Int, gender: Int,
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        (context as? Activity)?.let { AdManager.showInterstitialAd(it) }
    }

    val input = SajuInput(
        year = year, month = month, day = day, hour = hour,
        gender = if (gender == 0) Gender.MALE else Gender.FEMALE
    )
    val result = SajuCalculator.calculate(input)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("사주 결과", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = { BannerAdView() },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 사주 팔자 표
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "사주팔자 (四柱八字)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        PillarColumn("시주", result.hourPillar.display)
                        PillarColumn("일주", result.dayPillar.display)
                        PillarColumn("월주", result.monthPillar.display)
                        PillarColumn("년주", result.yearPillar.display)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "일간: ${result.mainElement}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ResultSection("총운", result.fortuneDescription)
            ResultSection("성격", result.personalityDescription)
            ResultSection("직업/재물", result.careerAdvice)
            ResultSection("연애/결혼", result.loveAdvice)
            ResultSection("건강", result.healthAdvice)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PillarColumn(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Background)
            .padding(8.dp)
    ) {
        Text(
            title,
            fontSize = 12.sp,
            color = Primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ResultSection(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                content,
                fontSize = 15.sp,
                lineHeight = 24.sp,
                color = Color.DarkGray
            )
        }
    }
}
