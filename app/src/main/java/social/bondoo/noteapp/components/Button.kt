package social.bondoo.noteapp.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enable: Boolean = true
) {
    Button(
        onClick = { onClick() },
        enabled = enable,
        shape = CircleShape,
        modifier = modifier
    ) {
        Text(text = text)
    }
}