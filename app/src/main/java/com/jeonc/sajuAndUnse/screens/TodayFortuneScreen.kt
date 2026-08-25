package com.jeonc.sajuAndUnse.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.ads.NativeAdComposable
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
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "생년월일을 입력하세요",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NumberSpinner(
                                value = birthYear,
                                min = 1940,
                                max = 2025,
                                label = "년도",
                                onValueChange = { birthYear = it },
                                modifier = Modifier.weight(2f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            NumberSpinner(
                                value = birthMonth,
                                min = 1,
                                max = 12,
                                label = "월",
                                onValueChange = { birthMonth = it },
                                modifier = Modifier.weight(1.2f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            NumberSpinner(
                                value = birthDay,
                                min = 1,
                                max = 31,
                                label = "일",
                                onValueChange = { birthDay = it },
                                modifier = Modifier.weight(1.2f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
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
                val zodiacIndex = TodayFortuneGenerator.getZodiacIndex(birthYear)
                val (zodiacEmoji, zodiacName) = TodayFortuneGenerator.zodiacAnimals[zodiacIndex]
                val zodiacDaily = TodayFortuneGenerator.generateZodiacDaily(zodiacIndex)

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
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("길한 날짜", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            fortune.lucky.dates.forEach { d ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Primary)
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("${d}일", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("길한 시간", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Spacer(modifier = Modifier.height(6.dp))
                        fortune.lucky.times.forEach { time ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(time, fontSize = 13.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                FortuneCard("종합운", fortune.overall)
                FortuneCard("애정운", fortune.love)

                NativeAdComposable()

                FortuneCard("금전운", fortune.money)
                FortuneCard("건강운", fortune.health)

                Spacer(modifier = Modifier.height(12.dp))

                // 띠별 오늘의 운세
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EDF7))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(zodiacEmoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "$zodiacName 오늘의 운세",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            zodiacDaily.todayFortune,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                zodiacDaily.cautionKeywords.forEach { keyword ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(Color(0xFFFFEBEE))
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "⚠ $keyword",
                                            fontSize = 12.sp,
                                            color = Color(0xFFC62828),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(zodiacDaily.luckyColorHex))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    zodiacDaily.luckyColor,
                                    fontSize = 13.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

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
fun NumberSpinner(
    value: Int,
    min: Int,
    max: Int,
    label: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textState by remember { mutableStateOf(value.toString()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        FilledTonalIconButton(
            onClick = {
                val next = if (value >= max) min else value + 1
                onValueChange(next)
                textState = next.toString()
            },
            modifier = Modifier.size(36.dp)
        ) {
            Text("▲", fontSize = 13.sp)
        }
        OutlinedTextField(
            value = textState,
            onValueChange = { str ->
                val digits = str.filter { it.isDigit() }.take(4)
                textState = digits
                digits.toIntOrNull()?.let { num ->
                    if (num in min..max) onValueChange(num)
                }
            },
            modifier = Modifier.width(if (max >= 1000) 88.dp else 60.dp),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Color.LightGray
            )
        )
        FilledTonalIconButton(
            onClick = {
                val prev = if (value <= min) max else value - 1
                onValueChange(prev)
                textState = prev.toString()
            },
            modifier = Modifier.size(36.dp)
        ) {
            Text("▼", fontSize = 13.sp)
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
