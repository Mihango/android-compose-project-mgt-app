package labs.khobfa.projectmgt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp


val linePosition = 80.dp
val DrawScope.topRight: Offset get() = Offset(size.width, 0f)
val DrawScope.bottomRight: Offset get() = Offset(size.width, size.height)

fun Modifier.drawLine() = drawBehind {
    drawLine(Color.Black, topRight, bottomRight, strokeWidth = 4f)
}

@Composable
fun TimeLineRow(
    leftContent: @Composable () -> Unit,
    rightContent: @Composable () -> Unit
) {
    Layout({
        Row(
            Modifier
                .padding(end = 16.dp)
                .drawLine(),
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
    TimeLineRow(leftContent = { Text(text = "Data") }) {
        Text(text = "Tasks")
        Text(
            text = "Show in days", textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TimelineTask(task: Task, modifier: Modifier = Modifier) {
    TimeLineRow(
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
                        Icon(Icons.Default.Notifications)
                        Text(text = "${task.commentCount}")
                    }
                }

                IconButton(
                    onClick = {},
                ) {
                    Row {
                        Icon(Icons.Default.Favorite)
                        Text(text = "${task.attachmentCount}")
                    }
                }
                Spacer(Modifier.width(5.dp))
                Text(text = "N\u00B0 ${task.id}")
                Spacer(Modifier.weight(1f))
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