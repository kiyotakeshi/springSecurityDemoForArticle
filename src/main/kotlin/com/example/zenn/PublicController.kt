package com.example.zenn

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author kiyota
 */
@RestController
@RequestMapping("/public")
class PublicController {

    @GetMapping
    fun hello(): String {
        return "hello public world";
    }
}