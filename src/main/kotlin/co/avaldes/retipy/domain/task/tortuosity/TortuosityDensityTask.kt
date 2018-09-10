package co.avaldes.retipy.domain.task.tortuosity

import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation

class TortuosityDensityTask(
    retipyUri: String,
    retipyEvaluation: RetipyEvaluation
) : AbstractTortuosityTask(
    retipyUri,
    retipyEvaluation,
    "/tortuosity/density"
)
