package kfd.lab4attempt1.model

import jakarta.persistence.*
import kfd.lab4attempt1.MoneyRecord
import kfd.lab4attempt1.enums.Currency

@Entity
@Table(name = "service_currency")
data class ServiceCurrency(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val currency: Currency,

    @Embedded
    var amount: MoneyRecord
)
