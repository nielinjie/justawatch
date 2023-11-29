package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import xyz.nietongxue.soccerTime.util.ColorU



val blueColor = ColorU("#3874CB") //color of mui primary
val lb = blueColor.lighten(0.4f)
val llb = blueColor.lighten(0.7f)
val none = ColorU("#FFFFFF")
external interface TagsProps : Props {
    var tags: List<Tag>
    var collapsed: Boolean

}

external interface TagProps : Props {
    var value: Tag
    var collapsed: Boolean
}

val TagsCom = FC<TagsProps> {props->
    div {
        css {

        }
        if(props.tags.isEmpty() && props.collapsed) {
            div {
                css{
                    color = Color(lb.string())
                    fontFamily = FontFamily.monospace
                }
                +"-"
            }
        }
        props.tags.sortedBy { it.power }.forEach {
            TagCom {
                value = it
                collapsed = props.collapsed
            }
        }
    }
}

val TagsHorizontalCom = FC<TagsProps> {props->
    span {
        css {

        }
        if(props.tags.isEmpty() && props.collapsed) {
            div {
                css{
                    color = Color(lb.string())
                    fontFamily = FontFamily.monospace
                }
                +"-"
            }
        }
        props.tags.sortedBy { it.power }.forEach {
            TagCom {
                value = it
                collapsed = props.collapsed
            }
        }
    }
}

val TagCom = FC<TagProps> {

    span {
        css {
            width = 0.3.em
            height = if(it.collapsed) {
                0.3.em
            }else {
                1.em
            }
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

