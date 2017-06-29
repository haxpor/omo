package io.wasin.omo.ui

import com.badlogic.gdx.math.MathUtils

/**
 * Created by haxpor on 6/28/17.
 */
class ScoreTextImage(x: Float, y: Float): TextImage("0", x, y) {
    var score: Int = 0
    var destScore: Int = 0
    var speed: Float = 0.15f    // speed in lerping score to destination score

    fun update(dt: Float) {
        // if not yet reach destination score then update it
        if (score != destScore) {
            score += MathUtils.floor(speed * (destScore - score))
        }

        text = score.toString()
    }

    fun addScore(amount: Int) {
        destScore = score + amount

        if (destScore < 0) {
            destScore = 0
        }
    }
}