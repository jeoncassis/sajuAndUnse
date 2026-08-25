package com.jeonc.sajuAndUnse.data

import java.util.Calendar

data class TojeongResult(
    val gwaeName: String,
    val gwaeHanja: String,
    val gwaeKeyword: String,
    val summary: String,
    val yearFortune: String,
    val spring: String,
    val summer: String,
    val autumn: String,
    val winter: String
)

object TojeongCalculator {

    private data class GwaeInfo(val name: String, val hanja: String, val keyword: String, val yearFortune: String)

    private val upperGwae = listOf(
        GwaeInfo("건", "乾", "강건·발전",
            "하늘의 기운이 가득한 해입니다. 강한 의지와 자신감으로 나아가면 크게 성취합니다. 리더십을 발휘해야 할 순간이 많으며, 주변의 신뢰가 높아집니다. 다만 지나친 고집은 화를 부르니 유연함도 갖추세요."),
        GwaeInfo("태", "兌", "기쁨·소통",
            "기쁨과 소통이 넘치는 해입니다. 대인관계에서 귀인을 만나고 협력으로 성과를 거둡니다. 말과 표현이 중요한 시기이며, 진솔한 소통이 모든 문제를 풀어줍니다. 새로운 만남이 인생을 바꿀 수 있습니다."),
        GwaeInfo("이", "離", "명예·성취",
            "밝은 빛처럼 재능이 드러나는 해입니다. 노력해온 일이 인정받고 명예가 높아집니다. 창의적인 분야에서 두각을 나타내며 주변에서 의지하는 존재가 됩니다. 감정의 기복을 조심하면 더욱 길합니다."),
        GwaeInfo("진", "震", "변화·도전",
            "천둥처럼 새로운 변화가 찾아오는 해입니다. 두려움 없이 도전하면 예상보다 큰 결과를 얻습니다. 변화를 거스르지 말고 흐름을 타세요. 새로운 환경이나 직업에서 기회가 열립니다."),
        GwaeInfo("손", "巽", "유연·침투",
            "바람처럼 부드럽게 스며드는 해입니다. 서두르지 않고 꾸준히 나아가면 반드시 결실을 맺습니다. 작은 기회를 놓치지 마세요. 주변과의 협력이 뜻밖의 큰 성과로 이어집니다."),
        GwaeInfo("감", "坎", "인내·극복",
            "물처럼 어려움을 헤쳐나가는 해입니다. 시련이 있으나 이를 견디면 반드시 좋은 날이 옵니다. 무리한 계획보다 현실적인 목표가 중요합니다. 신중하게 행동하면 어떤 어려움도 극복할 수 있습니다."),
        GwaeInfo("간", "艮", "안정·축적",
            "산처럼 묵직하게 내실을 다지는 해입니다. 겉으로 드러나기보다 내부를 충실히 하는 것이 좋습니다. 급하게 욕심 내지 말고 한 걸음씩 나아가면 큰 재물과 명성이 쌓입니다."),
        GwaeInfo("곤", "坤", "포용·협력",
            "대지처럼 넉넉하고 포용력 있는 해입니다. 혼자보다 함께할 때 훨씬 큰 힘을 발휘합니다. 주변 사람들의 도움이 크게 들어오며, 베풀수록 더 많은 것이 돌아옵니다. 가정과 건강이 특히 길합니다.")
    )

    private val springPool = listOf(
        "봄기운과 함께 새로운 시작이 활짝 열립니다. 계획한 일을 실행에 옮기기 좋은 시기입니다.",
        "귀인과의 만남이 예상됩니다. 사람을 사귀는 데 적극적으로 나서세요.",
        "재물의 씨앗이 뿌려지는 시기입니다. 작은 투자나 준비가 큰 결과로 이어집니다.",
        "학업과 자기계발에 집중하면 놀라운 성장이 찾아옵니다.",
        "건강 관리를 시작하기 좋은 계절입니다. 규칙적인 생활이 기반이 됩니다.",
        "오랫동안 기다리던 일에 드디어 진전이 생깁니다. 기대하셔도 좋습니다."
    )

    private val summerPool = listOf(
        "활동력이 왕성한 시기입니다. 적극적으로 행동할수록 원하는 바를 이룹니다.",
        "사업이나 직장에서 좋은 기회가 찾아옵니다. 주저하지 말고 잡으세요.",
        "인간관계에서 갈등이 생길 수 있습니다. 감정보다 이성으로 대처하세요.",
        "뜻밖의 재물이 들어올 수 있습니다. 다만 큰 지출은 삼가는 것이 좋습니다.",
        "체력 소모가 많은 시기이니 충분한 수분 보충과 휴식이 필요합니다.",
        "창의적인 아이디어가 빛을 발합니다. 새로운 방식을 과감히 시도해보세요."
    )

    private val autumnPool = listOf(
        "노력의 결실을 거두는 시기입니다. 그동안 쌓아온 것이 빛을 발합니다.",
        "금전운이 좋아지는 계절입니다. 재물이 모이는 흐름을 잘 활용하세요.",
        "가까운 사람에게 감사를 전하면 더 깊은 인연이 됩니다.",
        "새로운 제안이나 기회가 오지만 꼼꼼히 살피고 결정하세요.",
        "정신적으로 안정을 찾는 시기입니다. 여유를 갖고 삶을 돌아보세요.",
        "건강 검진이나 자기 점검이 필요한 시기입니다. 작은 신호를 놓치지 마세요."
    )

    private val winterPool = listOf(
        "한 해를 마무리하고 내년을 준비하는 중요한 시기입니다. 계획을 세우세요.",
        "재물을 지키는 것이 버는 것만큼 중요합니다. 불필요한 지출을 줄이세요.",
        "가족과의 따뜻한 시간이 큰 위안과 힘이 됩니다.",
        "몸과 마음 모두 충전이 필요합니다. 충분한 휴식을 취하세요.",
        "묵묵히 쌓아온 노력이 곧 결실로 이어질 준비를 마칩니다.",
        "새로운 인연이나 기회의 씨앗이 조용히 심어지는 시기입니다."
    )

    fun calculate(year: Int, month: Int, day: Int): TojeongResult {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val age = currentYear - year + 1

        val upperIdx = ((age - 1) % 8 + 8) % 8
        val seed = year.toLong() * 10000 + month * 100 + day

        fun pick(pool: List<String>, multiplier: Long): String {
            val idx = ((seed * multiplier) % pool.size).toInt().let { if (it < 0) it + pool.size else it }
            return pool[idx]
        }

        val gwae = upperGwae[upperIdx]

        val middleNum = ((month - 1) % 6) + 1
        val lowerNum = ((day - 1) % 3) + 1
        val gwaeName = "${gwae.name}${middleNum}${lowerNum}"

        return TojeongResult(
            gwaeName = "${gwae.name}괘 $middleNum-$lowerNum",
            gwaeHanja = gwae.hanja,
            gwaeKeyword = gwae.keyword,
            summary = gwae.yearFortune.lines().first(),
            yearFortune = gwae.yearFortune,
            spring = pick(springPool, 7L),
            summer = pick(summerPool, 11L),
            autumn = pick(autumnPool, 13L),
            winter = pick(winterPool, 17L)
        )
    }
}
