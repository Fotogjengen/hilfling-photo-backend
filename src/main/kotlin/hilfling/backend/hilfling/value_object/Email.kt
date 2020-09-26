package hilfling.backend.hilfling.value_object

data class Email private constructor(val value: String) {
    init {
        require(value.isNotBlank())
    }
}