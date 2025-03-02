package kfd.lab4attempt1

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
            "redirect:/home"
        } catch (e: IllegalArgumentException) {
            model.addAttribute("error", e.message)
            "register"
        }
    }

    @GetMapping("/login")
    fun loginForm(): String {
        return "login"
    }

    @GetMapping("/home")
    fun home(): String {
        return "home"
    }
}