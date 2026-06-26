package com.jeonc.sajuAndUnse.data

data class SajuInput(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val isLunar: Boolean = false,
    val gender: Gender = Gender.MALE
)

enum class Gender(val display: String) {
    MALE("남성"), FEMALE("여성")
}

enum class Cheongan(val hanja: String, val korean: String, val element: String, val yinYang: String) {
    GAP("甲", "갑", "목(木)", "양"),
    EUL("乙", "을", "목(木)", "음"),
    BYEONG("丙", "병", "화(火)", "양"),
    JEONG("丁", "정", "화(火)", "음"),
    MU("戊", "무", "토(土)", "양"),
    GI("己", "기", "토(土)", "음"),
    GYEONG("庚", "경", "금(金)", "양"),
    SIN("辛", "신", "금(金)", "음"),
    IM("壬", "임", "수(水)", "양"),
    GYE("癸", "계", "수(水)", "음");

    companion object {
        fun fromIndex(index: Int): Cheongan = entries[index % 10]
    }
}

enum class Jiji(val hanja: String, val korean: String, val animal: String, val element: String) {
    JA("子", "자", "쥐", "수(水)"),
    CHUK("丑", "축", "소", "토(土)"),
    IN("寅", "인", "호랑이", "목(木)"),
    MYO("卯", "묘", "토끼", "목(木)"),
    JIN("辰", "진", "용", "토(土)"),
    SA("巳", "사", "뱀", "화(火)"),
    O("午", "오", "말", "화(火)"),
    MI("未", "미", "양", "토(土)"),
    SHIN("申", "신", "원숭이", "금(金)"),
    YU("酉", "유", "닭", "금(金)"),
    SUL("戌", "술", "개", "토(土)"),
    HAE("亥", "해", "돼지", "수(水)");

    companion object {
        fun fromIndex(index: Int): Jiji = entries[index % 12]
    }
}

data class Pillar(val cheongan: Cheongan, val jiji: Jiji) {
    val display: String get() = "${cheongan.hanja}${jiji.hanja} (${cheongan.korean}${jiji.korean})"
}

data class SajuResult(
    val yearPillar: Pillar,
    val monthPillar: Pillar,
    val dayPillar: Pillar,
    val hourPillar: Pillar,
    val mainElement: String,
    val fortuneDescription: String,
    val personalityDescription: String,
    val careerAdvice: String,
    val loveAdvice: String,
    val healthAdvice: String
)

object SajuCalculator {

    fun calculate(input: SajuInput): SajuResult {
        val yearPillar = calculateYearPillar(input.year)
        val monthPillar = calculateMonthPillar(input.year, input.month)
        val dayPillar = calculateDayPillar(input.year, input.month, input.day)
        val hourPillar = calculateHourPillar(dayPillar.cheongan, input.hour)

        val mainElement = dayPillar.cheongan.element

        return SajuResult(
            yearPillar = yearPillar,
            monthPillar = monthPillar,
            dayPillar = dayPillar,
            hourPillar = hourPillar,
            mainElement = mainElement,
            fortuneDescription = getFortuneDescription(dayPillar.cheongan, yearPillar.jiji),
            personalityDescription = getPersonalityDescription(dayPillar.cheongan),
            careerAdvice = getCareerAdvice(dayPillar.cheongan),
            loveAdvice = getLoveAdvice(dayPillar.cheongan, input.gender),
            healthAdvice = getHealthAdvice(dayPillar.cheongan)
        )
    }

    private fun calculateYearPillar(year: Int): Pillar {
        val ganIndex = (year - 4) % 10
        val jiIndex = (year - 4) % 12
        return Pillar(Cheongan.fromIndex(ganIndex), Jiji.fromIndex(jiIndex))
    }

    private fun calculateMonthPillar(year: Int, month: Int): Pillar {
        val yearGanIndex = (year - 4) % 10
        val monthGanBase = (yearGanIndex % 5) * 2 + 2
        val monthGanIndex = (monthGanBase + month - 1) % 10
        val monthJiIndex = (month + 1) % 12
        return Pillar(Cheongan.fromIndex(monthGanIndex), Jiji.fromIndex(monthJiIndex))
    }

    private fun calculateDayPillar(year: Int, month: Int, day: Int): Pillar {
        val y = if (month <= 2) year - 1 else year
        val m = if (month <= 2) month + 12 else month
        val c = y / 100
        val k = y % 100
        val jdn = day + (13 * (m + 1)) / 5 + k + k / 4 + c / 4 - 2 * c + 2440000
        val ganIndex = (jdn - 1) % 10
        val jiIndex = (jdn + 1) % 12
        return Pillar(
            Cheongan.fromIndex(if (ganIndex < 0) ganIndex + 10 else ganIndex),
            Jiji.fromIndex(if (jiIndex < 0) jiIndex + 12 else jiIndex)
        )
    }

