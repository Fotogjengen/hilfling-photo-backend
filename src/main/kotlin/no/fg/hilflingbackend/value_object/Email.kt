package no.fg.hilflingbackend.value_object

data class Email private constructor(val value: String) {
    init {
        require(value.isNotBlank())
    }
}