package com.jeonc.sajuAndUnse.screens

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.data.PalmReadingService
import com.jeonc.sajuAndUnse.ui.theme.*
import kotlinx.coroutines.launch
import java.io.File

private enum class PalmStep { HAND_SELECT, IMAGE_CAPTURE, CAMERA_PREVIEW, GUIDE, ERROR }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PalmReadingScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var step by remember { mutableStateOf(PalmStep.HAND_SELECT) }
    var isLeftHand by remember { mutableStateOf(true) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    // 카메라 앱이 뜨는 동안 시스템이 메모리 확보를 위해 프로세스를 죽였다가 복원할 수 있는데,
    // remember만 쓰면 파일 경로가 새로 생성되어 카메라가 실제로 저장한 파일과 어긋나 버린다.
    // rememberSaveable로 경로를 보존해 재시작 후에도 같은 파일/Uri를 가리키게 한다.
    val photoFilePath = rememberSaveable {
        File.createTempFile("palm_", ".jpg", context.cacheDir).absolutePath
    }
    val photoUri = remember(photoFilePath) {
        FileProvider.getUriForFile(context, "${context.packageName}.provider", File(photoFilePath))
    }

    fun loadCapturedPhoto() {
        scope.launch {
            runCatching {
                PalmReadingService.loadAndResizeBitmap(context, photoUri)
            }.onSuccess { bmp ->
                capturedBitmap = bmp
                step = PalmStep.IMAGE_CAPTURE
            }.onFailure { e ->
                errorMessage = e.message ?: "사진을 불러오지 못했습니다."
                step = PalmStep.ERROR
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            scope.launch {
                runCatching {
                    PalmReadingService.loadAndResizeBitmap(context, it)
                }.onSuccess { bmp ->
                    capturedBitmap = bmp
                }.onFailure { e ->
                    errorMessage = e.message ?: "사진을 불러오지 못했습니다."
                    step = PalmStep.ERROR
                }
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) step = PalmStep.CAMERA_PREVIEW
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("운명선·손금 보기", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (step == PalmStep.HAND_SELECT) navController.popBackStack()
                        else { step = PalmStep.HAND_SELECT; capturedBitmap = null }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (step) {
                PalmStep.HAND_SELECT -> HandSelectStep(
                    onSelect = { left ->
                        isLeftHand = left
                        capturedBitmap = null
                        step = PalmStep.IMAGE_CAPTURE
                    }
                )

                PalmStep.IMAGE_CAPTURE -> ImageCaptureStep(
                    isLeftHand = isLeftHand,
                    bitmap = capturedBitmap,
                    onCamera = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    onGallery = { galleryLauncher.launch("image/*") },
                    onShowGuide = { step = PalmStep.GUIDE },
                    onChangeHand = { step = PalmStep.HAND_SELECT }
                )

                PalmStep.CAMERA_PREVIEW -> PalmCameraCaptureStep(
                    isLeftHand = isLeftHand,
                    photoFile = File(photoFilePath),
                    onCaptured = { loadCapturedPhoto() },
                    onError = { msg ->
                        errorMessage = msg
                        step = PalmStep.ERROR
                    },
                    onCancel = { step = PalmStep.IMAGE_CAPTURE }
                )

                PalmStep.GUIDE -> PalmGuideStep(
                    isLeftHand = isLeftHand,
                    bitmap = capturedBitmap,
                    onRetry = { step = PalmStep.HAND_SELECT; capturedBitmap = null }
                )

                PalmStep.ERROR -> ErrorStep(
                    message = errorMessage,
                    onRetry = { step = PalmStep.IMAGE_CAPTURE }
                )
            }
        }
    }
}

@Composable
private fun HandSelectStep(onSelect: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("✋", fontSize = 56.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "어느 손을 볼까요?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "손바닥이 잘 보이도록 촬영해주세요",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))

        HandButton(
            emoji = "🤚",
            title = "왼손",
            subtitle = "선천적 운명 · 타고난 잠재력",
            gradient = listOf(Color(0xFF1A237E), Color(0xFF283593)),
            onClick = { onSelect(true) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        HandButton(
            emoji = "✋",
            title = "오른손",
            subtitle = "후천적 운명 · 현실과 노력의 성취",
            gradient = listOf(Color(0xFF880E4F), Color(0xFFAD1457)),
            onClick = { onSelect(false) }
        )
    }
}

@Composable
private fun HandButton(
    emoji: String,
    title: String,
    subtitle: String,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.horizontalGradient(gradient))
            .clickable(onClick = onClick)
            .padding(20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 40.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun ImageCaptureStep(
    isLeftHand: Boolean,
    bitmap: Bitmap?,
    onCamera: () -> Unit,
    onGallery: () -> Unit,
    onShowGuide: () -> Unit,
    onChangeHand: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // 현재 선택 표시
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Primary)
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clickable(onClick = onChangeHand)
        ) {
            Text(
                "${if (isLeftHand) "왼손 🤚" else "오른손 ✋"}  (탭하여 변경)",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 이미지 미리보기
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF1A1A2E))
                .border(2.dp, if (bitmap != null) Primary else Color(0xFF7B1FA2), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "손 사진",
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                HandGuide(isLeftHand = isLeftHand, modifier = Modifier.fillMaxSize())
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCamera,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("📷 카메라")
            }
            OutlinedButton(
                onClick = onGallery,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("🖼️ 갤러리")
            }
        }

        if (bitmap != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onShowGuide,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A148C)
                )
            ) {
                Text("🔮 손금 보는 법으로 비교하기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("📌 촬영 팁", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF795548))
                Spacer(modifier = Modifier.height(6.dp))
                Text("• 손바닥을 펴고 손금이 잘 보이도록 촬영하세요\n• 밝은 조명 아래에서 찍으면 더 정확합니다\n• 손 전체가 화면에 들어오도록 하세요",
                    fontSize = 12.sp, color = Color(0xFF795548), lineHeight = 20.sp)
            }
        }
    }
}

