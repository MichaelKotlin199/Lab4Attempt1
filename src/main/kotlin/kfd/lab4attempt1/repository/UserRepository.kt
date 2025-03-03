package kfd.lab4attempt1.repository

import kfd.lab4attempt1.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}