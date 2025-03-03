package kfd.lab4attempt1.repository

import kfd.lab4attempt1.model.ServiceCurrency
import kfd.lab4attempt1.enums.Currency
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceCurrencyRepository : JpaRepository<ServiceCurrency, Long> {
    fun findByCurrency(currency: Currency): ServiceCurrency?
}