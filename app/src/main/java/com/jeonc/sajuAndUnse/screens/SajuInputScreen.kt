package com.jeonc.sajuAndUnse.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeonc.sajuAndUnse.ads.BannerAdView
import com.jeonc.sajuAndUnse.navigation.Screen
import com.jeonc.sajuAndUnse.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SajuInputScreen(navController: NavController) {
    var selectedYear by remember { mutableIntStateOf(1990) }
    var selectedMonth by remember { mutableIntStateOf(1) }
    var selectedDay by remember { mutableIntStateOf(1) }
    var selectedHour by remember { mutableIntStateOf(12) }
    var selectedGender by remember { mutableIntStateOf(0) }

    var yearExpanded by remember { mutableStateOf(false) }
    var monthExpanded by remember { mutableStateOf(false) }
    var dayExpanded by remember { mutableStateOf(false) }
    var hourExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("사주풀이", fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "생년월일시를 입력하세요",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 성별 선택
            Text("성별", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("남성" to 0, "여성" to 1).forEach { (label, value) ->
                    FilterChip(
                        selected = selectedGender == value,
                        onClick = { selectedGender = value },
                        label = { Text(label, fontSize = 16.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 년도
            DropdownSelector(
                label = "출생 년도",
                value = "${selectedYear}년",
                expanded = yearExpanded,
                onExpandChange = { yearExpanded = it },
                items = (1940..2025).toList(),
                selectedItem = selectedYear,
                onItemSelected = { selectedYear = it; yearExpanded = false },
                itemLabel = { "${it}년" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 월
            DropdownSelector(
                label = "출생 월",
                value = "${selectedMonth}월",
                expanded = monthExpanded,
                onExpandChange = { monthExpanded = it },
                items = (1..12).toList(),
                selectedItem = selectedMonth,
                onItemSelected = { selectedMonth = it; monthExpanded = false },
                itemLabel = { "${it}월" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 일
            DropdownSelector(
                label = "출생 일",
                value = "${selectedDay}일",
                expanded = dayExpanded,
                onExpandChange = { dayExpanded = it },
                items = (1..31).toList(),
                selectedItem = selectedDay,
                onItemSelected = { selectedDay = it; dayExpanded = false },
                itemLabel = { "${it}일" }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 시간
            val hourLabels = listOf(
                "자시 (23:00-01:00)" to 0,
                "축시 (01:00-03:00)" to 1,
                "인시 (03:00-05:00)" to 3,
                "묘시 (05:00-07:00)" to 5,
                "진시 (07:00-09:00)" to 7,
                "사시 (09:00-11:00)" to 9,
                "오시 (11:00-13:00)" to 11,
                "미시 (13:00-15:00)" to 13,
                "신시 (15:00-17:00)" to 15,
                "유시 (17:00-19:00)" to 17,
                "술시 (19:00-21:00)" to 19,
                "해시 (21:00-23:00)" to 21
            )
            val currentHourLabel = hourLabels.find { it.second == selectedHour }?.first ?: "모름"

            DropdownSelector(
                label = "출생 시간 (시주)",
                value = currentHourLabel,
                expanded = hourExpanded,
                onExpandChange = { hourExpanded = it },
                items = hourLabels.map { it.second },
                selectedItem = selectedHour,
                onItemSelected = { selectedHour = it; hourExpanded = false },
                itemLabel = { h -> hourLabels.find { it.second == h }?.first ?: "" }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate(
                        Screen.SajuResult.createRoute(
                            selectedYear, selectedMonth, selectedDay,
                            selectedHour, selectedGender
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text("사주 보기", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelector(
    label: String,
    value: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandChange
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandChange(false) }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(itemLabel(item)) },
                        onClick = { onItemSelected(item) }
                    )
                }
            }
        }
    }
}
