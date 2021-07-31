package com.cprieto.exercises.gql

import com.cprieto.exercises.data.Blog
import com.cprieto.exercises.data.BlogRepository
import com.cprieto.exercises.data.Post
import com.cprieto.exercises.data.PostRepository
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component


@Component
class BlogQueryResolver(val repo: BlogRepository): GraphQLQueryResolver {
    fun blog(id: Long) = repo.findByIdOrNull(id)
    fun blogs() = repo.findAll().toList()
}

@Component
class PostQueryResolver(val repo: PostRepository): GraphQLQueryResolver {
    fun post(id: Long) = repo.findByIdOrNull(id)
    fun posts() = repo.findAll().toList()
}

@Component
class BlogResolver(val data: PostRepository): GraphQLResolver<Blog> {
    fun posts(blog: Blog) =data.findAllByBlogId(blog.id)
}

@Component
class PostResolver(val data: BlogRepository): GraphQLResolver<Post> {
    fun blog(post: Post) = data.findByIdOrNull(post.blogId)
}
