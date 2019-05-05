package co.avaldes.retipy.domain.task

import co.avaldes.retipy.domain.evaluation.automated.IRetipyEvaluationService
import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation
import co.avaldes.retipy.domain.evaluation.automated.RetipyTask
import co.avaldes.retipy.domain.task.landmarks.ClassificationTask
import co.avaldes.retipy.domain.task.segmentation.SegmentationTask
import co.avaldes.retipy.domain.task.system.EmptyTask
import co.avaldes.retipy.domain.task.system.StatusTask
import co.avaldes.retipy.domain.task.tortuosity.TortuosityDensityTask
import co.avaldes.retipy.domain.task.tortuosity.TortuosityFractalTask
import co.avaldes.retipy.domain.task.vessels.VesselsClassificationTask
import co.avaldes.retipy.persistence.evaluation.retinal.RetipyEvaluationStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * Service to execute pending [RetipyEvaluation]. This will be executed every two minutes after an
 * execution is done.
 */
@Service
internal class ScheduledTaskRunnerService(
    private val retipyEvaluationService: IRetipyEvaluationService,
    @Value("\${retipy.python.backend.url}") private val retipyUri: String
) {
    private val logger: Logger = LoggerFactory.getLogger(ScheduledTaskRunnerService::class.java)
    private val statusTask: StatusTask = StatusTask(retipyUri)

    @Scheduled(fixedDelay = 120000) // wait 2 minutes to start again
    fun runPendingTasks() {
        if (statusTask.execute()) {
            val pendingEvaluations = retipyEvaluationService.getPendingEvaluations()
            // set all pending evaluations as running
            pendingEvaluations.forEach {
                it.status = RetipyEvaluationStatus.Running
                retipyEvaluationService.save(it)
            }
            val tasks = pendingEvaluations.map { createRetipyTask(it) }
            if (tasks.isNotEmpty()) {
                logger.info("Processing scheduled pending RetipyEvaluation tasks")
                val results = tasks.map {
                    it.execute() }
                results.forEach { if (it != null) retipyEvaluationService.save(it) }
            }
            // validate that no running evaluations exists after running, if there is, mark them as Error
            retipyEvaluationService
                .getEvaluationsByStatus(RetipyEvaluationStatus.Running)
                .forEach {
                    it.status = RetipyEvaluationStatus.Error
                    retipyEvaluationService.save(it)
                }
        } else {
            logger.debug("Retipy processing server '$retipyUri' is currently offline")
        }
    }

    private fun createRetipyTask(retipyEvaluation: RetipyEvaluation): ITask<RetipyEvaluation> =
        when (retipyEvaluation.name) {
            RetipyTask.TortuosityDensity -> TortuosityDensityTask(retipyUri, retipyEvaluation)
            RetipyTask.TortuosityFractal -> TortuosityFractalTask(retipyUri, retipyEvaluation)
            RetipyTask.LandmarksClassification -> ClassificationTask(retipyUri, retipyEvaluation)
            RetipyTask.Segmentation -> SegmentationTask(retipyUri, retipyEvaluation = retipyEvaluation)
            RetipyTask.VesselsClassification -> VesselsClassificationTask(
                    retipyUri,
                    retipyEvaluation = retipyEvaluation,
                    segmentedRetipyEvaluation = retipyEvaluationService.findByDiagnosticIdAndTask(
                            retipyEvaluation.diagnosticId, RetipyTask.Segmentation))
            else -> {
                EmptyTask()
            }
        }
}
