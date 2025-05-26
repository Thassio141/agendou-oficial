package br.com.agendou.util

import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Timestamp.toLocalDateTime() =
    LocalDateTime.ofEpochSecond(this.seconds, this.nanoseconds, ZoneOffset.UTC)
