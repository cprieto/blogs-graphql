package com.cprieto.exercises.data

import org.springframework.data.repository.CrudRepository
import javax.transaction.Transactional

interface BlogRepository: CrudRepository<Blog, Long>

interface PostRepository: CrudRepository<Post, Long> {
    @Transactional
    fun findAllByBlogId(id: Long): Set<Post>
}