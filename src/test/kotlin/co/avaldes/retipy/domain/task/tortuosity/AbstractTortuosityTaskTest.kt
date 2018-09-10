package co.avaldes.retipy.domain.task.tortuosity

import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class AbstractTortuosityTaskTest
{
    internal class TortuosityTaskTest : AbstractTortuosityTask(
        "http://localhost:28591","/anotherUri", RetipyEvaluation(diagnosticId = 0L))
    private val tortuosityTask = TortuosityTaskTest()

    @BeforeEach
    fun setUp()
    {
    }

    @AfterEach
    fun tearDown()
    {
    }

    @Test
    fun execute()
    {
        val evaluated = tortuosityTask.execute()
        Assert.assertEquals("status does not match", RetipyEvaluationStatus.Error, evaluated.status)
    }
}
