package no.fg.hilflingbackend.value_object

import java.lang.IllegalArgumentException

data class ImageFileName private constructor(val filename: String){
  companion object {
   operator fun invoke(filename: String): ImageFileName  {
     return if (!isValidImageFileName(filename)) throw IllegalArgumentException("Not valid filename")
     else ImageFileName(filename)
   }
    private fun isValidImageFileName(filename: String): Boolean{
      // Must have a valid file extension
      return filename.split(".").size == 2
        && (
         filename.contains(".jpg", ignoreCase = true)
           || filename.contains(".png", ignoreCase = true)
      )
    }
  }
}
