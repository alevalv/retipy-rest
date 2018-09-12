package co.avaldes.retipy.domain

import org.springframework.stereotype.Component

@Component
internal class StatusService : IStatusService
{
    var backendOnline: Boolean = false

    override fun setBackendStatus(status: Boolean)
    {
        backendOnline = status
    }

    override fun backendStatus(): Boolean = backendOnline
}
