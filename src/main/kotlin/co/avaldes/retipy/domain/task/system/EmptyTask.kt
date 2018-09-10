package co.avaldes.retipy.domain.task.system

import co.avaldes.retipy.domain.task.ITask
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class EmptyTask<R>: ITask<R>
{
    private val logger: Logger = LoggerFactory.getLogger(EmptyTask::class.java)

    override fun execute(): R?
    {
        logger.info("Empty task executed")
        return null
    }
}
