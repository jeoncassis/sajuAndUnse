package com.jeonc.sajuAndUnse.data

import java.util.Calendar

data class ZodiacDailyFortune(
    val todayFortune: String,
    val cautionKeywords: List<String>,
    val luckyColor: String,
    val luckyColorHex: Long
)

data class DailyFortune(
    val overall: String,
    val love: String,
    val money: String,
    val health: String,
    val lucky: LuckyInfo
)

data class LuckyInfo(
    val color: String,
    val number: Int,
    val direction: String,
    val dates: List<Int>,
    val times: List<String>
)

object TodayFortuneGenerator {

    val zodiacAnimals = listOf(
        Pair("🐭", "쥐띠"), Pair("🐮", "소띠"), Pair("🐯", "호랑이띠"),
        Pair("🐰", "토끼띠"), Pair("🐲", "용띠"), Pair("🐍", "뱀띠"),
        Pair("🐴", "말띠"), Pair("🐑", "양띠"), Pair("🐵", "원숭이띠"),
        Pair("🐔", "닭띠"), Pair("🐶", "개띠"), Pair("🐷", "돼지띠")
    )

    fun getZodiacIndex(year: Int): Int = ((year - 4) % 12 + 12) % 12

    private val overallFortunes = listOf(
        "오늘은 새로운 기회가 찾아오는 날입니다. 적극적으로 행동하면 좋은 결과를 얻을 수 있습니다.",
        "차분하게 하루를 보내는 것이 좋습니다. 급하게 결정하지 말고 심사숙고하세요.",
        "주변 사람들과의 관계에서 기쁨을 찾는 날입니다. 소통에 힘쓰세요.",
        "노력한 만큼 보상이 돌아오는 날입니다. 꾸준히 해온 일의 결실을 볼 수 있습니다.",
        "예상치 못한 변화가 있을 수 있습니다. 유연하게 대처하면 오히려 전화위복이 됩니다.",
        "자신감을 가지고 도전하세요. 오늘의 용기가 미래의 큰 자산이 됩니다.",
        "건강에 신경 써야 하는 날입니다. 무리하지 말고 충분한 휴식을 취하세요.",
        "금전운이 좋은 날입니다. 하지만 과한 지출은 삼가세요.",
        "학습과 자기계발에 좋은 날입니다. 새로운 것을 배우면 큰 도움이 됩니다.",
        "가까운 사람에게 감사의 마음을 전하면 좋은 기운이 돌아옵니다.",
        "오늘은 직감을 믿어도 좋은 날입니다. 마음이 이끄는 대로 따라가 보세요.",
        "정리와 계획을 세우기 좋은 날입니다. 미루던 일을 처리하면 마음이 가벼워집니다."
    )

    private val loveFortunes = listOf(
        "연인과 달콤한 시간을 보낼 수 있는 날입니다. 작은 서프라이즈가 큰 감동을 줍니다.",
        "솔로라면 새로운 만남의 기회가 있을 수 있습니다. 열린 마음으로 다가가세요.",
        "사랑하는 사람과 진솔한 대화를 나누기 좋은 날입니다.",
        "감정을 너무 앞세우지 마세요. 이성적인 판단이 관계를 더 좋게 만듭니다.",
        "오래된 연인이라면 처음의 설렘을 되찾는 시간을 가져보세요.",
        "혼자만의 시간도 소중합니다. 자기 자신을 사랑하는 하루를 보내세요.",
        "주변의 조언에 귀 기울이면 연애에 도움이 됩니다.",
        "상대방의 작은 배려에 감사하는 마음을 가지면 관계가 더욱 깊어집니다."
    )

    private val moneyFortunes = listOf(
        "재물운이 좋은 날입니다. 기대하지 않았던 곳에서 수입이 생길 수 있습니다.",
        "지출을 줄이고 저축에 힘쓰는 것이 좋은 날입니다.",
        "투자보다는 안정적인 재테크를 추천합니다.",
        "친구나 지인과의 금전 거래는 피하는 것이 좋습니다.",
        "소소한 행운이 찾아올 수 있습니다. 로또 한 장 사보는 건 어떨까요?",
        "계획적인 소비가 필요한 날입니다. 충동구매를 조심하세요.",
        "사업을 하고 있다면 새로운 거래처와의 인연이 생길 수 있습니다.",
        "절약하는 습관이 큰 재산을 만듭니다. 오늘부터 시작해보세요."
    )

    private val healthFortunes = listOf(
        "가벼운 운동으로 하루를 시작하면 활력이 넘칩니다.",
        "수분 섭취를 충분히 하고, 규칙적인 식사를 하세요.",
        "스트레스 관리가 필요한 날입니다. 명상이나 산책을 추천합니다.",
        "과로를 피하고 충분한 수면을 취하세요.",
        "소화기관에 신경 써야 합니다. 자극적인 음식은 피하세요.",
        "야외 활동을 하면 기분 전환과 건강 모두 챙길 수 있습니다.",
        "허리와 어깨 스트레칭을 자주 해주세요.",
        "비타민이 풍부한 과일과 채소를 챙겨 드세요."
    )