private data class PalmLine(
    val title: String,
    val color: Color,
    val location: String,
    val points: List<String>
)

// 손금 위치는 사람마다 조금씩 다르므로, 사진 위 오버레이는 "대략 이 근처를 보세요" 정도의
// 참고용 안내선이다. 실제 판단은 아래 해설을 보며 사용자가 직접 자신의 손과 비교한다.
private val palmLines = listOf(
    PalmLine(
        title = "생명선",
        color = Color(0xFFE53935),
        location = "엄지와 검지 사이에서 시작해 엄지 뿌리를 둥글게 감싸며 손목 쪽으로 이어지는 선",
        points = listOf(
            "길고 깊게 이어짐 — 강한 체력과 활력, 좋은 회복력",
            "짧거나 흐림 — 무리한 활동보다 여유 있는 생활이 어울림",
            "크게 둥글게 휨 — 활동적이고 에너지가 넘치는 성향",
            "중간에 끊긴 부분 — 건강 문제가 아니라 인생의 큰 전환점을 의미"
        )
    ),
    PalmLine(
        title = "두뇌선",
        color = Color(0xFF1E88E5),
        location = "생명선이 시작되는 지점 부근에서 출발해 손바닥을 가로지르는 선",
        points = listOf(
            "곧고 수평에 가까움 — 논리적이고 현실적인 사고",
            "아래로 완만하게 휘어짐 — 창의적이고 감성적인 사고",
            "끝이 두 갈래로 갈라짐 — 다재다능하고 여러 분야에 재능",
            "길게 손바닥 끝까지 이어짐 — 신중하고 분석적인 성향"
        )
    ),
    PalmLine(
        title = "감정선",
        color = Color(0xFF43A047),
        location = "손가락 아래쪽에서 가로로 이어지는 가장 위쪽 선",
        points = listOf(
            "검지 아래까지 길게 이어짐 — 이상적이고 다정한 연애관",
            "중지 아래에서 끝남 — 현실적이고 신중한 연애관",
            "완만한 곡선 — 감정 표현이 풍부하고 다정한 성격",
            "여러 갈래로 갈라짐 — 폭넓은 인간관계와 다양한 인연"
        )
    ),
    PalmLine(
        title = "운명선",
        color = Color(0xFFFB8C00),
        location = "손목 부근에서 중지 방향으로 수직에 가깝게 뻗는 선 (희미하거나 없는 경우도 흔함)",
        points = listOf(
            "뚜렷하고 곧게 뻗음 — 목표가 분명하고 꾸준한 노력형",
            "흐리거나 거의 없음 — 정해진 틀보다 자유로운 삶을 추구",
            "중간에 새 선이 갈라져 나옴 — 인생의 전환점, 새로운 도전",
            "여러 선이 겹쳐 나타남 — 여러 일을 병행하거나 진로 변경 가능성"
        )
    )
)

