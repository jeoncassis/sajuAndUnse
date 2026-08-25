package com.jeonc.sajuAndUnse.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PalmReadingService {

    suspend fun loadAndResizeBitmap(context: Context, uri: Uri): Bitmap = withContext(Dispatchers.IO) {
        val maxSize = 1024

        // 카메라 원본 사진은 수천만 화소라 그대로 디코딩하면 OOM으로 앱이 죽을 수 있어
        // 먼저 크기만 읽어 inSampleSize를 계산한 뒤 축소된 상태로 디코딩한다.
        // 주의: inJustDecodeBounds=true일 때 decodeStream은 항상 null을 반환하는 게 정상이므로
        // "스트림을 열 수 있는지"와 "디코딩 결과가 null인지"를 반드시 구분해서 검사해야 한다.
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        (context.contentResolver.openInputStream(uri) ?: throw Exception("이미지를 불러올 수 없습니다.")).use { stream ->
            BitmapFactory.decodeStream(stream, null, bounds)
        }

        var sampleSize = 1
        while (bounds.outWidth / (sampleSize * 2) >= maxSize && bounds.outHeight / (sampleSize * 2) >= maxSize) {
            sampleSize *= 2
        }

        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val decoded = (context.contentResolver.openInputStream(uri) ?: throw Exception("이미지를 불러올 수 없습니다.")).use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOptions)
        } ?: throw Exception("이미지 디코딩에 실패했습니다.")

        // 카메라는 센서가 찍은 원본 픽셀을 그대로 저장하고 EXIF Orientation 태그로 회전 방향만 기록한다.
        // 이 태그를 반영하지 않으면 사진이 옆으로 돌아간 채로 표시된다.
        val rotationDegrees = readExifRotationDegrees(context, uri)
        val sampled = if (rotationDegrees != 0) {
            val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
            val rotated = Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
            if (rotated !== decoded) decoded.recycle()
            rotated
        } else decoded

        val w = sampled.width
        val h = sampled.height
        if (w <= maxSize && h <= maxSize) return@withContext sampled

        val ratio = minOf(maxSize.toFloat() / w, maxSize.toFloat() / h)
        val resized = Bitmap.createScaledBitmap(sampled, (w * ratio).toInt(), (h * ratio).toInt(), true)
        if (resized !== sampled) sampled.recycle()
        resized
    }

    private fun readExifRotationDegrees(context: Context, uri: Uri): Int = runCatching {
        context.contentResolver.openInputStream(uri)?.use { stream ->
            when (ExifInterface(stream).getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } ?: 0
    }.getOrDefault(0)
}
