package co.avaldes.retipy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RetipyRestApplication

    fun main(args: Array<String>) {
        runApplication<RetipyRestApplication>(*args)
    }
