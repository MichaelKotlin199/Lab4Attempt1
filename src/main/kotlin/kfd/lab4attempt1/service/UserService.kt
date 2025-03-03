package kfd.lab4attempt1.service

import kfd.lab4attempt1.MoneyRecord
import kfd.lab4attempt1.enums.Currency
import kfd.lab4attempt1.model.User
import kfd.lab4attempt1.model.UserCurrency
import kfd.lab4attempt1.repository.UserCurrencyRepository
import kfd.lab4attempt1.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userCurrencyRepository: UserCurrencyRepository
) {
    @Transactional
    fun registerUser(username: String, password: String): User {
        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null) {
            throw IllegalArgumentException("Пользователь с именем $username уже существует")
        }
        val encodedPassword = passwordEncoder.encode(password)
        val user = User(username = username, password = encodedPassword)
        val savedUser = userRepository.save(user)

        // Добавляем начальные валюты пользователю
        val initialCurrencies = listOf(
            UserCurrency(user = savedUser, currency = Currency.RUB, amount = MoneyRecord(0)),
            UserCurrency(user = savedUser, currency = Currency.USD, amount = MoneyRecord(0))
        )
        userCurrencyRepository.saveAll(initialCurrencies)

        return savedUser
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
}