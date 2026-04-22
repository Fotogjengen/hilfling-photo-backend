package no.fg.hilflingbackend.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SqlUtilsTest {
  @Test
  fun `escapeLikePattern escapes percent signs`() {
    assertEquals("100\\% done", escapeLikePattern("100% done"))
  }

  @Test
  fun `escapeLikePattern escapes underscores`() {
    assertEquals("file\\_name", escapeLikePattern("file_name"))
  }

  @Test
  fun `escapeLikePattern escapes backslashes`() {
    assertEquals("path\\\\to", escapeLikePattern("path\\to"))
  }

  @Test
  fun `escapeLikePattern handles combined special characters`() {
    assertEquals("a\\%b\\_c\\\\d", escapeLikePattern("a%b_c\\d"))
  }

  @Test
  fun `escapeLikePattern leaves normal text unchanged`() {
    assertEquals("hello world", escapeLikePattern("hello world"))
  }

  @Test
  fun `escapeLikePattern handles empty string`() {
    assertEquals("", escapeLikePattern(""))
  }
}
