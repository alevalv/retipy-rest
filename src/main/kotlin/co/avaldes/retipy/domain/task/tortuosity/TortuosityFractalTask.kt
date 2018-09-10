package co.avaldes.retipy.domain.task.tortuosity

import co.avaldes.retipy.domain.evaluation.automated.RetipyEvaluation

class TortuosityFractalTask(
    retipyUri: String,
    retipyEvaluation: RetipyEvaluation
) : AbstractTortuosityTask(
    retipyUri,
    retipyEvaluation,
    "/tortuosity/fractal"
)
