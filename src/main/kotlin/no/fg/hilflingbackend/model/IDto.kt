package no.fg.hilflingbackend.model

interface IDto<T> {
  fun toDto(): T
}
