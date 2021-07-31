package com.cprieto.exercises

import com.cprieto.exercises.data.Blog
import com.cprieto.exercises.data.BlogRepository
import com.cprieto.exercises.data.Post
import com.cprieto.exercises.data.PostRepository
import com.thedeanda.lorem.LoremIpsum
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication.run
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Component
import javax.transaction.Transactional


@SpringBootApplication
class App

fun main(args: Array<String>) {
    run(App::class.java, *args)
}

@Component
class PopulateSampleData(val blogs: BlogRepository, val posts: PostRepository): ApplicationRunner {
    val logger: Logger = LoggerFactory.getLogger(PopulateSampleData::class.java)

    @Transactional
    override fun run(args: ApplicationArguments?) {
        val lorem = LoremIpsum.getInstance()

        val b1 = Blog(name = "Some Blog", slug = "some-blog")
        blogs.save(b1)
        posts.saveAll(listOf(
            Post(title = "blog post for first blog", content = lorem.getWords(5, 10), blogId = b1.id),
            Post(title = "another post for first blog", content = lorem.getWords(5, 10), blogId = b1.id)
        ))

        val b2 = Blog(name = "Another Blog", slug = "another-blog")
        blogs.save(b2)
        posts.saveAll(setOf(
            Post(title = "third blog post", content = lorem.getWords(5, 10), blogId = b2.id),
            Post(content = lorem.getWords(5, 10), blogId = b2.id)
        ))

        val count = blogs.count()
        logger.info("Saved $count entities")
    }
}
