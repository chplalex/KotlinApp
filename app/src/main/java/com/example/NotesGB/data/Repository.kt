package com.example.NotesGB.data

import com.example.NotesGB.data.model.Color.*
import com.example.NotesGB.data.model.Note
import java.util.*

object Repository {

    val notes: List<Note> = listOf(
            Note(UUID.randomUUID().toString(),
                    "Моя первая заметка",
                    "Kotlin это современно",
                    WHITE),
            Note(UUID.randomUUID().toString(),
                    "Моя вторая заметка",
                    "Kotlin это выразительно",
                    BLUE),
            Note(UUID.randomUUID().toString(),
                    "Моя третья заметка",
                    "Kotlin это кратко",
                    GREEN),
            Note(UUID.randomUUID().toString(),
                    "Моя четвертая заметка",
                    "Kotlin это будущее",
                    PINK),
            Note(UUID.randomUUID().toString(),
                    "Моя пятая заметка",
                    "Kotlin это на долго",
                    RED),
            Note(UUID.randomUUID().toString(),
                    "Моя шестая заметка",
                    "Kotlin это производительно",
                    YELLOW),
            Note(UUID.randomUUID().toString(),
                    "Моя седьмая заметка",
                    "Kotlin это к деньгам",
                    VIOLET)
    )

}