package com.jeonc.sajuAndUnse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jeonc.sajuAndUnse.ads.AdManager
import com.jeonc.sajuAndUnse.navigation.NavGraph
import com.jeonc.sajuAndUnse.ui.theme.SajuAndUnseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AdManager.initialize(this)
        AdManager.loadInterstitialAd(this)

        setContent {
            SajuAndUnseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
