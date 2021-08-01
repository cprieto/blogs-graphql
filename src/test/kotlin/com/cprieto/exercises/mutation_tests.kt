package com.cprieto.exercises

import com.cprieto.exercises.data.BlogRepository
import com.cprieto.exercises.data.PostRepository
import com.graphql.spring.boot.test.GraphQLTestTemplate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MutationTests {
    @Autowired
    lateinit var client: GraphQLTestTemplate

    @Autowired
    lateinit var blogs: BlogRepository

    @Autowired
    lateinit var posts: PostRepository

    private fun doTest(name: String) {
        val response = client.postForResource(name.test("mutations"))
        Assertions.assertTrue(response.isOk)
        JSONAssert.assertEquals(name.response("mutations"), response.rawResponse.body, true)
    }

    @Test
    fun `It can create a single blog`() {
        assertEquals(0, blogs.count())
        doTest("create-blog")
        assertEquals(1, blogs.count())
    }

    @Test
    fun `It can create a single blog with two posts`() {
        assertEquals(0, blogs.count())
        assertEquals(0, posts.count())

        doTest("create-blog-posts")

        assertEquals(1, blogs.count())
        assertEquals(2, posts.count())
        assertEquals(2, posts.findAllByBlogId(1).count())
    }
}