package com.example.NotesGB.data

import com.example.NotesGB.data.model.Note

object Repository {

    val notes: List<Note> = listOf(
            Note("Моя первая заметка",
                    "Kotlin это современно",
                    0xfff06292.toInt()),
            Note("Моя вторая заметка",
                    "Kotlin это выразительно",
                    0xff9575cd.toInt()),
            Note("Моя третья заметка",
                    "Kotlin это кратко",
                    0xff64b5f6.toInt()),
            Note("Моя четвертая заметка",
                    "Kotlin это будущее",
                    0xff4db6ac.toInt()),
            Note("Моя пятая заметка",
                    "Kotlin это на долго",
                    0xffb2ff59.toInt()),
            Note("Моя шестая заметка",
                    "Kotlin это производительно",
                    0xffffeb3b.toInt()),
            Note("Моя седьмая заметка",
                    "Kotlin это к деньгам",
                    0xffff6e40.toInt())
    )

}