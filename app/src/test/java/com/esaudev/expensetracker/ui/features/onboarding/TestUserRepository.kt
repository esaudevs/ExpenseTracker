package com.esaudev.expensetracker.ui.features.onboarding

import com.esaudev.expensetracker.domain.repository.UserRepository
import com.esaudev.expensetracker.proto.UserModel
import com.esaudev.expensetracker.proto.copy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

val emptyUserModel = UserModel.getDefaultInstance()

class TestUserRepository : UserRepository {

    private val _userModel = MutableStateFlow<UserModel>(emptyUserModel)

    override val user: Flow<UserModel>
        get() = _userModel

    override suspend fun saveUserName(userName: String) {
        _userModel.value = _userModel.value.copy {
            name = userName
        }
    }
}