    private val luckyColors = listOf("빨강", "파랑", "노랑", "초록", "보라", "흰색", "검정", "주황", "분홍", "금색")
    private val luckyDirections = listOf("동쪽", "서쪽", "남쪽", "북쪽", "동남쪽", "동북쪽", "서남쪽", "서북쪽")
    private val luckyTimes = listOf(
        "자시(子時) 23:00~01:00", "축시(丑時) 01:00~03:00", "인시(寅時) 03:00~05:00",
        "묘시(卯時) 05:00~07:00", "진시(辰時) 07:00~09:00", "사시(巳時) 09:00~11:00",
        "오시(午時) 11:00~13:00", "미시(未時) 13:00~15:00", "신시(申時) 15:00~17:00",
        "유시(酉時) 17:00~19:00", "술시(戌時) 19:00~21:00", "해시(亥時) 21:00~23:00"
    )

    fun generateZodiacDaily(zodiacIndex: Int): ZodiacDailyFortune {
        val cal = Calendar.getInstance()
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val seed = (zodiacIndex + 1).toLong() * 1000 + dayOfYear

        val fortunes = listOf(
            "오늘은 생각보다 일이 잘 풀리는 날입니다. 작은 것에도 감사하는 마음을 가지세요.",
            "주변 사람의 말에 귀 기울이면 뜻밖의 도움을 받을 수 있습니다.",
            "새로운 아이디어가 떠오르는 날입니다. 메모해두면 나중에 큰 도움이 됩니다.",
            "오전보다 오후에 집중력이 높아집니다. 중요한 일은 오후에 처리하세요.",
            "가까운 사람과의 대화에서 힌트를 얻을 수 있습니다. 경청하세요.",
            "서두르지 않으면 좋은 결과가 따라옵니다. 차분하게 진행하세요.",
            "뜻밖의 소식이 들려올 수 있습니다. 열린 마음으로 받아들이세요.",
            "작은 노력이 쌓여 큰 결과를 만드는 날입니다. 꾸준함이 중요합니다.",
            "기다리던 일에 진전이 있을 수 있습니다. 조금만 더 인내하세요.",
            "평소와 다른 길을 선택하면 새로운 기회를 만날 수 있습니다.",
            "오늘은 결정보다 준비에 집중하는 것이 좋습니다.",
            "주변을 정리하면 머릿속도 맑아지는 날입니다. 환경부터 바꿔보세요."
        )

        val allCautions = listOf(
            "충동적 결정", "과소비", "감정 기복", "과로", "오해",
            "성급함", "방심", "갈등", "의심", "고집", "지각", "거짓말"
        )

        val luckyColorsWithHex = listOf(
            Pair("빨강", 0xFFE53935L),
            Pair("파랑", 0xFF1E88E5L),
            Pair("초록", 0xFF43A047L),
            Pair("노랑", 0xFFFDD835L),
            Pair("보라", 0xFF8E24AAL),
            Pair("흰색", 0xFFF5F5F5L),
            Pair("금색", 0xFFFFD700L),
            Pair("은색", 0xFFB0BEC5L),
            Pair("분홍", 0xFFEC407AL),
            Pair("주황", 0xFFFB8C00L),
            Pair("하늘색", 0xFF29B6F6L),
            Pair("연두색", 0xFF9CCC65L)
        )

        fun pick(size: Int, multiplier: Long) = ((seed * multiplier) % size).toInt().let { if (it < 0) it + size else it }

        val caution1 = pick(allCautions.size, 7L)
        val caution2 = pick(allCautions.size, 11L).let { if (it == caution1) (it + 1) % allCautions.size else it }
        val colorPair = luckyColorsWithHex[pick(luckyColorsWithHex.size, 3L)]

        return ZodiacDailyFortune(
            todayFortune = fortunes[pick(fortunes.size, 13L)],
            cautionKeywords = listOf(allCautions[caution1], allCautions[caution2]),
            luckyColor = colorPair.first,
            luckyColorHex = colorPair.second
        )
    }

    fun generate(year: Int, month: Int, day: Int): DailyFortune {
        val seed = year * 10000L + month * 100 + day
        val cal = Calendar.getInstance()
        val todaySeed = seed + cal.get(Calendar.DAY_OF_YEAR)

        fun pick(size: Int, mul: Long) = ((todaySeed * mul) % size).toInt().let { if (it < 0) it + size else it }

        // 이번 달 길한 날짜 3개 (오늘 제외, 중복 없이)
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val today = cal.get(Calendar.DAY_OF_MONTH)
        val candidates = (1..maxDay).filter { it != today }
        val d1 = candidates[pick(candidates.size, 19L)]
        val d2 = candidates[pick(candidates.size, 29L)].let { if (it == d1) candidates[(candidates.indexOf(d1) + 1) % candidates.size] else it }
        val d3 = candidates[pick(candidates.size, 37L)].let { if (it == d1 || it == d2) candidates[(candidates.indexOf(d2) + 1) % candidates.size] else it }

        // 길한 시간 2개 (중복 없이)
        val t1 = pick(luckyTimes.size, 41L)
        val t2 = pick(luckyTimes.size, 43L).let { if (it == t1) (it + 1) % luckyTimes.size else it }

        return DailyFortune(
            overall = overallFortunes[pick(overallFortunes.size, 7L)],
            love = loveFortunes[pick(loveFortunes.size, 11L)],
            money = moneyFortunes[pick(moneyFortunes.size, 13L)],
            health = healthFortunes[pick(healthFortunes.size, 17L)],
            lucky = LuckyInfo(
                color = luckyColors[pick(luckyColors.size, 3L)],
                number = (pick(99, 23L) + 1),
                direction = luckyDirections[pick(luckyDirections.size, 5L)],
                dates = listOf(d1, d2, d3).sorted(),
                times = listOf(luckyTimes[t1], luckyTimes[t2])
            )
        )
    }
}
