package com.esaudev.expensetracker.domain.repository

import com.esaudev.expensetracker.proto.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val user: Flow<UserModel>

    suspend fun saveUserName(userName: String)
}
