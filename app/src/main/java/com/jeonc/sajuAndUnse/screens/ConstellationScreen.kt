package com.jeonc.sajuAndUnse.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.data.TodayFortuneGenerator
import com.jeonc.sajuAndUnse.ui.theme.*

data class ConstellationInfo(
    val symbol: String,
    val name: String,
    val dateRange: String,
    val element: String,
    val personality: String,
    val fortune: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstellationScreen(navController: NavController) {
    var selected by remember { mutableStateOf<ConstellationInfo?>(null) }

    val constellations = listOf(
        ConstellationInfo("♈", "양자리", "3/21~4/19", "불",
            "열정적이고 선구자 기질이 있습니다. 행동력이 강하고 도전을 두려워하지 않으며 리더십이 뛰어납니다.",
            "새로운 시작에 좋은 기운이 있습니다. 오래 미뤄온 계획을 실행할 최적의 시기입니다. 인간관계에서 긍정적인 변화가 생깁니다."),
        ConstellationInfo("♉", "황소자리", "4/20~5/20", "흙",
            "인내심이 강하고 실용적입니다. 안정을 중요시하며 신뢰할 수 있는 성격으로 주변의 믿음을 받습니다.",
            "꾸준한 노력이 결실을 맺는 시기입니다. 재물운이 안정적이며 가정에서 행복한 순간들이 이어집니다."),
        ConstellationInfo("♊", "쌍둥이자리", "5/21~6/20", "공기",
            "다재다능하고 커뮤니케이션 능력이 뛰어납니다. 지적 호기심이 많고 사교적이며 변화를 즐깁니다.",
            "소통과 학습에서 두각을 나타내는 시기입니다. 새로운 인연이 생길 수 있으며 아이디어가 빛을 발합니다."),
        ConstellationInfo("♋", "게자리", "6/21~7/22", "물",
            "감성적이고 직관력이 뛰어납니다. 가족과 가까운 사람을 소중히 여기며 배려심이 넘칩니다.",
            "가정과 감정적 유대가 강화되는 시기입니다. 내면의 목소리에 귀 기울이면 중요한 결정을 잘 내릴 수 있습니다."),
        ConstellationInfo("♌", "사자자리", "7/23~8/22", "불",
            "카리스마가 넘치고 창의적입니다. 무대의 중심에 서는 것을 좋아하며 자신감과 리더십이 강합니다.",
            "자신의 재능을 드러낼 기회가 찾아옵니다. 연애운이 좋으며 창작 활동에서 뛰어난 성과를 낼 수 있습니다."),
        ConstellationInfo("♍", "처녀자리", "8/23~9/22", "흙",
            "꼼꼼하고 분석적입니다. 완벽주의 성향이 있으며 세심한 배려로 주변 사람들에게 도움을 줍니다.",
            "계획적인 접근이 큰 성과를 가져오는 시기입니다. 건강 관리와 루틴을 정비하면 에너지가 상승합니다."),
        ConstellationInfo("♎", "천칭자리", "9/23~10/22", "공기",
            "균형감각과 공정함을 추구합니다. 미적 감각이 뛰어나고 조화로운 인간관계를 만들어갑니다.",
            "대인관계에서 좋은 에너지가 흐르는 시기입니다. 협력과 파트너십을 통해 목표를 이루어 가세요."),
        ConstellationInfo("♏", "전갈자리", "10/23~11/21", "물",
            "통찰력이 깊고 의지가 강합니다. 한번 목표를 정하면 끝까지 밀고 나가는 집중력이 있습니다.",
            "숨겨진 재능이 빛을 발하는 시기입니다. 깊이 있는 연구나 탐구 활동에서 큰 성과를 거둡니다."),
        ConstellationInfo("♐", "사수자리", "11/22~12/21", "불",
            "자유로운 영혼으로 모험을 즐깁니다. 낙천적이고 철학적 사고를 지니며 넓은 시야로 세상을 바라봅니다.",
            "확장과 여행에 좋은 운이 있습니다. 새로운 문화와 사람을 만나며 지평을 넓힐 수 있는 기회가 옵니다."),
        ConstellationInfo("♑", "염소자리", "12/22~1/19", "흙",
            "야망이 있고 책임감이 강합니다. 어떤 상황에서도 침착하게 대처하며 목표를 향해 꾸준히 나아갑니다.",
            "꾸준한 노력이 사회적 인정으로 이어지는 시기입니다. 장기적 관점에서 중요한 결정을 내려보세요."),
        ConstellationInfo("♒", "물병자리", "1/20~2/18", "공기",
            "독창적이고 인도주의적입니다. 미래지향적 사고로 혁신적인 아이디어를 제시하며 개성이 돋보입니다.",
            "독특한 아이디어가 주목받는 시기입니다. 사회적 연대와 협력을 통해 뜻밖의 기회를 만날 수 있습니다."),
        ConstellationInfo("♓", "물고기자리", "2/19~3/20", "물",
            "감수성이 풍부하고 공감 능력이 뛰어납니다. 예술적 재능과 직관력이 강하며 다른 사람을 잘 이해합니다.",
            "창의력과 영감이 넘치는 시기입니다. 예술·음악 분야에서 좋은 성과를 낼 수 있으며 귀인을 만날 수 있습니다.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("별자리 운세", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selected != null) selected = null else navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = { BannerAdView() },
        containerColor = Background
    ) { paddingValues ->
        if (selected == null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(constellations.size) { index ->
                    val c = constellations[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selected = c },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(c.symbol, fontSize = 30.sp, color = Primary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                c.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                c.dateRange,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            val c = selected!!
            val cIndex = constellations.indexOf(c)
            val daily = TodayFortuneGenerator.generateZodiacDaily(cIndex)

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
                        Text(c.symbol, fontSize = 56.sp, color = Primary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(c.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(c.dateRange, fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Primary.copy(alpha = 0.1f))
                                .padding(horizontal = 14.dp, vertical = 5.dp)
                        ) {
                            Text(
                                "${c.element}의 별자리",
                                fontSize = 13.sp,
                                color = Primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ResultSection("성격 특성", c.personality)
                ResultSection("올해의 운세", c.fortune)

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "오늘의 운세",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            daily.todayFortune,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "주의 키워드",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    daily.cautionKeywords.forEach { keyword ->
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
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "행운의 색",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(daily.luckyColorHex))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    daily.luckyColor,
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { selected = null },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("다른 별자리 보기")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
