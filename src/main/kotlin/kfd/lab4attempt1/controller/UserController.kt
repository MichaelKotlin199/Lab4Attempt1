package kfd.lab4attempt1.controller

import kfd.lab4attempt1.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class UserController(private val userService: UserService) {

    @GetMapping("/register")
    fun registerForm(): String {
        return "register"
    }

    @PostMapping("/register")
    fun registerUser(
        @RequestParam username: String,
        @RequestParam password: String,
        model: Model
    ): String {
        return try {
            val user = userService.registerUser(username, password)
            model.addAttribute("user", user)
            "redirect:/exchange"
        } catch (e: IllegalArgumentException) {
            model.addAttribute("error", e.message)
            "register"
        }
    }

    @GetMapping("/login")
    fun loginForm(): String {
        return "login"
    }
}