package com.opencallshield.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpamDao {

    // ---- spam_numbers ----

    @Query("SELECT * FROM spam_numbers ORDER BY lastReported DESC")
    fun observeAll(): Flow<List<SpamNumber>>

    @Query("SELECT * FROM spam_numbers WHERE number = :number LIMIT 1")
    suspend fun findByNumber(number: String): SpamNumber?

    @Query("SELECT COUNT(*) FROM spam_numbers")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: SpamNumber)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<SpamNumber>)

    @Delete
    suspend fun delete(item: SpamNumber)

    @Query("DELETE FROM spam_numbers WHERE number = :number")
    suspend fun deleteByNumber(number: String)

    // ---- blocked_calls ----

    @Query("SELECT * FROM blocked_calls ORDER BY timestamp DESC LIMIT 200")
    fun observeBlocked(): Flow<List<BlockedCall>>

    @Insert
    suspend fun insertBlocked(call: BlockedCall)

    @Query("DELETE FROM blocked_calls")
    suspend fun clearBlocked()

    @Query("SELECT COUNT(*) FROM blocked_calls")
    suspend fun blockedCount(): Int
}
