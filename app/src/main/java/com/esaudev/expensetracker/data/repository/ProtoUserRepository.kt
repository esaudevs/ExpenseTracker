package com.esaudev.expensetracker.data.repository

import android.content.Context
import com.esaudev.expensetracker.data.local.proto.userModelDataStore
import com.esaudev.expensetracker.domain.repository.UserRepository
import com.esaudev.expensetracker.proto.UserModel
import com.esaudev.expensetracker.proto.copy
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProtoUserRepository @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : UserRepository {

    override val user: Flow<UserModel>
        get() = applicationContext.userModelDataStore.data

    override suspend fun saveUserName(userName: String) {
        applicationContext.userModelDataStore.updateData { userModel ->
            userModel.copy {
                name = userName
            }
        }
    }
}
