package no.fg.hilflingbackend.repository

import me.liuwj.ktorm.database.Database
import no.fg.hilflingbackend.dto.FilterSuggestionDto
import no.fg.hilflingbackend.dto.FilterSuggestionType
import org.springframework.stereotype.Repository

@Repository
class SearchSuggestionsRepository(
  val database: Database,
) {
  fun findFilterSuggestions(term: String): List<FilterSuggestionDto> {
    if (term.isBlank()) return emptyList()

    // grusom query, men ktrom kan ikke gjøre dette
    val sql =
      """
      SELECT type, id, display_text
      FROM (
        SELECT 'PLACE' AS type, id::text AS id, name AS display_text,
          GREATEST(
            similarity(name, ?),
            CASE WHEN name ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END
          ) AS score
        FROM place WHERE date_deleted IS NULL

        UNION ALL

        SELECT 'EVENT_OWNER' AS type, id::text AS id, name AS display_text,
          GREATEST(
            similarity(name, ?),
            CASE WHEN name ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END
          ) AS score
        FROM event_owner WHERE date_deleted IS NULL

        UNION ALL

        SELECT 'CATEGORY' AS type, id::text AS id, name AS display_text,
          GREATEST(
            similarity(name, ?),
            CASE WHEN name ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END
          ) AS score
        FROM category WHERE date_deleted IS NULL

        UNION ALL

        SELECT 'SECURITY_LEVEL' AS type, level AS id, level AS display_text,
          GREATEST(
            similarity(level, ?),
            CASE WHEN level ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END
          ) AS score
        FROM (VALUES ('FG'), ('HUSFOLK'), ('ALLE')) AS sl(level)

        UNION ALL

        SELECT 'ALBUM' AS type, id::text AS id,
          CASE WHEN description IS NOT NULL THEN name || ' (' || description || ')' ELSE name END AS display_text,
          GREATEST(
            similarity(name, ?),
            similarity(COALESCE(description, ''), ?),
            CASE WHEN name ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END,
            CASE WHEN description ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END
          ) AS score
        FROM album WHERE date_deleted IS NULL

        UNION ALL

        SELECT 'MOTIVE' AS type, id::text AS id, title AS display_text,
          GREATEST(
            similarity(title, ?),
            CASE WHEN title ILIKE '%' || ? || '%' THEN 0.3 ELSE 0 END
          ) AS score
        FROM motive WHERE date_deleted IS NULL
      ) combined
      WHERE score > 0.05
      ORDER BY score DESC
      LIMIT 10
      """.trimIndent()

    // place(2), event_owner(2), category(2), security_level(2), album(4), motive(2) = 14 params
    val params =
      arrayOf(
        term,
        term,
        term,
        term,
        term,
        term,
        term,
        term,
        term,
        term,
        term,
        term,
        term,
        term,
      )

    return database.useConnection { conn ->
      conn.prepareStatement(sql).use { stmt ->
        params.forEachIndexed { i, p -> stmt.setString(i + 1, p) }
        stmt.executeQuery().use { rs ->
          val results = mutableListOf<FilterSuggestionDto>()
          while (rs.next()) {
            results +=
              FilterSuggestionDto(
                type = FilterSuggestionType.valueOf(rs.getString("type")),
                id = rs.getString("id"),
                displayText = rs.getString("display_text"),
              )
          }
          results
        }
      }
    }
  }
}
