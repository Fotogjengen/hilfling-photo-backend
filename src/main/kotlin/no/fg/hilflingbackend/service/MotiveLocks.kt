package no.fg.hilflingbackend.service

import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

/**
 * Serialises operations that decide where a motive's photo files live (uploads, moves, relocations).
 * In-memory, so it assumes a single backend instance.
 */
@Component
class MotiveLocks {
  private val locks = ConcurrentHashMap<UUID, ReentrantLock>()

  fun <T> withLock(
    vararg motiveIds: UUID,
    block: () -> T,
  ): T {
    // Fixed ordering so two operations locking the same pair of motives cannot deadlock
    val ordered = motiveIds.distinct().sorted().map { locks.computeIfAbsent(it) { ReentrantLock() } }
    ordered.forEach { it.lock() }
    try {
      return block()
    } finally {
      ordered.asReversed().forEach { it.unlock() }
    }
  }
}
