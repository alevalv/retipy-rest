package co.avaldes.retipy.domain

import co.avaldes.retipy.domain.diagnostic.Diagnostic
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface DiagnosticService : PagingAndSortingRepository<Diagnostic, Long>, JpaSpecificationExecutor<Diagnostic>
