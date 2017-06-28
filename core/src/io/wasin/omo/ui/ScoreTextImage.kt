package io.wasin.omo.ui

/**
 * Created by haxpor on 6/28/17.
 */
class ScoreTextImage(x: Float, y: Float): TextImage("0", x, y) {
    var score: Int = 0

    fun addScore(amount: Int) {
        score += amount

        if (score < 0) {
            score = 0
        }

        text = score.toString()
    }
}