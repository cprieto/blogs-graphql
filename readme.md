# Simple blog API in GraphQL

This is an experiment to expose database operations in blog data using [GraphQL](https://graphql.org/), with this API is possible to:

 - Get a given blog by ID
 - List all blogs
 - Get a given blog post by ID
 - List all blog posts
 - Create a blog
 - Create a blog post

# Build and run the application

You need a recent JDK installed, to build the application just run

```bash
gradlew build
```

In the command prompt. To run the application just issue

```bash
gradlew bootRun
```

# Playground and Voyager

The application includes a [GraphQL playground](https://github.com/graphql/graphql-playground), available at `http://localhost:8080/playground` and you can see the exposed entities and relationships using [Voyager](https://github.com/APIs-guru/graphql-voyager) at `http://localhost:8080/voyager`

# Why Kotlin and JVM?

I had usually used GraphQL in dynamic languages like [Python](https://docs.graphene-python.org/projects/sqlalchemy/en/latest/), [Javascript](https://www.apollographql.com/docs/apollo-server/) or [Elixir](https://github.com/absinthe-graphql/absinthe) but I had never tried to use a strongly typed language.

Spring Boot is a "it has all you need" option and what I am most familiar with, and it has not one but two different libraries to support GraphQL. I tried both but [GraphQL kickstarter](https://github.com/graphql-java-kickstart) is probably the one with the best documentation.

I am really not a fan of Java as a language so every time I have to interact with the JVM I prefer to use [Kotlin](https://kotlinlang.org/), it is nicer and wrist friendly and 100% compatible with existing Java code.

Kotlin has its own very opinionated microservice framework, [Ktor](https://ktor.io), and I tried a small experiment with it and its very opinionated data ORM [Exposed](https://github.com/JetBrains/Exposed) and [KGraphQL](https://kgraphql.io/) but after just a few hours playing with it I discovered they don't like each other (Exposed and KGraphQL) so I decided to stick to Spring Boot.

# Tests

There are integration tests for the query and mutation operations, the tests live at `src/tests/kotlin` and to simplify the query and check of results I have created separated graphql queries and mutations (in the `resources/queries` and `resources/mutations` folders) and JSON files with the expected results.

The database is H2 in memory and initialized empty.

# Data access

Thanks to the support of JPA in Spring, changing data source to a different database will be matter of using the correct JDBC driver and setting its configuration in the `resources/application.properties` file.

If you want to support other databases or strategies not supported by JPA (for example, blog posts in text files), you will have to create your own separate repository with the basic operations.

# Tradeoffs

There is no direct link between the GraphQL engine and the JPA query engine (in reality JPA is using Hibernate as its ORM system), this means we will face a few limitations:

 - The generated queries are not optimized, for example, selecting _only_ the `name` field in a blog will issue a query for all its fields and not only the required field
 - In the same kind of problem, with related queries is possible to hit the [SELECT N+1](https://vladmihalcea.com/n-plus-1-query-problem/) problem
 - This problems could be potentially be fixed using a [DataFetcher](https://www.graphql-java.com/documentation/v11/data-fetching/) but this will require a lot more research and understand the internals of GraphQL Java kickstarter (it is somehow documented [here](https://www.graphql-java-kickstart.com/tools/))

For speed of implementation I decided this tradeoffs will work for this MVP and initial release

# Future improvements

With enough time I will probably try to see if there is a better way to connect the data fetchers so the query itself could be optimized depending on the field requested by the GraphQL query.

There is another problem with the way JPA sessions work, because of this handling [lazy loading](https://www.baeldung.com/hibernate-lazy-eager-loading) of properties is restricted (that is why there is no property named `posts`  in the `Blog` class), this could be solved with a custom Context class but for now it didn't worth the effort.

Another big improvement will be generating the queries and mutations automatically, we already know all the details about each entity, so it could be possible to generate the graphql schema on the fly with all the accepted queries and mutations (from the JPA repository). This will require deeper knowledge about how the GraphQL kickstart works and hence more time to develop, but I am confident it should be possible. This will bring a total new flexibility to the way the entities can be queried and modified (or created).
