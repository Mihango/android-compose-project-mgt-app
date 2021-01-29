package labs.khobfa.projectmgt

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.accompanist.coil.CoilImage

val bgColor = Color(0xFF33354E)

class Client(
    val id: Int,
    val name: String,
    val logo: String
)

val mockClients = listOf(
    Client(
        id = 0,
        name = "Awsmd Team",
        logo = "",
    ),
    Client(
        id = 1,
        name = "Google",
        logo = "",
    ),
    Client(
        id = 2,
        name = "Airbnb",
        logo = "",
    ),
)

class Attachment(
    val name: String,
    val size: Int,
    val preview: String,
) {
    val progress: Float get() = 0.8f
}

val mockAttachment = Attachment(
    name = "Reference_1",
    preview = "https://i.pravatar.cc/200?img=50",
    size = 168,
)

@Composable
fun CreateTaskScreen(clients: List<Client>) {
    var projectName by remember { mutableStateOf("Create additional pages") }
    var client by remember { mutableStateOf(clients.first()) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Design") }
    val attachments = remember { mutableStateListOf(mockAttachment) }

    ScrollableColumn()
    {
        Surface(
            color = bgColor,
            contentColor = Color.White
        ) {
            Column {

                Column(
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Field(label = "CLIENT") {
                        ClientDropdownField(
                            selectedValue = client,
                            values = clients,
                            onValueChange = { client = it }
                        )
                    }

                    Field(label = "PROJECT NAME") {
                        PMTextField(
                            modifier = Modifier.weight(1f),
                            value = projectName,
                            onValueChange = { projectName = it },
                        )
                    }

                    Field(label = "PROJECT TIMELINE") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DatePicker(
                                value = startDate,
                                onValueChange = { startDate = it },
                                modifier = Modifier.weight(1f)
                            )
                            Text(text = "-", modifier = Modifier.padding(horizontal = 10.dp))
                            DatePicker(
                                value = endDate,
                                onValueChange = { endDate = it },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Field(label = "ASSIGNEE") {
                        AvatarList(
                            size = 40.dp,
                            users = mockProject.users,
                            showAddBtn = true,
                            onAddClick = { },
                            onUserClick = { index, user -> /*TODO*/ })
                    }

                    Field(label = "CATEGORY") {
                        RadioChips(
                            values = listOf("Design", "Frontend", "Backend"),
                            selectedValue = category,
                            onSelectedChange = { category = it })
                    }
                }

                Surface(
                    shape = RoundedCornerShape(
                        topLeft = 24.dp,
                        topRight = 24.dp,
                    ),
                    color = Color.White,
                ) {
                    Column(
                        Modifier
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Description")
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(Icons.Default.Attachment, tint = primaryGreen)
                            }
                        }
                        Text(text = "ATTACHMENTS")
                        for (attachment in attachments) {
                            AttachmentProgress(attachment = attachment)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = primaryGreen,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(corner = CornerSize(16.dp))
                        ) {
                            Text("CREATE TASK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentProgress(attachment: Attachment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White),
            data = attachment.preview
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = attachment.name)
                Text(text = "${attachment.size} KB")
            }
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = attachment.progress,
                color = primaryGreen
            )
        }
        Spacer(modifier = Modifier.size(8.dp))
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Icon(Icons.Default.Close)
        }
    }
}

@Composable
inline fun Field(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable RowScope.() -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row {
            content()
        }
    }
}

val primaryGreen = Color(0xFF2CC09C)

@Composable
fun RadioChips(
    values: List<String>,
    selectedValue: String,
    onSelectedChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val selectedColor =
            ButtonDefaults.buttonColors(backgroundColor = primaryGreen, contentColor = Color.White)
        val unselectedColor = ButtonDefaults.buttonColors(backgroundColor = Color.White)

        for (value in values) {
            val selected = value == selectedValue
            Button(
                contentPadding = PaddingValues(
                    start = 8.dp,
                    end = 24.dp,
                    top = 6.dp,
                    bottom = 6.dp
                ),
                colors = if (selected) selectedColor else unselectedColor,
                onClick = { onSelectedChange(value) },
                shape = RoundedCornerShape(8.dp),
            ) {
                if (selected)
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Check,
                    )
                else
                    Spacer(modifier = Modifier.size(16.dp))

                Text(
                    text = value,
                    color = if (selected) Color.White else Color.Blue
                )
            }
        }
    }
}


@Composable
fun ClientDropdownField(
    selectedValue: Client,
    onValueChange: (Client) -> Unit,
    values: List<Client>,
) {
    // show the name
    Row {
        CoilImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White),
            data = "https://i.pravatar.cc/200?img=$60"
        )
    }
    Spacer(modifier = Modifier.size(6.dp))
    PMTextField(
        value = selectedValue.name,
        onValueChange = { client -> onValueChange(values[0]) },
    )
}

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        activeColor = Color.White,
        inactiveColor = Color.White.copy(alpha = 0.7f),
        backgroundColor = bgColor
    )
}

@Composable
fun <T> DropdownField(
    selectedValue: T,
    onValueChange: (T) -> Unit,
    values: List<T>,
    label: String,
    composeItems: @Composable (T) -> Unit
) {

}

@Composable
fun PMTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        activeColor = Color.White,
        inactiveColor = Color.White.copy(alpha = 0.7f),
        backgroundColor = bgColor
    )
}