    private fun calculateHourPillar(dayGan: Cheongan, hour: Int): Pillar {
        val jiIndex = when (hour) {
            23, 0 -> 0
            in 1..2 -> 1
            in 3..4 -> 2
            in 5..6 -> 3
            in 7..8 -> 4
            in 9..10 -> 5
            in 11..12 -> 6
            in 13..14 -> 7
            in 15..16 -> 8
            in 17..18 -> 9
            in 19..20 -> 10
            in 21..22 -> 11
            else -> 0
        }
        val dayGanIndex = dayGan.ordinal
        val hourGanBase = (dayGanIndex % 5) * 2
        val hourGanIndex = (hourGanBase + jiIndex) % 10
        return Pillar(Cheongan.fromIndex(hourGanIndex), Jiji.fromIndex(jiIndex))
    }

    private fun getFortuneDescription(dayGan: Cheongan, yearJi: Jiji): String {
        val base = when (dayGan) {
            Cheongan.GAP, Cheongan.EUL -> "목(木)의 기운을 타고난 당신은 성장과 발전의 운명을 가지고 있습니다. 봄의 생명력처럼 끊임없이 자라나는 기운이 있어 새로운 시작에 강합니다."
            Cheongan.BYEONG, Cheongan.JEONG -> "화(火)의 기운을 타고난 당신은 열정과 빛의 운명을 가지고 있습니다. 태양처럼 밝고 따뜻한 에너지로 주변을 밝히는 힘이 있습니다."
            Cheongan.MU, Cheongan.GI -> "토(土)의 기운을 타고난 당신은 안정과 중심의 운명을 가지고 있습니다. 대지처럼 든든하고 포용력 있는 기운으로 모든 것을 품어안습니다."
            Cheongan.GYEONG, Cheongan.SIN -> "금(金)의 기운을 타고난 당신은 결단력과 의리의 운명을 가지고 있습니다. 강철같은 의지와 날카로운 판단력이 당신의 무기입니다."
            Cheongan.IM, Cheongan.GYE -> "수(水)의 기운을 타고난 당신은 지혜와 유연함의 운명을 가지고 있습니다. 물처럼 어떤 상황에도 적응하는 유연한 능력을 지녔습니다."
        }
        val animal = "띠 동물: ${yearJi.animal} - ${yearJi.animal}띠의 특성이 당신의 운세에 영향을 미칩니다."
        return "$base\n\n$animal"
    }

    private fun getPersonalityDescription(dayGan: Cheongan): String = when (dayGan) {
        Cheongan.GAP -> "갑목(甲木)일주 - 큰 나무와 같은 성격입니다. 곧은 성품에 리더십이 강하고, 자존심이 세지만 의로운 마음을 가지고 있습니다. 큰 뜻을 품고 묵묵히 나아가는 타입입니다."
        Cheongan.EUL -> "을목(乙木)일주 - 풀이나 덩굴과 같은 성격입니다. 부드럽고 유연하며 적응력이 뛰어납니다. 겉보기엔 약해 보이지만 끈기와 생명력이 강합니다."
        Cheongan.BYEONG -> "병화(丙火)일주 - 태양과 같은 성격입니다. 밝고 활발하며 모든 사람에게 따뜻합니다. 낙천적이고 표현력이 풍부하지만 가끔 과격해질 수 있습니다."
        Cheongan.JEONG -> "정화(丁火)일주 - 촛불과 같은 성격입니다. 은은하고 섬세하며 집중력이 뛰어납니다. 예민한 감수성으로 예술적 재능이 있고 내면이 깊습니다."
        Cheongan.MU -> "무토(戊土)일주 - 산과 같은 성격입니다. 무게감 있고 듬직하며 신뢰감을 줍니다. 고집이 있지만 책임감이 강하고 포용력이 넓습니다."
        Cheongan.GI -> "기토(己土)일주 - 논밭과 같은 성격입니다. 온화하고 차분하며 다른 사람을 잘 돌봅니다. 실용적이고 꼼꼼하며 내면이 따뜻합니다."
        Cheongan.GYEONG -> "경금(庚金)일주 - 바위나 쇠와 같은 성격입니다. 강하고 결단력 있으며 의리가 있습니다. 직설적이고 행동력이 강하지만 때론 날카로울 수 있습니다."
        Cheongan.SIN -> "신금(辛金)일주 - 보석과 같은 성격입니다. 섬세하고 예리하며 완벽을 추구합니다. 외모에 신경을 쓰고 미적 감각이 뛰어나며 자기 세계가 확고합니다."
        Cheongan.IM -> "임수(壬水)일주 - 바다나 큰 강과 같은 성격입니다. 넓은 도량과 지혜가 있으며 어디든 흘러갈 수 있는 적응력을 가졌습니다. 대범하고 진취적입니다."
        Cheongan.GYE -> "계수(癸水)일주 - 이슬이나 샘물과 같은 성격입니다. 조용하고 깊은 사고력을 가졌으며 직관력이 뛰어납니다. 감성적이고 영적인 면이 있습니다."
    }

