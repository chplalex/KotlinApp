package com.example.NotesGB.data.model

class Note(var title: String, var body: String, var color: Int) {

    constructor() : this("", "", 0xfff06292.toInt())

    constructor(title: String, note: String) : this(title, note, 0xfff06292.toInt())

}
