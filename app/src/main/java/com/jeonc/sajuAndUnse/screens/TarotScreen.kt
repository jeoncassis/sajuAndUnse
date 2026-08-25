package com.jeonc.sajuAndUnse.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.ui.theme.*

data class TarotCard(
    val name: String,
    val emoji: String,
    val keyword: String,
    val meaning: String,
    val reversedMeaning: String
)

val tarotDeck = listOf(
    TarotCard("바보", "🃏", "새로운 시작", "새로운 여정이 시작됩니다. 순수한 마음으로 도전하세요. 두려움 없이 앞으로 나아갈 때입니다.", "무모한 판단, 방향 상실을 주의하세요."),
    TarotCard("마법사", "✨", "의지와 실현", "당신에게는 목표를 이룰 능력이 있습니다. 집중력을 발휘하면 원하는 것을 이룰 수 있습니다.", "재능을 낭비하고 있습니다. 집중하세요."),
    TarotCard("여사제", "🌙", "직관과 지혜", "내면의 목소리에 귀를 기울이세요. 숨겨진 진실이 드러날 것입니다.", "비밀이 밝혀지거나 판단력이 흐려집니다."),
    TarotCard("여황제", "🌺", "풍요와 창조", "풍요롭고 창조적인 시기입니다. 인간관계와 사업 모두 번창합니다.", "의존성이나 과잉보호를 조심하세요."),
    TarotCard("황제", "👑", "권위와 안정", "강한 리더십으로 상황을 주도하세요. 안정과 성공이 따릅니다.", "권위주의적 태도를 내려놓으세요."),
    TarotCard("교황", "⛪", "전통과 조언", "믿을 수 있는 조언자를 찾으세요. 전통적인 방법이 도움이 됩니다.", "경직된 사고방식에서 벗어나세요."),
    TarotCard("연인", "💕", "사랑과 선택", "중요한 선택의 기로에 있습니다. 마음이 이끄는 대로 결정하세요.", "관계에서 갈등이나 불화가 생길 수 있습니다."),
    TarotCard("전차", "🏆", "승리와 의지", "강한 의지로 장애물을 극복하세요. 승리가 가까이 있습니다.", "통제력을 잃거나 방향이 흔들릴 수 있습니다."),
    TarotCard("힘", "🦁", "용기와 인내", "내면의 힘을 발휘할 때입니다. 부드러운 용기가 강한 힘을 이깁니다.", "자신감 결여나 두려움이 앞을 막고 있습니다."),
    TarotCard("은둔자", "🕯️", "내면 탐구", "홀로 성찰하는 시간이 필요합니다. 답은 내면에 있습니다.", "고립을 끝내고 세상으로 나올 때입니다."),
    TarotCard("운명의 수레바퀴", "🎡", "행운과 변화", "긍정적인 변화가 찾아옵니다. 새로운 기회를 잡으세요.", "예상치 못한 변화에 대비하세요."),
    TarotCard("정의", "⚖️", "균형과 진실", "공정한 판단과 균형이 필요합니다. 진실이 밝혀질 것입니다.", "불공정하거나 편향된 결정을 주의하세요."),
    TarotCard("매달린 사람", "🌿", "희생과 관점", "잠시 멈추고 다른 시각으로 바라보세요. 포기가 아닌 성장의 시간입니다.", "희생만 강요하는 상황에서 벗어나세요."),
    TarotCard("죽음", "🍂", "변화와 전환", "끝은 새로운 시작입니다. 변화를 두려워하지 말고 받아들이세요.", "변화에 저항하면 더 힘들어집니다."),
    TarotCard("절제", "🌊", "조화와 균형", "인내와 절제로 균형을 찾으세요. 천천히 꾸준히 나아가면 됩니다.", "과도함이나 불균형을 조심하세요."),
    TarotCard("악마", "🔗", "집착과 욕망", "욕망이나 집착에서 벗어나야 합니다. 스스로 만든 한계를 깨뜨리세요.", "속박에서 벗어나는 길이 보이기 시작합니다."),
    TarotCard("탑", "⚡", "급격한 변화", "예상치 못한 충격이 올 수 있습니다. 하지만 이것이 더 나은 재건의 시작입니다.", "혼란은 지나가고 있습니다. 안정을 찾으세요."),
    TarotCard("별", "⭐", "희망과 치유", "희망의 빛이 보입니다. 믿음을 갖고 나아가세요. 치유와 회복의 시간입니다.", "희망을 잃지 마세요. 어둠 뒤에 빛이 옵니다."),
    TarotCard("달", "🌛", "불안과 환상", "불안과 혼란의 시기지만 곧 지나갑니다. 직관을 믿으세요.", "숨겨진 진실이 드러나 혼란이 해소됩니다."),
    TarotCard("태양", "☀️", "성공과 행복", "밝고 긍정적인 에너지가 가득합니다. 성공과 행복이 찾아옵니다.", "일시적인 지연이 있지만 결국 좋은 결과가 옵니다."),
    TarotCard("심판", "🎺", "부활과 각성", "과거를 돌아보고 새롭게 태어날 때입니다. 중요한 결정을 내리세요.", "자기 비판에서 벗어나 새 출발을 하세요."),
    TarotCard("세계", "🌍", "완성과 성취", "목표를 달성하고 완성의 기쁨을 누립니다. 새로운 사이클이 시작됩니다.", "완성이 가까웠습니다. 마지막 힘을 내세요.")
)

val spreadLabels = listOf("과거", "현재", "미래")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarotScreen(navController: NavController) {
    val shuffled = remember { tarotDeck.shuffled() }
    val selected = remember { mutableStateListOf<Int>() }
    val reversed = remember { mutableStateListOf<Boolean>() }
    var showResult by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("타로점 보기", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showResult) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "카드 3장을 선택하세요",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "과거 · 현재 · 미래 순서로 뽑힙니다 (${selected.size}/3)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(480.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(shuffled.size) { index ->
                        val isSelected = selected.contains(index)
                        val selectionOrder = selected.indexOf(index)
                        Box(
                            modifier = Modifier
                                .aspectRatio(0.65f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected)
                                        Brush.verticalGradient(listOf(Primary, PrimaryDark))
                                    else
                                        Brush.verticalGradient(listOf(Color(0xFF2D2B55), Color(0xFF1A1840)))
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(0xFFFFD700) else Color(0xFF5A5890),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = !isSelected && selected.size < 3) {
                                    selected.add(index)
                                    reversed.add((0..1).random() == 1)
                                    if (selected.size == 3) showResult = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Text(
                                    "${selectionOrder + 1}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD700)
                                )
                            } else {
                                Text("🔮", fontSize = 24.sp)
                            }
                        }
                    }
                }

                if (selected.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = {
                        selected.clear()
                        reversed.clear()
                    }) {
                        Text("다시 선택", color = Color.Gray)
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "타로 결과",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                selected.forEachIndexed { i, cardIndex ->
                    val card = shuffled[cardIndex]
                    val isReversed = reversed[i]
                    TarotResultCard(
                        label = spreadLabels[i],
                        card = card,
                        isReversed = isReversed
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        selected.clear()
                        reversed.clear()
                        showResult = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("다시 뽑기")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TarotResultCard(label: String, card: TarotCard, isReversed: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Primary)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "${card.emoji} ${card.name}${if (isReversed) " (역방향)" else ""}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isReversed) Color(0xFFC62828) else Color(0xFF1A1A2E)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Background)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    "키워드: ${card.keyword}",
                    fontSize = 13.sp,
                    color = Primary,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                if (isReversed) card.reversedMeaning else card.meaning,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Start
            )
        }
    }
}
