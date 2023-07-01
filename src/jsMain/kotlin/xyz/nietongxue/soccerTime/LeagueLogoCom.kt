package xyz.nietongxue.soccerTime

import react.FC
import react.Props
import react.dom.html.ReactHTML.img
import xyz.nietongxue.soccerTime.util.iconUnkownUrl

val LeagueLogoCom = FC<LeagueLogoComProp> {
    img {
        src = leagueLogos[it.fixture.leagueSeason.first] ?: iconUnkownUrl
        width = 15.0

    }
}

external interface LeagueLogoComProp : Props {
    var fixture: Fixture
}