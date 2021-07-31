package com.cprieto.exercises.gql

import com.cprieto.exercises.data.Blog
import com.cprieto.exercises.data.BlogRepository
import com.cprieto.exercises.data.Post
import com.cprieto.exercises.data.PostRepository
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.GraphQLResolver
import org.springframework.stereotype.Component


@Component
class BlogQueryResolver(val repo: BlogRepository): GraphQLQueryResolver {
    fun blog(id: Long): Blog = repo.findById(id).get()
    fun blogs(): List<Blog> = repo.findAll().toList()
}

@Component
class PostQueryResolver(val repo: PostRepository): GraphQLQueryResolver {
    fun post(id: Long) = repo.findById(id).get()
}

@Component
class BlogResolver(val data: PostRepository): GraphQLResolver<Blog> {
    fun posts(blog: Blog): Set<Post> {
        return data.findAllByBlogId(blog.id)
    }
}

@Component
class PostResolver(val data: BlogRepository): GraphQLResolver<Post> {
    fun blog(post: Post): Blog {
        return data.findById(post.blogId).get()
    }
}
