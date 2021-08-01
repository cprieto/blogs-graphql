package com.cprieto.exercises.gql

import com.cprieto.exercises.data.Blog
import com.cprieto.exercises.data.BlogRepository
import com.cprieto.exercises.data.Post
import com.cprieto.exercises.data.PostRepository
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import javax.transaction.Transactional

data class CreateBlog(
    val name: String,
    val slug: String,
    val posts: Set<CreateBlogPost>? = emptySet()
)

data class CreatePost(
    val title: String?,
    val content: String,
    val blogId: Long
)

data class CreateBlogPost(
    val title: String?,
    val content: String
)

@Component
class BlogMutation(val blogs: BlogRepository, val posts: PostRepository): GraphQLMutationResolver {
    @Transactional
    fun createBlog(input: CreateBlog): Blog {
        val blog = Blog(
            name=input.name,
            slug=input.slug
        )
        blogs.save(blog)

        input.posts?.forEach {
            posts.save(Post(content = it.content, blogId = blog.id, title = it.title))
        }

        return blog
    }
}

@Component
class PostMutation(val data: PostRepository): GraphQLMutationResolver {
    @Transactional
    fun createPost(input: CreatePost): Post {
        val post = Post(
            title = input.title,
            content = input.content,
            blogId = input.blogId
        )
        data.save(post)

        return post
    }
}
