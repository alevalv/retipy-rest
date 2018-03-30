package co.avaldes.retipy.domain.repository

import co.avaldes.retipy.domain.RetinalEvaluation
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface RetinalEvaluationRepository : PagingAndSortingRepository<RetinalEvaluation, Long>, JpaSpecificationExecutor<RetinalEvaluation>
