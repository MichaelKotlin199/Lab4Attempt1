package kfd.lab4attempt1.repository

import kfd.lab4attempt1.model.User
import kfd.lab4attempt1.model.UserCurrency
import kfd.lab4attempt1.enums.Currency
import org.springframework.data.jpa.repository.JpaRepository

interface UserCurrencyRepository : JpaRepository<UserCurrency, Long> {
    fun findByUserAndCurrency(user: User, currency: Currency): UserCurrency?
    fun findByUser(user: User): List<UserCurrency>
}

