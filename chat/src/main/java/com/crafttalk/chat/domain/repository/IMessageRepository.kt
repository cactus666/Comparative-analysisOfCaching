package com.crafttalk.chat.domain.repository

import androidx.paging.DataSource
import com.crafttalk.chat.data.local.db.entity.MessageEntity

interface IMessageRepository {
    fun getMessages(uuid: String): DataSource.Factory<Int, MessageEntity>
    suspend fun sendMessages(message: String)
    suspend fun syncMessages(timestamp: Long)
    suspend fun selectAction(actionId: String)

    fun updateSizeMessage(idKey: Long, height: Int, width: Int)
    fun readMessage(uuid: String, id: String)
}