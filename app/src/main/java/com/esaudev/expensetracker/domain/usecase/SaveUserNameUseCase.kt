package com.esaudev.expensetracker.domain.usecase

import com.esaudev.expensetracker.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun execute(userName: String) {
        userRepository.saveUserName(
            userName = userName
        )
    }
}
