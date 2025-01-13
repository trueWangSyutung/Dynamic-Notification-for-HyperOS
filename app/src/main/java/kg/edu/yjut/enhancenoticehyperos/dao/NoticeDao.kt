/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package kg.edu.yjut.enhancenoticehyperos.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kg.edu.yjut.enhancenoticehyperos.entity.NoticeHistory

@Dao
interface NoticeDao {
    @Insert
    fun insertNotice(notice: NoticeHistory)
    @Insert
    fun insertNoticeList(notice: List<NoticeHistory>)
    @Update
    fun updateNotice(notice: NoticeHistory)
    @Update
    fun updateNoticeList(notice: List<NoticeHistory>)
    @Delete
    fun deleteNotice(notice: NoticeHistory)
    @Delete
    fun deleteNoticeList(notice: List<NoticeHistory>)
    @Query("SELECT * FROM notice ORDER BY sendTime DESC LIMIT :limit OFFSET :offset ")
    fun getNoticeList(limit: Int, offset: Int): List<NoticeHistory>
    @Query("SELECT * FROM notice WHERE id = :id")
    fun getNotice(id: String): NoticeHistory?

    // 删除七天以外的记录
    @Query("DELETE FROM notice WHERE sendTime < :time")
    fun deleteNoticeBeforeTime(time: Long)

}