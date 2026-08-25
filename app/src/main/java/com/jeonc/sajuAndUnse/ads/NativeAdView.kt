package com.jeonc.sajuAndUnse.ads

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

@Composable
fun NativeAdComposable(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        val adLoader = AdLoader.Builder(context, AdManager.NATIVE_AD_UNIT_ID)
            .forNativeAd { ad -> nativeAd = ad }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    nativeAd = null
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
        onDispose { nativeAd?.destroy() }
    }

    nativeAd?.let { ad ->
        key(ad) {
            AndroidView(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                factory = { ctx ->
                    NativeAdView(ctx).also { adView ->
                        adView.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )

                        val container = LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(40, 28, 40, 28)
                            setBackgroundColor(android.graphics.Color.WHITE)
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                        }

                        val adLabel = TextView(ctx).apply {
                            text = "광고"
                            textSize = 10f
                            setTextColor(android.graphics.Color.parseColor("#888888"))
                            setTypeface(null, Typeface.BOLD)
                            val lp = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            lp.bottomMargin = 6
                            layoutParams = lp
                        }

                        val headlineView = TextView(ctx).apply {
                            text = ad.headline ?: ""
                            textSize = 16f
                            setTextColor(android.graphics.Color.parseColor("#1A1A1A"))
                            setTypeface(null, Typeface.BOLD)
                            val lp = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            lp.bottomMargin = 8
                            layoutParams = lp
                        }

                        val bodyView = TextView(ctx).apply {
                            text = ad.body ?: ""
                            textSize = 13f
                            setTextColor(android.graphics.Color.DKGRAY)
                            val lp = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            lp.bottomMargin = 16
                            layoutParams = lp
                        }

                        val ctaButton = Button(ctx).apply {
                            text = ad.callToAction ?: "자세히 보기"
                            textSize = 13f
                            setTextColor(android.graphics.Color.WHITE)
                            setBackgroundColor(android.graphics.Color.parseColor("#8B1A1A"))
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }

                        container.addView(adLabel)
                        container.addView(headlineView)
                        container.addView(bodyView)
                        container.addView(ctaButton)
                        adView.addView(container)

                        adView.headlineView = headlineView
                        adView.bodyView = bodyView
                        adView.callToActionView = ctaButton

                        adView.setNativeAd(ad)
                    }
                }
            )
        }
    }
}
