package kfd.lab4attempt1.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it.requestMatchers("/register", "/login", "/css/**").permitAll()
                it.anyRequest().authenticated()
            }
            .formLogin {
                it.loginPage("/login") // Указываем страницу входа
                    .defaultSuccessUrl("/exchange", true) // Перенаправление после успешного входа
                    .permitAll()
            }
            .logout {
                it.logoutSuccessUrl("/login")
            }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
