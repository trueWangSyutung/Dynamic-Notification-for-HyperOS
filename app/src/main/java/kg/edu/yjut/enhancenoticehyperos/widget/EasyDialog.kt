package kg.edu.yjut.enhancenoticehyperos.widget

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeBackgroundColor
import kg.edu.yjut.enhancenoticehyperos.utils.getDarkModeTextColor

@Preview
@Composable
fun showMyDialog(
    title : String = "取件",
    content : String = "是否取件",
    ok: () -> Unit = {},
    cancel: () -> Unit = {}
) {
    AlertDialog(onDismissRequest = {
        // 关闭对话框
        cancel()
    },
        title = {
            Text(text = title)
        },
        text = { Text(text = content) },
        confirmButton = {
            Button(
                onClick = ok ) {
                Text(text = "确认")
            }
        },
        dismissButton = {
            Button(onClick = {
                cancel()
            }) {
                Text(text = "取消")
            }
        }
    )
}
@Preview
@Composable
fun EasyDialog(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
    show : Boolean = true,
    onDismissRequest: () -> Unit = {}
) {
    if (show) {
        Dialog(onDismissRequest = {
            onDismissRequest()
        }) {
            Column(
                modifier = modifier
            ) {
                content()
            }
        }
    }

}

data class SelectDialogItems<T>(
    val show: String,
    val value: T
)

@Composable
fun <T>  EasySelectDialog(
    context: Context,
    title: String = "选择",
    modifier: Modifier = Modifier,
    selcted: SelectDialogItems<T>,
    typelist: List<SelectDialogItems<T>> = mutableStateListOf(),
    onConfirm: (SelectDialogItems<T>) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()

    ) {
        val current = remember {
            mutableStateOf(selcted)
        }


        val showHomeItemsDialog = remember {
            mutableStateOf(false)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showHomeItemsDialog.value = true }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = getDarkModeTextColor(context)
            )
            Text(
                text = current.value.show,
                fontSize = 14.sp,
                color = getDarkModeTextColor(context).copy(alpha = 0.6f)
            )
        }

        if (showHomeItemsDialog.value) {
            Dialog(
                onDismissRequest = { showHomeItemsDialog.value = false },
                content = {
                    Column(
                        modifier = Modifier
                            .background(
                                color = getDarkModeBackgroundColor(
                                    context, 1
                                ),
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(15.dp)
                            .clip(
                                RoundedCornerShape(15.dp)
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        typelist.forEachIndexed { index, s ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            current.value = typelist[index]
                                            onConfirm(s)
                                            showHomeItemsDialog.value = false
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ){
                                    Text(
                                        text = s.show,
                                        fontSize = 24.sp,
                                        color = getDarkModeTextColor(context),
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                    )
                                }

                            }
                        }
                    }
                },

                )
        }
    }
}

@Composable
fun EasyButton(
    text: String = "按钮",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
    }
}


@Composable
fun EasySwitchButton(
    text: String = "按钮",
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {} ,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChange(!isChecked)
            }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
        Switch(checked = isChecked, onCheckedChange = onCheckedChange, modifier = Modifier.height(30.dp))
    }

}
@Composable
fun EasyTextButton(
    text: String =  "按钮",
    text2: String = "按钮2",
    enabled: Boolean = true,
    onClick : () -> Unit )
{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
            Button(
                enabled = enabled,
                onClick = { onClick()}
            ) {
                Text(text = text2)
            }
        }
    }

