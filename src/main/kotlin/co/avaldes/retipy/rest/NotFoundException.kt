package co.avaldes.retipy.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
internal class NotFoundException(message: String): RuntimeException(message)