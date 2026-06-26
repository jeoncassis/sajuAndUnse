package com.jeonc.sajuAndUnse.screens

import android.app.Activity
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.AdManager
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.data.Cheongan
import com.jeonc.sajuAndUnse.data.Jiji
import com.jeonc.sajuAndUnse.ui.theme.*
import kotlin.math.abs

data class CompatibilityResult(
    val score: Int,
    val overallMessage: String,
    val loveMessage: String,
    val friendMessage: String,
    val advice: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompatibilityScreen(navController: NavController) {
    var year1 by remember { mutableIntStateOf(1990) }
    var month1 by remember { mutableIntStateOf(1) }
    var day1 by remember { mutableIntStateOf(1) }
    var year2 by remember { mutableIntStateOf(1992) }
    var month2 by remember { mutableIntStateOf(1) }
    var day2 by remember { mutableIntStateOf(1) }

    var year1Expanded by remember { mutableStateOf(false) }
    var month1Expanded by remember { mutableStateOf(false) }
    var day1Expanded by remember { mutableStateOf(false) }
    var year2Expanded by remember { mutableStateOf(false) }
    var month2Expanded by remember { mutableStateOf(false) }
    var day2Expanded by remember { mutableStateOf(false) }

    var result by remember { mutableStateOf<CompatibilityResult?>(null) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("궁합 보기", fontWeight = FontWeight.Bold) },
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
            Spacer(modifier = Modifier.height(12.dp))

            if (result == null) {
                // 첫 번째 사람 입력
                PersonInputCard(
                    title = "첫 번째 사람",
                    year = year1, month = month1, day = day1,
                    yearExpanded = year1Expanded, monthExpanded = month1Expanded, dayExpanded = day1Expanded,
                    onYearExpandChange = { year1Expanded = it },
                    onMonthExpandChange = { month1Expanded = it },
                    onDayExpandChange = { day1Expanded = it },
                    onYearSelect = { year1 = it; year1Expanded = false },
                    onMonthSelect = { month1 = it; month1Expanded = false },
                    onDaySelect = { day1 = it; day1Expanded = false }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("❤", fontSize = 32.sp)

                Spacer(modifier = Modifier.height(12.dp))

                // 두 번째 사람 입력
                PersonInputCard(
                    title = "두 번째 사람",
                    year = year2, month = month2, day = day2,
                    yearExpanded = year2Expanded, monthExpanded = month2Expanded, dayExpanded = day2Expanded,
                    onYearExpandChange = { year2Expanded = it },
                    onMonthExpandChange = { month2Expanded = it },
                    onDayExpandChange = { day2Expanded = it },
                    onYearSelect = { year2 = it; year2Expanded = false },
                    onMonthSelect = { month2 = it; month2Expanded = false },
                    onDaySelect = { day2 = it; day2Expanded = false }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        result = calculateCompatibility(year1, month1, day1, year2, month2, day2)
                        (context as? Activity)?.let { AdManager.showInterstitialAd(it) }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("궁합 보기", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                val r = result!!

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("궁합 점수", fontSize = 16.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${r.score}점",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                r.score >= 80 -> Color(0xFFE53935)
                                r.score >= 60 -> Primary
                                else -> Color(0xFF1565C0)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            when {
                                r.score >= 90 -> "천생연분!"
                                r.score >= 80 -> "아주 좋은 궁합"
                                r.score >= 70 -> "좋은 궁합"
                                r.score >= 60 -> "보통 궁합"
                                r.score >= 50 -> "노력이 필요한 궁합"
                                else -> "서로 맞추려는 노력이 필요해요"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ResultSection("종합", r.overallMessage)
                ResultSection("연애 궁합", r.loveMessage)
                ResultSection("우정 궁합", r.friendMessage)
                ResultSection("조언", r.advice)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { result = null },
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
fun PersonInputCard(
    title: String,
    year: Int, month: Int, day: Int,
    yearExpanded: Boolean, monthExpanded: Boolean, dayExpanded: Boolean,
    onYearExpandChange: (Boolean) -> Unit,
    onMonthExpandChange: (Boolean) -> Unit,
    onDayExpandChange: (Boolean) -> Unit,
    onYearSelect: (Int) -> Unit,
    onMonthSelect: (Int) -> Unit,
    onDaySelect: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
            Spacer(modifier = Modifier.height(12.dp))
            DropdownSelector(
                label = "년도", value = "${year}년",
                expanded = yearExpanded, onExpandChange = onYearExpandChange,
                items = (1940..2025).toList(), selectedItem = year,
                onItemSelected = onYearSelect, itemLabel = { "${it}년" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    DropdownSelector(
                        label = "월", value = "${month}월",
                        expanded = monthExpanded, onExpandChange = onMonthExpandChange,
                        items = (1..12).toList(), selectedItem = month,
                        onItemSelected = onMonthSelect, itemLabel = { "${it}월" }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    DropdownSelector(
                        label = "일", value = "${day}일",
                        expanded = dayExpanded, onExpandChange = onDayExpandChange,
                        items = (1..31).toList(), selectedItem = day,
                        onItemSelected = onDaySelect, itemLabel = { "${it}일" }
                    )
                }
            }
        }
    }
}

private fun calculateCompatibility(
    year1: Int, month1: Int, day1: Int,
    year2: Int, month2: Int, day2: Int
): CompatibilityResult {
    val gan1 = Cheongan.fromIndex((year1 - 4) % 10)
    val ji1 = Jiji.fromIndex((year1 - 4) % 12)
    val gan2 = Cheongan.fromIndex((year2 - 4) % 10)
    val ji2 = Jiji.fromIndex((year2 - 4) % 12)

    var score = 50

    // 오행 상생 관계 점수
    val element1 = gan1.element
    val element2 = gan2.element
    if (element1 == element2) score += 15
    if (isGenerating(element1, element2)) score += 20
    if (isOvercoming(element1, element2)) score -= 10

    // 지지 삼합/육합 점수
    val jiScore = getJijiCompatibility(ji1, ji2)
    score += jiScore

    // 월일 조합
    val monthDiff = abs(month1 - month2)
    if (monthDiff <= 2 || monthDiff >= 10) score += 5
    val dayDiff = abs(day1 - day2)
    if (dayDiff <= 3) score += 5

    score = score.coerceIn(30, 99)

    val overallMessage = when {
        score >= 85 -> "두 사람은 매우 잘 어울리는 조합입니다! 서로의 장단점을 보완해주며, 함께할 때 더 큰 시너지를 발휘합니다. ${ji1.animal}띠와 ${ji2.animal}띠의 조합은 자연스러운 조화를 이룹니다."
        score >= 70 -> "두 사람의 궁합은 양호합니다. ${ji1.animal}띠와 ${ji2.animal}띠는 서로를 이해하려는 노력이 있다면 좋은 관계를 유지할 수 있습니다."
        score >= 55 -> "보통 수준의 궁합입니다. ${ji1.animal}띠와 ${ji2.animal}띠는 서로 다른 점이 있지만, 차이를 인정하고 존중한다면 좋은 관계를 만들 수 있습니다."
        else -> "${ji1.animal}띠와 ${ji2.animal}띠는 성향의 차이가 있을 수 있습니다. 하지만 서로를 이해하고 맞춰가려는 노력이 있다면 충분히 좋은 관계를 만들어 갈 수 있습니다."
    }

    val loveMessage = when {
        score >= 80 -> "연인으로서 매우 잘 어울립니다. 서로에 대한 깊은 이해와 배려가 자연스러운 관계입니다. 함께하는 시간이 즐겁고 편안할 것입니다."
        score >= 60 -> "연인으로서 좋은 관계를 유지할 수 있습니다. 가끔 의견 충돌이 있을 수 있지만, 대화로 해결할 수 있는 수준입니다."
        else -> "연인 관계에서 서로의 차이를 이해하는 노력이 필요합니다. 상대방의 입장에서 생각해보고, 충분한 소통을 통해 관계를 발전시켜 나가세요."
    }

    val friendMessage = when {
        score >= 75 -> "친구로서 최고의 조합입니다! 서로의 장점을 살려주고 함께 성장할 수 있는 관계입니다."
        score >= 55 -> "좋은 친구 관계를 유지할 수 있습니다. 공통 관심사를 찾아 함께 활동하면 더욱 친밀해질 수 있습니다."
        else -> "성격 차이가 있지만, 오히려 서로 다른 시각으로 도움을 줄 수 있는 관계입니다."
    }

    val advice = when {
        score >= 80 -> "좋은 궁합을 유지하기 위해 서로에 대한 감사와 존중을 잊지 마세요. 당연하게 여기지 않는 마음이 관계를 더욱 빛나게 합니다."
        score >= 60 -> "서로의 다른 점을 단점이 아닌 매력으로 바라보세요. 열린 마음으로 소통하면 더 깊은 관계로 발전할 수 있습니다."
        else -> "궁합 점수가 전부가 아닙니다. 서로를 향한 진심과 노력이 있다면 어떤 궁합도 극복할 수 있습니다. 상대를 있는 그대로 받아들이고 사랑하세요."
    }

    return CompatibilityResult(score, overallMessage, loveMessage, friendMessage, advice)
}

private fun isGenerating(e1: String, e2: String): Boolean {
    val pairs = setOf(
        "목(木)" to "화(火)", "화(火)" to "토(土)",
        "토(土)" to "금(金)", "금(金)" to "수(水)",
        "수(水)" to "목(木)"
    )
    return (e1 to e2) in pairs || (e2 to e1) in pairs
}

private fun isOvercoming(e1: String, e2: String): Boolean {
    val pairs = setOf(
        "목(木)" to "토(土)", "토(土)" to "수(水)",
        "수(水)" to "화(火)", "화(火)" to "금(金)",
        "금(金)" to "목(木)"
    )
    return (e1 to e2) in pairs || (e2 to e1) in pairs
}

private fun getJijiCompatibility(ji1: Jiji, ji2: Jiji): Int {
    val samhap = setOf(
        setOf(Jiji.SHIN, Jiji.JA, Jiji.JIN),
        setOf(Jiji.SA, Jiji.YU, Jiji.CHUK),
        setOf(Jiji.IN, Jiji.O, Jiji.SUL),
        setOf(Jiji.HAE, Jiji.MYO, Jiji.MI)
    )
    if (samhap.any { ji1 in it && ji2 in it }) return 15

    val yukhap = setOf(
        setOf(Jiji.JA, Jiji.CHUK),
        setOf(Jiji.IN, Jiji.HAE),
        setOf(Jiji.MYO, Jiji.SUL),
        setOf(Jiji.JIN, Jiji.YU),
        setOf(Jiji.SA, Jiji.SHIN),
        setOf(Jiji.O, Jiji.MI)
    )
    if (yukhap.any { ji1 in it && ji2 in it }) return 20

    val chung = setOf(
        setOf(Jiji.JA, Jiji.O),
        setOf(Jiji.CHUK, Jiji.MI),
        setOf(Jiji.IN, Jiji.SHIN),
        setOf(Jiji.MYO, Jiji.YU),
        setOf(Jiji.JIN, Jiji.SUL),
        setOf(Jiji.SA, Jiji.HAE)
    )
    if (chung.any { ji1 in it && ji2 in it }) return -15

    return 0
}
