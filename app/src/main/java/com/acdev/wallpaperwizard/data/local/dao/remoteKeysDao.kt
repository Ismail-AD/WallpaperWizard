package com.acdev.wallpaperwizard.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.acdev.wallpaperwizard.data.modelClasses.UnsplashRemoteKeys

@Dao
interface remoteKeysDao {
    @Query("SELECT * FROM _remote_keys_db WHERE id=:id")
    fun getRemoteKey(id: String): UnsplashRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: List<UnsplashRemoteKeys>)

    @Query("DELETE FROM _remote_keys_db")
    suspend fun deleteAllKeys()
}