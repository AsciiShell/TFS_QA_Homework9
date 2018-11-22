package ru.tinkoff.uiautomator.test

import org.junit.Test
import ru.tinkoff.uiautomator.application.YouTube

class YoutubeTest : AbstractApplicationTest<YouTube>(YouTube()) {
    @Test
    fun youtubeTest() {
        app.open()
        app.clickSearch()
            .searchFor("рик и морти")
            .selectResult(0)
            .waitAd()
            .waitForNSeconds(10)
            .stopPlay()
    }

}