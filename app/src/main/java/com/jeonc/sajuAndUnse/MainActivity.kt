package com.jeonc.sajuAndUnse

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import com.jeonc.sajuAndUnse.ads.AdManager
import com.jeonc.sajuAndUnse.navigation.NavGraph
import com.jeonc.sajuAndUnse.ui.theme.SajuAndUnseTheme

class MainActivity : ComponentActivity() {
    private var showUpdateDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AdManager.initialize(this)
        AdManager.loadInterstitialAd(this)
        checkForUpdate()

        setContent {
            SajuAndUnseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)

                    if (showUpdateDialog) {
                        AlertDialog(
                            onDismissRequest = { showUpdateDialog = false },
                            title = { Text("업데이트 안내") },
                            text = { Text("새로운 버전이 출시되었습니다.\n더 나은 서비스 이용을 위해 최신 버전으로 업데이트해 주세요.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    showUpdateDialog = false
                                    openPlayStore()
                                }) { Text("업데이트") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showUpdateDialog = false }) { Text("나중에") }
                            }
                        )
                    }
                }
            }
        }
    }

    // 스토어에 올라간 버전이 현재 설치된 버전보다 높으면 업데이트 안내 표시
    // (Play 스토어로 설치한 경우에만 동작, 디버그/사이드로드 설치는 조회 실패로 무시됨)
    private fun checkForUpdate() {
        AppUpdateManagerFactory.create(this).appUpdateInfo
            .addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    showUpdateDialog = true
                }
            }
    }

    private fun openPlayStore() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            )
        }
    }
}
