// src/main/kotlin/kfd/lab4attempt1/config/DataInitializer.kt
package kfd.lab4attempt1.config

import kfd.lab4attempt1.MoneyRecord
import kfd.lab4attempt1.model.ServiceCurrency
import kfd.lab4attempt1.repository.ServiceCurrencyRepository
import kfd.lab4attempt1.enums.Currency
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer {
    @Bean
    fun initServiceCurrencies(repo: ServiceCurrencyRepository) = CommandLineRunner {
        for (currency in Currency.entries) {
            if (repo.findByCurrency(currency) == null) {
                repo.save(ServiceCurrency(
                    currency = currency,
                    amount = when (currency) {
                        Currency.RUB -> MoneyRecord(1000000)
                        Currency.USD -> MoneyRecord(1000000)
                        Currency.EUR -> MoneyRecord(1000000)
                        Currency.USDT -> MoneyRecord(1000000)
                        Currency.BTC -> MoneyRecord(5)
                    }
                ))
            }
        }
    }
}