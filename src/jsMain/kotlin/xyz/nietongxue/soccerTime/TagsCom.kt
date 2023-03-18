package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import xyz.nietongxue.soccerTime.util.ColorU

external interface TagsProps : Props {
    var tags: List<Tag>
}

external interface TagProps : Props {
    var value: Tag
}

val TagsCom = FC<TagsProps> {
    div {
        css {

        }
        it.tags.sortedBy { it.power }.forEach {
            TagCom {
                value = it
            }
        }
    }
}

val TagCom = FC<TagProps> {
    val blueColor = ColorU("#4682B4") //SteelBlue
    val lb = blueColor.lighten(0.4f)
    val llb = blueColor.lighten(0.7f)
    val none = ColorU("#FFFFFF")
    span {
        css {
            width = 0.3.em
            height = 1.em
            marginLeft = 0.1.em
            marginRight = 0.1.em
            backgroundColor = Color(
                when (it.value.power) {
                    Power.HIGH -> blueColor
                    Power.MEDIUM -> lb
                    Power.LOW -> llb
                    else -> none
                }.string()
            )
            display = Display.inlineBlock
            borderRadius = 4.px
            borderWidth = 1.px
            borderStyle = when(it.value.power){
                Power.NEGATIVE -> LineStyle.dotted
                else  -> LineStyle.solid
            }
            borderColor = Color(blueColor.string())
        }
        +" "
    }
}

val TagWithTextCom = FC<TagProps> {
    div {
        TagCom {
            value = it.value
        }
        css {
            marginTop = 0.5.em
            marginLeft = 5.em
            fontFamily = FontFamily.monospace
            fontSize = 12.px
            display = Display.flex
            flexDirection = FlexDirection.row
        }
        div {
            css {
                marginLeft = 1.em
            }
            +it.value.text
        }
    }
}

