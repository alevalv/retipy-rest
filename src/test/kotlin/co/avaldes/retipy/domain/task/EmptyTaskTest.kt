package co.avaldes.retipy.domain.task

import co.avaldes.retipy.domain.task.system.EmptyTask
import org.junit.jupiter.api.Test

internal class EmptyTaskTest
{
    private val emptyTask = EmptyTask<Any>()

    @Test
    fun execute()
    {
        emptyTask.execute() // nothing should happen
    }
}
