package co.avaldes.retipy.persistence.repository

import co.avaldes.retipy.persistence.RetinalEvaluationBean
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface RetinalEvaluationRepository : PagingAndSortingRepository<RetinalEvaluationBean, Long>, JpaSpecificationExecutor<RetinalEvaluationBean>