@Composable
private fun PalmGuideStep(
    isLeftHand: Boolean,
    bitmap: Bitmap?,
    onRetry: () -> Unit
) {
    var showOverlay by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isLeftHand) Color(0xFF1A237E) else Color(0xFF880E4F)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    if (isLeftHand) "🤚 왼손 손금 보는 법" else "✋ 오른손 손금 보는 법",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "아래 가이드를 참고해 내 사진 속 손금과 비교해보세요",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (bitmap != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black)
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "내 손 사진",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (showOverlay) {
                    PalmLineOverlay(isLeftHand = isLeftHand, modifier = Modifier.fillMaxSize())
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("참고 가이드라인 표시", fontSize = 13.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = showOverlay, onCheckedChange = { showOverlay = it })
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "※ 가이드라인은 일반적인 위치를 표시한 참고선이며, 실제 손금 위치는 사람마다 조금씩 다릅니다.",
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        palmLines.forEach { line ->
            PalmLineGuideCard(line)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("다른 손 다시 보기")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PalmLineOverlay(isLeftHand: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        // 아래 경로들은 엄지가 왼쪽에 오는 오른손 기준으로 그려져 있다.
        // 왼손은 엄지가 반대쪽(오른쪽)에 오므로 좌우로 뒤집어야 생명선이 실제 엄지 쪽을 따라가게 된다.
        scale(scaleX = if (isLeftHand) -1f else 1f, scaleY = 1f, pivot = center) {
            val w = size.width
            val h = size.height

            fun cubic(x0: Float, y0: Float, x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Path =
                Path().apply {
                    moveTo(x0 * w, y0 * h)
                    cubicTo(x1 * w, y1 * h, x2 * w, y2 * h, x3 * w, y3 * h)
                }

            val strokeWidth = 3.dp.toPx()

            // 생명선: 엄지 뿌리를 감싸며 손목 방향으로
            drawPath(
                path = cubic(0.42f, 0.40f, 0.30f, 0.45f, 0.24f, 0.65f, 0.30f, 0.88f),
                color = palmLines[0].color.copy(alpha = 0.85f),
                style = Stroke(width = strokeWidth)
            )
            // 두뇌선: 손바닥을 가로지름
            drawPath(
                path = cubic(0.35f, 0.48f, 0.50f, 0.52f, 0.65f, 0.55f, 0.80f, 0.58f),
                color = palmLines[1].color.copy(alpha = 0.85f),
                style = Stroke(width = strokeWidth)
            )
            // 감정선: 손가락 아래 가로선
            drawPath(
                path = cubic(0.22f, 0.32f, 0.40f, 0.27f, 0.65f, 0.27f, 0.85f, 0.32f),
                color = palmLines[2].color.copy(alpha = 0.85f),
                style = Stroke(width = strokeWidth)
            )
            // 운명선: 손목에서 중지 방향으로 수직
            drawPath(
                path = cubic(0.53f, 0.90f, 0.55f, 0.65f, 0.55f, 0.40f, 0.56f, 0.18f),
                color = palmLines[3].color.copy(alpha = 0.85f),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}

@Composable
private fun PalmLineGuideCard(line: PalmLine) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(line.color, RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(line.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("위치: ${line.location}", fontSize = 13.sp, color = Color.DarkGray, lineHeight = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            line.points.forEach { point ->
                Text("• $point", fontSize = 13.sp, color = Color.DarkGray, lineHeight = 21.sp)
            }
        }
    }
}

@Composable
internal fun HandGuide(isLeftHand: Boolean, modifier: Modifier = Modifier) {
    val guideColor = Color(0xFF9C27B0)
    val fillColor = Color(0x22CE93D8)

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 아래 손가락 배치는 엄지가 왼쪽에 오는 오른손 기준이라, 왼손일 때는 좌우로 뒤집는다.
            scale(scaleX = if (isLeftHand) -1f else 1f, scaleY = 1f, pivot = center) {
            val dashes = PathEffect.dashPathEffect(floatArrayOf(14f, 8f))
            val stroke = Stroke(width = 3.dp.toPx(), pathEffect = dashes)
            val solidStroke = Stroke(width = 2.dp.toPx())

            val w = size.width
            val h = size.height

            // 손바닥 영역
            val palmL = w * 0.16f
            val palmR = w * 0.84f
            val palmT = h * 0.44f
            val palmB = h * 0.90f
            val palmW = palmR - palmL
            val palmH = palmB - palmT
            val palmCorner = 22.dp.toPx()

            // 손바닥 채우기
            drawRoundRect(
                color = fillColor,
                topLeft = Offset(palmL, palmT),
                size = Size(palmW, palmH),
                cornerRadius = CornerRadius(palmCorner)
            )
            // 손바닥 외곽선
            drawRoundRect(
                color = guideColor,
                topLeft = Offset(palmL, palmT),
                size = Size(palmW, palmH),
                cornerRadius = CornerRadius(palmCorner),
                style = stroke
            )

            // 손가락 5개 (엄지~새끼)
            // 너비 비율: 엄지 넓고 짧음, 나머지는 비슷
            val gap = 5.dp.toPx()
            val totalGap = gap * 4
            val baseFingerW = (palmW - totalGap) / 5f

            // 각 손가락: [너비배율, 높이비율, 가로오프셋]
            val fingers = listOf(
                Triple(0.90f, 0.26f, 0f),   // 엄지
                Triple(0.95f, 0.38f, 0f),   // 검지
                Triple(1.05f, 0.42f, 0f),   // 중지
                Triple(0.95f, 0.37f, 0f),   // 약지
                Triple(0.85f, 0.28f, 0f)    // 새끼
            )

            fingers.forEachIndexed { i, (wRatio, hRatio, _) ->
                val fW = baseFingerW * wRatio
                val fH = h * hRatio
                val fL = palmL + i * (baseFingerW + gap) + (baseFingerW - fW) / 2
                val fT = palmT - fH + 10.dp.toPx()
                val fR = fW / 2

                drawRoundRect(
                    color = fillColor,
                    topLeft = Offset(fL, fT),
                    size = Size(fW, fH),
                    cornerRadius = CornerRadius(fR)
                )
                drawRoundRect(
                    color = guideColor,
                    topLeft = Offset(fL, fT),
                    size = Size(fW, fH),
                    cornerRadius = CornerRadius(fR),
                    style = stroke
                )
            }

            // 손목선 (점선)
            drawLine(
                color = guideColor.copy(alpha = 0.5f),
                start = Offset(palmL + palmCorner, palmB),
                end = Offset(palmR - palmCorner, palmB),
                strokeWidth = 2.dp.toPx(),
                pathEffect = dashes
            )
            }
        }

        // 안내 텍스트
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                if (isLeftHand) "왼손 손바닥을 가이드에 맞춰주세요" else "오른손 손바닥을 가이드에 맞춰주세요",
                fontSize = 12.sp,
                color = Color(0xFFCE93D8),
                fontWeight = FontWeight.Medium
            )
        }

        // 좌/우 표시
        Text(
            if (isLeftHand) "L" else "R",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .background(Color(0x447B1FA2), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 12.sp,
            color = Color(0xFFCE93D8),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ErrorStep(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⚠️", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("불러오기 실패", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3F3))
        ) {
            Text(
                message,
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                color = Color(0xFFC62828),
                lineHeight = 22.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("다시 시도")
        }
    }
}
