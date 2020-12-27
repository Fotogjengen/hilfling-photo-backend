package no.fg.hilflingbackend.utils

private class Memoize1<in T, out R>(val f: (T) -> R) : (T) -> R {
  private val values = mutableMapOf<T, R>()
  override fun invoke(x: T): R {
    return values.getOrPut(x, { f(x) })
  }
  suspend fun test(x: T): R {
    return values.getOrPut(x, { f(x) })
  }
}

public suspend fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize1(this)
