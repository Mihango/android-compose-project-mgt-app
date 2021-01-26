package labs.khobfa.projectmgt


import android.util.Log
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.coil.CoilImage
import labs.khobfa.projectmgt.ui.typography
import kotlin.math.roundToInt

enum class Status(val color: Color, val humanReadable: String) {
    New(Color(0xFF2CC09C), "New"),
    InProgress(Color(0xFFF26950), "In Progress"),
    Review(Color.Red, "Review"),
    Done(Color(0xFF5A55CA), "Done"),
}

class Project(
    val id: Int,
    val title: String,
    val date: String,
    val days: Int,
    val status: Status,
    val progress: Float,
    val users: List<User>,
    val tasks: List<Task>
)

class User(
    val id: Int,
    val name: String,
) {
    fun imageUrlForSize(size: Int) = "https://i.pravatar.cc/$size?img=$id"
}

class Task(
    val id: Int,
    val timeCode: String,
    val title: String,
    val tag: String,
    val status: Status,
    val assignees: List<User>,
    val commentCount: Int,
    val attachmentCount: Int,
)

val zachary = User(
    id = 2,
    name = "Zachary Butler",
)
val mary = User(
    id = 3,
    name = "Mary Brown",
)
val sarah = User(
    id = 4,
    name = "Sarah Murphy"
)
val mockProject = Project(
    id = 1,
    title = "Create additional pages",
    date = "Dec 18, 2019",
    days = 3,
    status = Status.InProgress,
    progress = 0.85f,
    users = listOf(mary, sarah, zachary),
    tasks = listOf(
        Task(
            id = 163,
            timeCode = "24.19",
            title = "Contact page",
            tag = "#Design",
            status = Status.InProgress,
            assignees = listOf(zachary),
            commentCount = 3,
            attachmentCount = 5,
        ),
        Task(
            id = 158,
            timeCode = "24.19",
            title = "Calculator page",
            tag = "#Design",
            status = Status.Done,
            assignees = listOf(sarah, mary),
            commentCount = 8,
            attachmentCount = 2,
        ),
        Task(
            id = 157,
            timeCode = "23.19",
            title = "Technical Task",
            tag = "#Frontend",
            status = Status.Review,
            assignees = listOf(zachary),
            commentCount = 4,
            attachmentCount = 8,
        ),
        Task(
            id = 159,
            timeCode = "23.19",
            title = "Calculator page",
            tag = "#Backend",
            status = Status.Done,
            assignees = listOf(mary),
            commentCount = 4,
            attachmentCount = 6,
        ),
        Task(
            id = 163,
            timeCode = "22.19",
            title = "Contact page",
            tag = "#Design",
            status = Status.InProgress,
            assignees = listOf(zachary),
            commentCount = 3,
            attachmentCount = 5,
        ),
        Task(
            id = 158,
            timeCode = "22.19",
            title = "Calculator page",
            tag = "#Design",
            status = Status.Done,
            assignees = listOf(sarah, mary),
            commentCount = 8,
            attachmentCount = 2,
        ),
        Task(
            id = 157,
            timeCode = "21.19",
            title = "Technical Task",
            tag = "#Frontend",
            status = Status.Review,
            assignees = listOf(zachary),
            commentCount = 4,
            attachmentCount = 8,
        ),
        Task(
            id = 159,
            timeCode = "21.19",
            title = "Calculator page",
            tag = "#Backend",
            status = Status.Done,
            assignees = listOf(mary),
            commentCount = 4,
            attachmentCount = 6,
        ),
    )
)

