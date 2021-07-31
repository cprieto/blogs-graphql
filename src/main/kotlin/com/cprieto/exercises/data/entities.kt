package com.cprieto.exercises.data

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Blog(
    @Id @GeneratedValue
    var id: Long = 0,
    var name: String,
    var slug: String
)

@Entity
data class Post(
    @Id @GeneratedValue
    var id: Long = 0,
    var title: String? = null,
    var content: String,
    var viewCount: Int = 0,
    var blogId: Long
)