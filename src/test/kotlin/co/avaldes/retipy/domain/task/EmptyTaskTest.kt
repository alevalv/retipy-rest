package co.avaldes.retipy.domain.task

import co.avaldes.retipy.domain.task.system.EmptyTask
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class EmptyTaskTest {
    private val emptyTask = EmptyTask<Any>()

    @Test
    fun execute() {
        assertNull(emptyTask.execute(), "execute should return null")
    }
}
