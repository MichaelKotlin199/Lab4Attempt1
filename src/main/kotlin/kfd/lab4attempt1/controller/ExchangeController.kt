// src/main/kotlin/kfd/lab4attempt1/controller/ExchangeController.kt
package kfd.lab4attempt1.controller

import kfd.lab4attempt1.enums.Currency
import kfd.lab4attempt1.service.CurrencyService
import kfd.lab4attempt1.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ExchangeController(
    private val currencyService: CurrencyService,
    private val userService: UserService
) {
    @GetMapping("/exchange")
    fun exchangePage(
        @AuthenticationPrincipal userDetails: UserDetails,
        model: Model
    ): String {
        val user = userService.findByUsername(userDetails.username)
            ?: return "redirect:/login" // Если пользователь не найден, перенаправляем на страницу входа

        val userCurrencies = currencyService.getUserCurrencies(user)
        val serviceCurrencies = currencyService.getServiceCurrencies()
        val exchangeRates = currencyService.getExchangeRates()

        model.addAttribute("userCurrencies", userCurrencies)
        model.addAttribute("serviceCurrencies", serviceCurrencies)
        model.addAttribute("rates", exchangeRates) // Передаем курсы валют
        model.addAttribute("currencyList", Currency.entries)
        return "exchange"
    }

    @PostMapping("/exchange")
    fun exchangeCurrency(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam fromCurrency: Currency,
        @RequestParam toCurrency: Currency,
        @RequestParam amount: Double,
        model: Model
    ): String {
        val user = userService.findByUsername(userDetails.username)
            ?: return "redirect:/login" // Если пользователь не найден, перенаправляем на страницу входа

        return try {
            currencyService.exchangeCurrency(user, fromCurrency, toCurrency, amount)
            model.addAttribute("success", "Exchange successful!")
            "redirect:/exchange"
        } catch (e: Exception) {
            model.addAttribute("error", e.message)
            "exchange"
        }
    }

    @PostMapping("/addFunds")
    fun addFunds(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam currency: Currency,
        @RequestParam amount: Double,
        model: Model
    ): String {
        val user = userService.findByUsername(userDetails.username)
            ?: return "redirect:/login" // Если пользователь не найден, перенаправляем на страницу входа

        return try {
            currencyService.addFunds(user, currency, amount)
            model.addAttribute("success", "Funds added successfully!")
            "redirect:/exchange"
        } catch (e: Exception) {
            model.addAttribute("error", e.message)
            "exchange"
        }
    }
}