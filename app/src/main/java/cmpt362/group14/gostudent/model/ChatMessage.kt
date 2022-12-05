package cmpt362.group14.gostudent.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class ChatMessage(
    @DocumentId
    val mid: String = "",
    val text: String = "",
    val fromId: String = "",
    val toId: String = "",
    val initiateCall: Boolean = false,
    val call: Boolean = false,
    val callResponse: Boolean = false,
    val createdTime: Date = Date()
)
