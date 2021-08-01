package com.cprieto.exercises

import io.micrometer.core.instrument.util.IOUtils
import org.springframework.core.io.ClassPathResource
import java.nio.charset.StandardCharsets

fun String.test(path: String = "queries") = "${path}/$this.graphql"
fun String.response(path: String = "queries"): String = IOUtils.toString(ClassPathResource("${path}/$this.json").inputStream, StandardCharsets.UTF_8)
