package com.cprieto.exercises

import com.cprieto.exercises.data.Blog
import com.cprieto.exercises.data.BlogRepository
import com.cprieto.exercises.data.Post
import com.cprieto.exercises.data.PostRepository
import com.graphql.spring.boot.test.GraphQLTestTemplate
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QueryTests {
    @Autowired
    lateinit var client: GraphQLTestTemplate

    @Autowired
    lateinit var blogs: BlogRepository

    @Autowired
    lateinit var posts: PostRepository

    @BeforeAll
    fun initData() {
        val b1 = Blog(name = "Some Blog", slug = "some-blog")
        blogs.save(b1)
        posts.saveAll(listOf(
            Post(title = "blog post for first blog", content = "latine iudicabit varius inimicus quis decore fabellas quem", blogId = b1.id),
            Post(title = "another post for first blog", content = "disputationi ad fermentum leo quem dolorem", blogId = b1.id)
        ))

        val b2 = Blog(name = "Another Blog", slug = "another-blog")
        blogs.save(b2)
        posts.saveAll(setOf(
            Post(title = "third blog post", content = "ante vocibus comprehensam nullam vel nobis a sea", blogId = b2.id),
            Post(content = "omittantur vero iusto fusce persius quas quas donec", blogId = b2.id)
        ))
    }

    private fun doTest(testName: String) {
        val response = client.postForResource(testName.test())
        assertTrue(response.isOk)
        JSONAssert.assertEquals(testName.response(), response.rawResponse.body, true)
    }

    @Test
    fun `It can query all blogs`() = doTest("all-blogs")

    @Test
    fun `It can query given blog basic data`() = doTest("single-blog")

    @Test
    fun `It can query blog and posts`() = doTest("blog-posts")

    @Test
    fun `It can query full blog data`() = doTest("blog-full")

    @Test
    fun `It can grab all posts`() = doTest("all-posts")

    @Test
    fun `It can query all posts with properties`() = doTest("posts-full")

    @Test
    fun `It can query all posts with hierarchical blog and posts`() = doTest("posts-complex")

    @Test
    fun `It can query a single post`() = doTest("single-post")
}