@Composable
fun TimelineScreen(project: Project = mockProject) {
    ScrollableColumn {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(text = project.title, fontWeight = FontWeight.Bold, style = typography.h1)
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(text = "${project.days} days", style = typography.body2)
                Text("|", modifier = Modifier.padding(horizontal = 4.dp), style = typography.body2)
                Text(text = project.date, style = typography.body2)
            }
            Row(
                Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AvatarList(
                    size = 44.dp,
                    users = project.users,
                    showAddBtn = true,
                    onAddClick = { Log.e("Add btn clicked", "show add user button") },
                    onUserClick = { index, user ->
                        Log.e("Avatar $index Clicked", "User name ${user.name} : id >>> ${user.id}")
                    }
                )

                ProjectProgressIndicator(
                    progress = project.progress,
                    primaryColor = project.status.color
                )
            }
        }

        Column(
            Modifier
                .background(Color(0xFFF1F5FE), shape = RoundedCornerShape(topLeft = 40.dp))
                .padding(top = 40.dp)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            TimelineHeader()
            for (index in project.tasks.indices) {
                val task = project.tasks[index]
                val prev = project.tasks.getOrNull(index - 1)
                val next = project.tasks.getOrNull(index + 1)
                TimelineTask(
                    task = task,
                    topLineState = when {
                        prev == null -> LineState.Undefined
                        task.timeCode == prev.timeCode -> LineState.In
                        else -> LineState.Out
                    },
                    bottomLineState = when {
                        next == null -> LineState.Undefined
                        task.timeCode == next.timeCode -> LineState.In
                        else -> LineState.Out
                    },
                )
            }
        }
    }
}

private val stroke = Stroke(8f, cap = StrokeCap.Butt)

// F26950 - proper orange
// FACEC1 - light orange
@Composable
fun ProjectProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    primaryColor: Color
) {
    val text = "${(progress * 100).roundToInt()}%"
    Box(
        modifier
            .circularProgress(progress, primaryColor)
            .size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = primaryColor,
            fontWeight = FontWeight.Bold
        )
    }
}

fun Modifier.circularProgress(
    progress: Float,
    primaryColor: Color
) = this.drawBehind {
    drawProjectProgressIndicator(
        startAngle = 270f,
        sweep = progress * 360,
        color = primaryColor,
        stroke = stroke
    )
}

private fun DrawScope.drawProjectProgressIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke,
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )

    if (sweep < 360) {
        drawArc(
            color = color.copy(alpha = 0.3f),
            startAngle = (startAngle + sweep) % 360,
            sweepAngle = 360 - sweep,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )
    }
}

// offset that ensures what is size drawn is similar to actual value
private fun Modifier.layoutOffset(x: Dp = 0.dp, y: Dp = 0.dp) =
    this then Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        return@layout layout(placeable.width + x.toIntPx(), placeable.height + y.toIntPx()) {
            placeable.placeRelative(x.toIntPx(), y.toIntPx())
        }
    }

// removed offset Modifier.offset(x = if (index != 0) (-10).dp * index else 0.dp) since it does
// not resize the parent but parent is same as without offset
@Composable
fun AvatarList(
    size: Dp,
    users: List<User>,
    modifier: Modifier = Modifier,
    showAddBtn: Boolean = false,
    onAddClick: () -> Unit,
    onUserClick: (Int, User) -> Unit
) {
    Row {
        users.forEachIndexed { index, user ->
            Avatar(
                size = size,
                user = user,
                onClick = {
                    onUserClick(index, user)
                },
                modifier = modifier
            )
        }

        if (showAddBtn) {
            AvatarPlusButton(
                size = size,
                onClick = onAddClick,
                modifier = modifier
            )
        }
    }
}

@Composable
fun AvatarPlusButton(
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    stack: Boolean = false,
) {
    Avatar(
        size = size,
        modifier = modifier,
        onClick = onClick,
        content = { Icon(Icons.Filled.Add) },
        stack = stack
    )
}

@Composable
fun Avatar(
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    stack: Boolean = false,
    content: @Composable () -> Unit
) {
    val overlap = remember { -size / 6 }
    IconButton(
        onClick = onClick,
        modifier = modifier
            .layoutOffset(x = if (stack) 0.dp else (overlap))
            .shadow(elevation = 10.dp, shape = CircleShape, clip = false)
            .background(color = Color.White, shape = CircleShape)
            .padding(2.dp)
            .size(size),
        content = content
    )
}

@Composable
fun Avatar(
    size: Dp,
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    stack: Boolean = false,
) {
    Avatar(
        size,
        onClick = onClick,
        stack = stack,
        modifier = modifier
    ) {
        CoilImage(
            data = user.imageUrlForSize(with(AmbientDensity.current) { size.toIntPx() }),
            modifier = Modifier
                .clip(shape = CircleShape)
                .fillMaxSize() //40.dp)
        )
    }
}