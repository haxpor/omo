package io.wasin.omo.ui

import com.badlogic.gdx.math.MathUtils

/**
 * Created by haxpor on 6/28/17.
 */
class ScoreTextImage(x: Float, y: Float): TextImage("0", x, y) {
    var score: Float = 0f
        private set
    var destScore: Float = 0f
        private set

    var speed: Float = 0.15f    // speed in lerping score to destination score

    fun update(dt: Float) {
        // if score is still not equal to destScore
        if (Math.abs(score - destScore) >= 0.0001f) {
            score += speed * (destScore - score)
        }

        if (score < 0f) {
            score = 0f
        }

        // round first then convert to string for representation
        text = score.toInt().toString()
    }

    fun addScore(amount: Float) {
        destScore = score + amount

        if (destScore < 0f) {
            destScore = 0f
        }
    }
}