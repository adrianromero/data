//     Data Access is a Java library to store data
//     Copyright (C) 2016-2017 Adrián Romero Corchado.
//
//     This file is part of Data Access
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//
//         http://www.apache.org/licenses/LICENSE-2.0
//
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.

package com.adr.data.`var`

import com.adr.data.record.Entry
import java.math.BigDecimal

object NULL {
    val INT: VariantInt = VariantInt.NULL
    val LONG: VariantLong = VariantLong.NULL
    val STRING: VariantString = VariantString.NULL
    val DOUBLE: VariantDouble = VariantDouble.NULL
    val DECIMAL: VariantDecimal = VariantDecimal.NULL
    val BOOLEAN: VariantBoolean = VariantBoolean.NULL
    val INSTANT: VariantInstant = VariantInstant.NULL
    val LOCALDATETIME: VariantLocalDateTime = VariantLocalDateTime.NULL
    val LOCALDATE: VariantLocalDate = VariantLocalDate.NULL
    val LOCALTIME: VariantLocalTime = VariantLocalTime.NULL
    val BYTES: VariantBytes = VariantBytes.NULL
    val OBJECT: VariantObject = VariantObject.NULL
}

infix fun String.to(x: Variant): Entry = Entry(this, x)

infix fun String.to(x: String): Entry = Entry(this, x)
infix fun String.to(x: Int): Entry = Entry(this, x)
infix fun String.to(x: Double): Entry = Entry(this, x)
infix fun String.to(x: Boolean): Entry = Entry(this, x)

infix fun String.to(x: NULL): Entry = Entry(this, VariantString.NULL)

val String.INT: VariantInt
    get() = VariantInt(ISOResults(this).int)
val String.LONG: VariantLong
    get() = VariantLong(ISOResults(this).long)
val String.STRING: VariantString
    get() = VariantString(ISOResults(this).string)
val String.DOUBLE: VariantDouble
    get() = VariantDouble(ISOResults(this).double)

val Number.INT: VariantInt
    get() = VariantInt(this.toInt())
val Number.LONG: VariantLong
    get() = VariantLong(this.toLong())
val Number.DOUBLE: VariantDouble
    get() = VariantDouble(this.toDouble())
val Number.DECIMAL: VariantDecimal
    get() = VariantDecimal(BigDecimal(this.toString()))


