package co.avaldes.retipy.domain.task

interface ITask<R> {
    fun execute(): R?
}
