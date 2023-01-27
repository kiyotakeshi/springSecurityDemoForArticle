package com.example.zenn.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author kiyota
 */
@RestController
@RequestMapping("/private")
class PrivateController {

    @GetMapping
    fun hello(): String {
        return "hello private world";
    }
}