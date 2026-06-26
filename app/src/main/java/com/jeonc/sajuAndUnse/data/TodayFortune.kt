package com.jeonc.sajuAndUnse.data

import java.util.Calendar

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
    val direction: String
)

object TodayFortuneGenerator {

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

    fun generate(year: Int, month: Int, day: Int): DailyFortune {
        val seed = year * 10000L + month * 100 + day
        val cal = Calendar.getInstance()
        val todaySeed = seed + cal.get(Calendar.DAY_OF_YEAR)

        return DailyFortune(
            overall = overallFortunes[((todaySeed * 7) % overallFortunes.size).toInt().let { if (it < 0) it + overallFortunes.size else it }],
            love = loveFortunes[((todaySeed * 11) % loveFortunes.size).toInt().let { if (it < 0) it + loveFortunes.size else it }],
            money = moneyFortunes[((todaySeed * 13) % moneyFortunes.size).toInt().let { if (it < 0) it + moneyFortunes.size else it }],
            health = healthFortunes[((todaySeed * 17) % healthFortunes.size).toInt().let { if (it < 0) it + healthFortunes.size else it }],
            lucky = LuckyInfo(
                color = luckyColors[((todaySeed * 3) % luckyColors.size).toInt().let { if (it < 0) it + luckyColors.size else it }],
                number = ((todaySeed * 23) % 99 + 1).toInt().let { if (it < 0) it + 99 else it },
                direction = luckyDirections[((todaySeed * 5) % luckyDirections.size).toInt().let { if (it < 0) it + luckyDirections.size else it }]
            )
        )
    }
}
