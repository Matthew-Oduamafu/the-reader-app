package mattie.freelancer.reaader.screens.login

data class LoadingState(
    val status: Status, val message: String? = null
) {
    companion object{
        val IDLE = LoadingState(Status.IDLE)
        val FAILED = LoadingState(Status.FAILED)
        val SUCCESS = LoadingState(Status.SUCCESS)
        val LOADING = LoadingState(Status.LOADING)

    }
    enum class Status {
        IDLE,
        FAILED,
        SUCCESS,
        LOADING
    }
}
