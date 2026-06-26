package com.jeonc.sajuAndUnse.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.ui.theme.*

data class ZodiacInfo(
    val emoji: String,
    val name: String,
    val years: String,
    val personality: String,
    val fortune: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZodiacScreen(navController: NavController) {
    var selectedZodiac by remember { mutableStateOf<ZodiacInfo?>(null) }

    val zodiacs = listOf(
        ZodiacInfo("🐭", "쥐띠", "1948, 1960, 1972, 1984, 1996, 2008, 2020",
            "영리하고 재치가 있으며 사교적입니다. 계획적이고 절약 정신이 강하지만, 때로는 소심한 면도 있습니다.",
            "올해는 새로운 기회가 많은 해입니다. 대인관계에서 좋은 인연을 만날 수 있으니 적극적으로 나서세요. 금전적으로는 안정적이나 큰 투자는 신중하게 하세요."),
        ZodiacInfo("🐮", "소띠", "1949, 1961, 1973, 1985, 1997, 2009, 2021",
            "성실하고 인내심이 강합니다. 꾸준한 노력으로 목표를 이루며, 신뢰할 수 있는 성격입니다.",
            "꾸준히 노력해온 일이 결실을 맺는 시기입니다. 건강 관리에 신경 쓰고, 가족과의 시간을 소중히 하세요."),
        ZodiacInfo("🐯", "호랑이띠", "1950, 1962, 1974, 1986, 1998, 2010, 2022",
            "용감하고 자신감이 넘칩니다. 리더십이 강하고 정의감이 있지만, 성급할 때가 있습니다.",
            "도전적인 일에 좋은 결과가 있을 수 있습니다. 다만 감정 조절이 필요하며, 주변 조언에 귀 기울이세요."),
        ZodiacInfo("🐰", "토끼띠", "1951, 1963, 1975, 1987, 1999, 2011, 2023",
            "온화하고 세심합니다. 예술적 감각이 뛰어나고 평화를 사랑하며, 인간관계가 원만합니다.",
            "평화롭고 안정적인 시기입니다. 예술이나 취미 활동에서 기쁨을 찾을 수 있고, 재물운도 양호합니다."),
        ZodiacInfo("🐲", "용띠", "1952, 1964, 1976, 1988, 2000, 2012, 2024",
            "카리스마가 있고 야망이 큽니다. 에너지가 넘치고 진취적이며, 큰 일을 도모하는 타입입니다.",
            "큰 기회가 찾아오는 해입니다. 자신감을 가지되 겸손함을 잃지 마세요. 건강에도 관심을 기울이세요."),
        ZodiacInfo("🐍", "뱀띠", "1953, 1965, 1977, 1989, 2001, 2013, 2025",
            "지혜롭고 직관력이 뛰어납니다. 신중하고 분석적이며, 내면이 깊고 신비로운 매력이 있습니다.",
            "지혜를 발휘할 기회가 많은 시기입니다. 학습과 자기 발전에 좋은 때이며, 금전적으로도 안정적입니다."),
        ZodiacInfo("🐴", "말띠", "1954, 1966, 1978, 1990, 2002, 2014, 2026",
            "활발하고 자유로운 영혼입니다. 행동력이 강하고 사교적이며, 여행과 모험을 좋아합니다.",
            "활동적인 한 해가 될 것입니다. 여행이나 새로운 경험에서 영감을 얻을 수 있고, 연애운도 좋습니다."),
        ZodiacInfo("🐑", "양띠", "1955, 1967, 1979, 1991, 2003, 2015",
            "온순하고 예술적입니다. 감성이 풍부하고 창의적이며, 다른 사람을 배려할 줄 압니다.",
            "창의적인 활동에서 성과를 얻는 시기입니다. 인간관계에서 따뜻한 만남이 있을 수 있습니다."),
        ZodiacInfo("🐵", "원숭이띠", "1956, 1968, 1980, 1992, 2004, 2016",
            "총명하고 재치가 넘칩니다. 다재다능하고 호기심이 많으며, 문제 해결 능력이 뛰어납니다.",
            "아이디어가 빛을 발하는 시기입니다. 새로운 프로젝트를 시작하기 좋으며, 사업운이 양호합니다."),
        ZodiacInfo("🐔", "닭띠", "1957, 1969, 1981, 1993, 2005, 2017",
            "근면하고 용감합니다. 자기 표현이 강하고 완벽주의적이며, 시간 관리에 철저합니다.",
            "계획대로 일이 진행되는 시기입니다. 세밀한 준비가 큰 성과로 이어지니 차근차근 진행하세요."),
        ZodiacInfo("🐶", "개띠", "1958, 1970, 1982, 1994, 2006, 2018",
            "충실하고 정직합니다. 의리가 강하고 정의감이 있으며, 가족과 친구를 소중히 합니다.",
            "신뢰를 바탕으로 좋은 관계가 형성되는 시기입니다. 주변의 도움으로 어려움을 극복할 수 있습니다."),
        ZodiacInfo("🐷", "돼지띠", "1959, 1971, 1983, 1995, 2007, 2019",
            "너그럽고 낙천적입니다. 물질적 풍요를 누리는 운이 있고, 성격이 좋아 인기가 많습니다.",
            "풍요로운 시기입니다. 재물운이 좋으며, 주변 사람들과 함께 나누는 기쁨이 큽니다.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("띠별 운세", fontWeight = FontWeight.Bold) },
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
        if (selectedZodiac == null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(zodiacs.size) { index ->
                    val zodiac = zodiacs[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedZodiac = zodiac },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(zodiac.emoji, fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                zodiac.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            val zodiac = selectedZodiac!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(zodiac.emoji, fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(zodiac.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(zodiac.years, fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ResultSection("성격 특성", zodiac.personality)
                ResultSection("올해의 운세", zodiac.fortune)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { selectedZodiac = null },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("다른 띠 보기")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
