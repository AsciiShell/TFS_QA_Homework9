package ru.tinkoff.uiautomator.application

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import ru.tinkoff.uiautomator.util.*
import java.util.concurrent.TimeUnit

class YouTube : AbstractApplication("com.google.android.youtube") {
    private val youTubeAdWait = TimeUnit.SECONDS.toMillis(60)
    private val youTubePlayPauseButtonSelector = "com.google.android.youtube:id/player_control_play_pause_replay_button"
    private val youTubePlayLayoutSelector = "com.google.android.youtube:id/player_overlays_custom_views_container"
    private val youTubeSearchInputSelector = "com.google.android.youtube:id/search_edit_text"
    private val youTubeResultSelector = "com.google.android.youtube:id/results"
    private val youTubeLearnMoreButtonSelector = "com.google.android.youtube:id/player_learn_more_button"
    private val youTubeAdProgressSelector = "com.google.android.youtube:id/ad_progress_text"

    private fun activateControlLayout() {
        if (getDevice().findObject(byStringRes(youTubePlayPauseButtonSelector)) == null)
            byStringRes(youTubePlayLayoutSelector).waitFindObject().click()
    }

    fun clickSearch(): YouTube {
        byContentDesc("Введите запрос").waitFindObject().click()
        return this
    }

    fun searchFor(text: String): YouTube {
        byStringRes(youTubeSearchInputSelector).waitFindObject().text = text
        getDevice().pressEnter()
        return this
    }

    fun selectResult(index: Int): YouTube {
        val result: UiObject2 = byStringRes(youTubeResultSelector).waitFindObject()
        val end = System.currentTimeMillis() + 1000
        while (System.currentTimeMillis() < end && result.children.isEmpty())
            Thread.sleep(100)
        var i = index
        while (result.children[i].className == "android.widget.LinearLayout")
            i += 1
        result.children[i].click()
        return this
    }

    fun waitAd(): YouTube {
        for (i in 1..2)
            if (byStringRes(youTubeAdProgressSelector).waitHasObject())
                getDevice().wait(Until.gone(byStringRes(youTubeLearnMoreButtonSelector)), youTubeAdWait)
        return this
    }

    fun waitForNSeconds(n: Int): YouTube {
        var time: Int = getCurrentTime()
        while (time < n - 2) {
            Thread.sleep((n - time - 2) * 1000L)
            time = getCurrentTime()
        }
        return this
    }

    private fun getCurrentTime(): Int {
        val time: List<String> =
            By.clazz("android.widget.SeekBar").waitFindObject().contentDescription.split(" из ")[0].split(":")
        return if (time.size == 3)
            time[0].toInt() * 3600 + time[1].toInt() * 60 + time[2].toInt()
        else
            time[0].toInt() * 60 + time[1].toInt()
    }

    fun stopPlay(): YouTube {
        activateControlLayout()
        byStringRes(youTubePlayPauseButtonSelector).waitFindObject().click()
        return this
    }

}