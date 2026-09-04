package no.fg.hilflingbackend.service

import jakarta.persistence.EntityNotFoundException
import no.fg.hilflingbackend.dto.CategoryDto
import no.fg.hilflingbackend.dto.CategoryPatchRequestDto
import no.fg.hilflingbackend.dto.Page
import no.fg.hilflingbackend.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CategoryService(
  val repository: CategoryRepository,
) {
  fun findById(id: UUID): CategoryDto = repository.findById(id) ?: throw EntityNotFoundException("Category $id not found")

  fun findAll(
    page: Int,
    pageSize: Int,
  ): Page<CategoryDto> = repository.findAll(page, pageSize)

  fun create(dto: CategoryDto): Int = repository.create(dto)

  fun delete(id: UUID): Int = repository.delete(id)

  fun patch(dto: CategoryPatchRequestDto): CategoryDto = repository.patch(dto)
}
