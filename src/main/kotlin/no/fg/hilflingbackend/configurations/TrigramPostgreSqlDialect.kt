package no.fg.hilflingbackend.configurations

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.expression.ScalarExpression
import me.liuwj.ktorm.expression.SqlExpression
import me.liuwj.ktorm.expression.SqlFormatter
import me.liuwj.ktorm.schema.BooleanSqlType
import me.liuwj.ktorm.schema.SqlType
import me.liuwj.ktorm.support.postgresql.PostgreSqlDialect
import me.liuwj.ktorm.support.postgresql.PostgreSqlFormatter

/**
 * pg_trgm `left <% right`: true when `word_similarity(left, right)` exceeds the
 * session's `pg_trgm.word_similarity_threshold` (set in application.yml).
 * Unlike comparing `word_similarity()` directly, the operator can use a trigram index on [right].
 */
data class WordSimilarExpression(
  val left: ScalarExpression<*>,
  val right: ScalarExpression<*>,
  override val sqlType: SqlType<Boolean> = BooleanSqlType,
  override val isLeafNode: Boolean = false,
  override val extraProperties: Map<String, Any> = emptyMap(),
) : ScalarExpression<Boolean>()

/** Ktorm's PostgreSQL dialect plus [WordSimilarExpression], which Ktorm 3.1 has no syntax for */
class TrigramPostgreSqlDialect : PostgreSqlDialect() {
  override fun createSqlFormatter(
    database: Database,
    beautifySql: Boolean,
    indentSize: Int,
  ): SqlFormatter = TrigramPostgreSqlFormatter(database, beautifySql, indentSize)
}

class TrigramPostgreSqlFormatter(
  database: Database,
  beautifySql: Boolean,
  indentSize: Int,
) : PostgreSqlFormatter(database, beautifySql, indentSize) {
  override fun visitUnknown(expr: SqlExpression): SqlExpression {
    if (expr !is WordSimilarExpression) return super.visitUnknown(expr)
    write("(")
    visit(expr.left)
    write("<% ")
    visit(expr.right)
    removeLastBlank()
    write(") ")
    return expr
  }
}
