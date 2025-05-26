package br.com.agendou.util

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toTimestamp() =
    Timestamp(this.toEpochSecond(ZoneOffset.UTC), 0)