    private fun getCareerAdvice(dayGan: Cheongan): String = when (dayGan) {
        Cheongan.GAP, Cheongan.EUL -> "목(木)의 기운이 강한 당신에게는 교육, 출판, 의류, 가구, 건축, 조경 등의 분야가 잘 맞습니다. 성장시키고 키워나가는 일에 소질이 있으며, 리더 역할이 어울립니다."
        Cheongan.BYEONG, Cheongan.JEONG -> "화(火)의 기운이 강한 당신에게는 방송, 예술, 요식업, 전자, IT, 마케팅 등의 분야가 잘 맞습니다. 열정적으로 표현하고 빛을 발하는 일에 재능이 있습니다."
        Cheongan.MU, Cheongan.GI -> "토(土)의 기운이 강한 당신에게는 부동산, 건설, 농업, 중개, 관리, 금융 등의 분야가 잘 맞습니다. 안정적이고 신뢰가 필요한 일에 뛰어난 능력을 보입니다."
        Cheongan.GYEONG, Cheongan.SIN -> "금(金)의 기운이 강한 당신에게는 금융, 기계, 자동차, 법률, 군/경찰, 의료 등의 분야가 잘 맞습니다. 정확하고 단호한 판단이 필요한 일에 강합니다."
        Cheongan.IM, Cheongan.GYE -> "수(水)의 기운이 강한 당신에게는 무역, 유통, 관광, 수산, 연구, 컨설팅 등의 분야가 잘 맞습니다. 흐름을 읽고 유연하게 대처하는 일에 탁월합니다."
    }

    private fun getLoveAdvice(dayGan: Cheongan, gender: Gender): String = when (dayGan) {
        Cheongan.GAP, Cheongan.EUL -> "목(木) 일주의 연애 - 한번 마음을 주면 변치 않는 진실한 사랑을 합니다. 다만 자존심이 강해 먼저 다가가는 것에 서툴 수 있습니다. 상대를 존중하면서도 자신의 감정을 솔직히 표현하세요."
        Cheongan.BYEONG, Cheongan.JEONG -> "화(火) 일주의 연애 - 열정적이고 적극적인 사랑을 합니다. 감정 표현이 풍부하고 로맨틱한 면이 있지만, 감정 기복이 클 수 있으니 안정적인 관계를 위해 노력하세요."
        Cheongan.MU, Cheongan.GI -> "토(土) 일주의 연애 - 든든하고 안정적인 사랑을 합니다. 가정적이며 신뢰할 수 있는 파트너이지만, 가끔은 변화와 설렘도 필요합니다. 일상 속 작은 감동을 만들어 보세요."
        Cheongan.GYEONG, Cheongan.SIN -> "금(金) 일주의 연애 - 의리 있고 헌신적인 사랑을 합니다. 상대에 대한 기준이 높고 완벽한 관계를 추구하지만, 때로는 있는 그대로를 받아들이는 여유가 필요합니다."
        Cheongan.IM, Cheongan.GYE -> "수(水) 일주의 연애 - 깊고 신비로운 사랑을 합니다. 감성적이고 상대의 마음을 잘 읽지만, 감정에 빠져 현실을 놓치지 않도록 균형을 유지하세요."
    }

    private fun getHealthAdvice(dayGan: Cheongan): String = when (dayGan) {
        Cheongan.GAP, Cheongan.EUL -> "목(木) 기운의 건강 - 간과 담, 눈, 근육, 신경계를 주의하세요. 스트레스를 잘 관리하고, 산림욕이나 요가 같은 자연 친화적 운동이 좋습니다. 봄철에 건강관리에 더 신경 쓰세요."
        Cheongan.BYEONG, Cheongan.JEONG -> "화(火) 기운의 건강 - 심장, 소장, 혈액순환, 눈을 주의하세요. 과로를 피하고 충분한 수면을 취하세요. 명상이나 수영처럼 마음을 차분히 하는 활동이 도움됩니다."
        Cheongan.MU, Cheongan.GI -> "토(土) 기운의 건강 - 위장, 비장, 소화기관, 피부를 주의하세요. 규칙적인 식사와 적당한 운동이 중요합니다. 과식을 피하고 소화가 잘 되는 음식을 섭취하세요."
        Cheongan.GYEONG, Cheongan.SIN -> "금(金) 기운의 건강 - 폐, 대장, 피부, 호흡기를 주의하세요. 맑은 공기를 마시며 유산소 운동을 하면 좋습니다. 가을철에 건강관리에 특히 신경 쓰세요."
        Cheongan.IM, Cheongan.GYE -> "수(水) 기운의 건강 - 신장, 방광, 귀, 뼈를 주의하세요. 충분한 수분 섭취와 하체 운동이 중요합니다. 겨울철 보양식을 챙기고 몸을 따뜻하게 유지하세요."
    }
}
