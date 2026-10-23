package com.jeonc.sajuAndUnse.ads

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdManager {
    const val BANNER_AD_UNIT_ID = "ca-app-pub-8346854510090115/6882458335"
    const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-8346854510090115/2891242439"
    const val NATIVE_AD_UNIT_ID = "ca-app-pub-8346854510090115/8850146127"

    // 전면 광고 최소 노출 간격 (마지막 노출 후 이 시간이 지나야 다시 노출)
    private const val INTERSTITIAL_MIN_INTERVAL_MS = 90_000L

    private var interstitialAd: InterstitialAd? = null
    private var isLoadingInterstitial = false
    private var lastInterstitialShownAt = 0L

    fun initialize(context: Context) {
        MobileAds.initialize(context)
    }

    fun loadInterstitialAd(context: Context) {
        if (interstitialAd != null || isLoadingInterstitial) return
        isLoadingInterstitial = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoadingInterstitial = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoadingInterstitial = false
                }
            }
        )
    }

    private fun canShowInterstitial(): Boolean =
        lastInterstitialShownAt == 0L ||
            SystemClock.elapsedRealtime() - lastInterstitialShownAt >= INTERSTITIAL_MIN_INTERVAL_MS

    fun showInterstitialAd(activity: Activity, onDismissed: () -> Unit = {}) {
        val ad = interstitialAd
        if (ad == null) {
            loadInterstitialAd(activity)
            onDismissed()
            return
        }
        if (!canShowInterstitial()) {
            onDismissed()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                interstitialAd = null
                loadInterstitialAd(activity)
                onDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                interstitialAd = null
                loadInterstitialAd(activity)
                onDismissed()
            }
        }
        lastInterstitialShownAt = SystemClock.elapsedRealtime()
        ad.show(activity)
    }
}
