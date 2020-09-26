package hilfling.backend.hilfing.value_object

data class Email private constructor(val value: String) {
    init {
        require(value.isNotBlank())
    }
}