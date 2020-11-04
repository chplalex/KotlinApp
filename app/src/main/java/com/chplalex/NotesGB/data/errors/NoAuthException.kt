package com.chplalex.notesgb.data.errors

class NoAuthException : Throwable() {

    override fun equals(other: Any?): Boolean {
        return (this === other) or this.toString().equals(other.toString())
    }

}