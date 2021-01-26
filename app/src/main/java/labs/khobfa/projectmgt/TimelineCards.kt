package labs.khobfa.projectmgt

import android.graphics.DashPathEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp


val linePosition = 80.dp
val timelineDotSize = 7.dp
val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
val linePathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 0f), 0f)

val DrawScope.topRight: Offset get() = Offset(size.width, 0f)
val DrawScope.bottomRight: Offset get() = Offset(size.width, size.height)

enum class LineState(val color: Color, val stroke: Stroke) {
    Undefined(Color.Gray, Stroke(width = 1f, pathEffect = dashPathEffect)), // dotted line
    In(Color.White, Stroke(width = 8f, pathEffect = linePathEffect)), // solid white
    Out(Color.Gray, Stroke(width = 1f, pathEffect = linePathEffect)), // solid gray
}


fun Modifier.drawLine2() = drawBehind {
    val centerPosition = Offset(x = size.width, y = size.height / 2f)
    drawLine(Color.Black, topRight, bottomRight, strokeWidth = 4f)
    drawCircle(
        color = Color.White,
        radius = timelineDotSize.toPx(),
        center = centerPosition
    )

    drawCircle(
        color = Status.InProgress.color,
        radius = (timelineDotSize - 1.dp).toPx(),
        center = centerPosition
    )
}

fun Modifier.drawLine(
    status: Status?,
    top: LineState,
    bottom: LineState
) = drawBehind {
    val centerPosition = Offset(x = size.width, y = size.height / 2f)
    drawLine(
        color = top.color,
        start = topRight,
        end = centerPosition,
        strokeWidth = top.stroke.width,
    )
    drawLine(
        color = bottom.color,
        start = centerPosition,
        end = bottomRight,
        strokeWidth = bottom.stroke.width,
    )

    if (status != null) {
        drawCircle(
            color = Color.White,
            radius = timelineDotSize.toPx(),
            center = centerPosition
        )
        drawCircle(
            color = status.color,
            radius = (timelineDotSize - 2.dp).toPx(),
            center = centerPosition
        )
    }
}

@Composable
fun TimeLineRow(
    status: Status?,
    topLineState: LineState,
    bottomLineState: LineState,
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit
) {
    Layout({
        Row(
            Modifier
                .padding(end = 16.dp)
                .drawLine(status, topLineState, bottomLineState),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leftContent()
        }
        Row {
            rightContent()
        }
    }) { (left, right), constraints ->
        val leftWidth = linePosition.toIntPx()
        val placedRight = right.measure(
            Constraints(
                minWidth = (constraints.minWidth - leftWidth).coerceAtLeast(0),
                maxWidth = (constraints.maxWidth - leftWidth).coerceAtLeast(0),
                minHeight = constraints.minHeight,
                maxHeight = constraints.maxHeight
            )
        )
        val placedLeft = left.measure(Constraints.fixed(leftWidth, placedRight.height))

        layout(placedLeft.width + placedRight.width, placedRight.height) {
            placedLeft.placeRelative(0, 0)
            placedRight.placeRelative(leftWidth, 0)
        }
    }
//    Column(
//        Modifier
//            .padding(end = 16.dp)
//            .drawLine()
//            .width(linePosition),
//        verticalArrangement = Arrangement.Center,
//    ) {
//        rightContent()
//    }
}

@Composable
fun TimelineHeader() {
    TimeLineRow(status = null,
        topLineState = LineState.Undefined,
        bottomLineState = LineState.Undefined,
        leftContent = { Text(text = "Data") }) {
        Text(text = "Tasks")
        Text(
            text = "Show in days", textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TimelineTask(
    task: Task,
    topLineState: LineState,
    bottomLineState: LineState,
) {
    TimeLineRow(
        status = task.status,
        topLineState = topLineState,
        bottomLineState = bottomLineState,
        leftContent = { Text(text = task.timeCode) }
    ) {
        Column(
            Modifier
                .padding(vertical = 8.dp)
                .background(Color.White, RoundedCornerShape(6.dp))
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = task.status.humanReadable,
                    color = task.status.color
                )
                Text(text = task.tag)
            }

            Row {
                Text(text = task.title)
            }

            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {},
                ) {
                    Row {
                        Icon(Icons.Default.ChatBubbleOutline)
                        Text(text = "${task.commentCount}")
                    }
                }

                IconButton(
                    onClick = {},
                ) {
                    Row {
                        Icon(Icons.Default.Attachment)
                        Text(text = "${task.attachmentCount}")
                    }
                }
                Spacer(Modifier.weight(1f))
                Text(text = "N\u00B0 ${task.id}")
                Spacer(modifier = Modifier.width(5.dp))
                AvatarList(
                    size = 32.dp,
                    users = task.assignees,
                    onAddClick = { /*TODO*/ },
                    onUserClick = { index, user ->/*TODO*/ }
                )
            }
        }
    }
}

@Composable
fun TimelineMessageIcon(onClick: () -> Unit, content: @Composable () -> Unit) {
    Row(
        Modifier
            .padding(8.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        content()
    }
}