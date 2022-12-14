package cmpt362.group14.gostudent.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class User(
    @DocumentId
    val id: String = "",
    val uid: String = "",
    val name: String = "",
    val fcm: String = "",
    val password: String = "",
    val mail: String = "",
    var createdTime: Date = Date(),
    val profileImageUrl: String = ""
)
