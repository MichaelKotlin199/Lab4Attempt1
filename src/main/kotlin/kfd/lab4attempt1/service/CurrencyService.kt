package kfd.lab4attempt1.service

import kfd.lab4attempt1.MoneyRecord
import kfd.lab4attempt1.model.User
import kfd.lab4attempt1.model.*
import kfd.lab4attempt1.repository.ServiceCurrencyRepository
import kfd.lab4attempt1.repository.UserCurrencyRepository
import kfd.lab4attempt1.enums.Currency
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CurrencyService(
    private val userCurrencyRepo: UserCurrencyRepository,
    private val serviceCurrencyRepo: ServiceCurrencyRepository
) {
    private val exchangeRates: Map<Currency, Map<Currency, Double>> = mapOf(
        Currency.RUB to mapOf(
            Currency.USD to 1.0 / 90.0,
            Currency.EUR to 1.0 / 100.0
        ),
        Currency.USD to mapOf(
            Currency.RUB to 90.0,
            Currency.EUR to 0.9,
            Currency.USDT to 1.0,
            Currency.BTC to 1.0 / 59112.0
        ),
        Currency.EUR to mapOf(
            Currency.RUB to 100.0,
            Currency.USD to 1.0 / 9.0
        ),
        Currency.USDT to mapOf(
            Currency.USD to 1.0
        ),
        Currency.BTC to mapOf(
            Currency.USD to 59112.0
        )
    )


    @Transactional
    fun exchangeCurrency(
        user: User,
        fromCurrency: Currency,
        toCurrency: Currency,
        amount: Double
    ): Boolean {
        val from = userCurrencyRepo.findByUserAndCurrency(user, fromCurrency)
            ?: throw IllegalArgumentException("Currency not found")

        val serviceFrom = serviceCurrencyRepo.findByCurrency(fromCurrency)
            ?: throw IllegalStateException("Service currency error")

        val rate = exchangeRates[fromCurrency]?.get(toCurrency)
            ?: throw IllegalArgumentException("Exchange rate not available")

        val totalAmount = (amount * 100).toLong()
        if (from.amount.amountInSubunits < totalAmount) {
            throw IllegalArgumentException("Insufficient funds")
        }

        val convertedAmount = (totalAmount * rate).toLong()

        val serviceTo = serviceCurrencyRepo.findByCurrency(toCurrency)
            ?: throw IllegalStateException("Service currency error")

        if (serviceTo.amount.amountInSubunits < convertedAmount) {
            throw IllegalArgumentException("Service out of currency")
        }

        from.amount -= MoneyRecord(totalAmount)
        serviceFrom.amount += MoneyRecord(totalAmount)

        val to = userCurrencyRepo.findByUserAndCurrency(user, toCurrency) ?:
        UserCurrency(user = user, currency = toCurrency, amount = MoneyRecord(0))

        to.amount += MoneyRecord(convertedAmount)
        serviceTo.amount -= MoneyRecord(convertedAmount)

        userCurrencyRepo.save(from)
        userCurrencyRepo.save(to)
        serviceCurrencyRepo.save(serviceFrom)
        serviceCurrencyRepo.save(serviceTo)

        return true
    }

    fun getUserCurrencies(user: User) =
        userCurrencyRepo.findByUser(user)

    fun getExchangeRates() = exchangeRates

    fun getServiceCurrencies(): List<ServiceCurrency> {
        return serviceCurrencyRepo.findAll()
    }

    @Transactional
    fun addFunds(user: User, currency: Currency, amount: Double) {
        val totalAmount = (amount * 100).toLong()
        val userCurrency = userCurrencyRepo.findByUserAndCurrency(user, currency)
            ?: UserCurrency(user = user, currency = currency, amount = MoneyRecord(0))

        userCurrency.amount += MoneyRecord(totalAmount)
        userCurrencyRepo.save(userCurrency)
    }
}