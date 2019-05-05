package com.gabrielfeo.exchangerates.domain

import java.math.BigDecimal

/**
 * Represents one of the functions of money: being a unit of measurement for defining and comparing value.
 */
interface UnitOfAccount : Comparable<UnitOfAccount> {

    /**
     * Compares this unit of account to another, resulting in the value of [other] based on this unit.
     *
     * @param other the unit that this unit should be measured against
     * @return the comparison value of the other unit based on this unit
     */
    fun valueIn(other: UnitOfAccount): BigDecimal

}