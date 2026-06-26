package com.jeonc.sajuAndUnse.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.data.TodayFortuneGenerator
import com.jeonc.sajuAndUnse.ui.theme.*
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayFortuneScreen(navController: NavController) {
    var birthYear by remember { mutableIntStateOf(1990) }
    var birthMonth by remember { mutableIntStateOf(1) }
    var birthDay by remember { mutableIntStateOf(1) }
    var showResult by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }
    var monthExpanded by remember { mutableStateOf(false) }
    var dayExpanded by remember { mutableStateOf(false) }

    val cal = Calendar.getInstance()
    val todayStr = "${cal.get(Calendar.YEAR)}년 ${cal.get(Calendar.MONTH) + 1}월 ${cal.get(Calendar.DAY_OF_MONTH)}일"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("오늘의 운세", fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(todayStr, fontSize = 16.sp, color = Primary, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(16.dp))

            if (!showResult) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "생년월일을 입력하세요",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        DropdownSelector(
                            label = "년도",
                            value = "${birthYear}년",
                            expanded = yearExpanded,
                            onExpandChange = { yearExpanded = it },
                            items = (1940..2025).toList(),
                            selectedItem = birthYear,
                            onItemSelected = { birthYear = it; yearExpanded = false },
                            itemLabel = { "${it}년" }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DropdownSelector(
                            label = "월",
                            value = "${birthMonth}월",
                            expanded = monthExpanded,
                            onExpandChange = { monthExpanded = it },
                            items = (1..12).toList(),
                            selectedItem = birthMonth,
                            onItemSelected = { birthMonth = it; monthExpanded = false },
                            itemLabel = { "${it}월" }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DropdownSelector(
                            label = "일",
                            value = "${birthDay}일",
                            expanded = dayExpanded,
                            onExpandChange = { dayExpanded = it },
                            items = (1..31).toList(),
                            selectedItem = birthDay,
                            onItemSelected = { birthDay = it; dayExpanded = false },
                            itemLabel = { "${it}일" }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { showResult = true },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) {
                            Text("운세 보기", fontSize = 16.sp)
                        }
                    }
                }
            } else {
                val fortune = TodayFortuneGenerator.generate(birthYear, birthMonth, birthDay)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("행운 정보", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            LuckyItem("행운의 색", fortune.lucky.color)
                            LuckyItem("행운의 숫자", "${fortune.lucky.number}")
                            LuckyItem("행운의 방향", fortune.lucky.direction)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                FortuneCard("종합운", fortune.overall)
                FortuneCard("애정운", fortune.love)
                FortuneCard("금전운", fortune.money)
                FortuneCard("건강운", fortune.health)

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showResult = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("다시 보기")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LuckyItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Background)
            .padding(12.dp)
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
    }
}

@Composable
fun FortuneCard(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(content, fontSize = 14.sp, lineHeight = 22.sp, color = Color.DarkGray)
        }
    }
}
