/*
 * PlayTube Project Original (2026)
 * arslandaim-hub (GitHub.com/arslandaim-hub)
 * Licenced Under GPL-3.0+
*/
package com.arslandaim.playtube.utils

import android.media.audiofx.LoudnessEnhancer

/**
 * Sound boost levels. Gain is in millibels: +6 dB (600 mB) is exactly 2x amplitude,
 * so "200%" really is double the original volume.
 */
enum class VolumeBoostLevel(val gainMb: Int, val label: String, val description: String) {
    OFF(0, "100%", "Original volume"),
    X150(350, "150%", "Louder, still natural"),
    X200(600, "200%", "Loud & clear - recommended"),
    X300(950, "300%", "Very loud for quiet videos"),
    X400(1200, "400%", "Maximum - may distort")
}

class AudioEnhancerManager(initialLevel: VolumeBoostLevel = VolumeBoostLevel.X200) {
    private var loudnessEnhancer: LoudnessEnhancer? = null
    private var currentSessionId: Int = -1
    private var currentBoostLevel: VolumeBoostLevel = initialLevel

    fun attachToAudioSession(audioSessionId: Int) {
        if (audioSessionId <= 0 || audioSessionId == currentSessionId) return
        
        release()
        currentSessionId = audioSessionId

        try {
            val enhancer = LoudnessEnhancer(audioSessionId)
            loudnessEnhancer = enhancer
            applyBoostLevel(currentBoostLevel)
            PTLog.d("AudioEnhancer", "Attached LoudnessEnhancer to session $audioSessionId")
        } catch (e: Exception) {
            PTLog.e("AudioEnhancer", "Failed to attach LoudnessEnhancer", e)
            loudnessEnhancer = null
        }
    }

    fun setBoostLevel(level: VolumeBoostLevel) {
        currentBoostLevel = level
        applyBoostLevel(level)
    }

    private fun applyBoostLevel(level: VolumeBoostLevel) {
        try {
            loudnessEnhancer?.let { enhancer ->
                if (level == VolumeBoostLevel.OFF) {
                    enhancer.enabled = false
                } else {
                    enhancer.setTargetGain(level.gainMb)
                    enhancer.enabled = true
                }
            }
        } catch (e: Exception) {
            PTLog.e("AudioEnhancer", "Failed to set boost gain", e)
        }
    }

    fun release() {
        try {
            loudnessEnhancer?.enabled = false
            loudnessEnhancer?.release()
        } catch (e: Exception) {
            PTLog.w("AudioEnhancer", "Error releasing LoudnessEnhancer: ${e.message}")
        } finally {
            loudnessEnhancer = null
            currentSessionId = -1
        }
    }
}
