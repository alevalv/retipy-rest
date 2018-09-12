package co.avaldes.retipy.domain

interface IStatusService
{
    fun setBackendStatus(status: Boolean)

    /**
     * Queries if the backend configured url is working.
     */
    fun backendStatus(): Boolean
}
