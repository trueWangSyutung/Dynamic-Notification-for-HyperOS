package kg.edu.yjut.enhancenoticehyperos.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notice")
data class NoticeHistory(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var packageName: String = "",
    var title: String = "",
    var content: String = "",
    var chennel: String = "",
    var sendTime: Long = 0L,
)