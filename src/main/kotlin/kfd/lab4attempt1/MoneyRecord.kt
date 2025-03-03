package kfd.lab4attempt1

import jakarta.persistence.Embeddable

@Embeddable
data class MoneyRecord(
    var amountInSubunits: Long = 0
) {
    constructor(basicUnits: Long, subunits: Long) :
            this(basicUnits * 100 + subunits)

    fun basicUnits() = amountInSubunits / 100
    fun subunits() = amountInSubunits % 100

    operator fun plus(other: MoneyRecord) =
        MoneyRecord(amountInSubunits + other.amountInSubunits)

    operator fun minus(other: MoneyRecord) =
        MoneyRecord(amountInSubunits - other.amountInSubunits)

    operator fun compareTo(other: MoneyRecord) =
        amountInSubunits.compareTo(other.amountInSubunits)

    override fun toString() =
        "%.2f".format(amountInSubunits.toDouble() / 100)
}
