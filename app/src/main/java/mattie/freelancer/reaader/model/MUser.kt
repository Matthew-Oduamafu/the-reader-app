package mattie.freelancer.reaader.model

data class MUser private constructor(
    var id: String?,
    var userId: String,
    var displayName: String,
    var avatarUrl: String,
    var quote: String,
    var profession: String


) {
    private fun toMapFunc(): MutableMap<String, Any> {
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "quote" to this.quote,
            "profession" to this.profession,
            "avatar_url" to this.avatarUrl
        )
    }


    companion object {
        @JvmStatic
        fun toMap(
            id: String?,
            userId: String,
            displayName: String,
            avatarUrl: String,
            quote: String,
            profession: String
        ): MutableMap<String, Any> =
            MUser(id, userId, displayName, avatarUrl, quote, profession).toMapFunc()
    }
}
