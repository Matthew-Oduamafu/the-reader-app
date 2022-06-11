package mattie.freelancer.reaader.navigation

enum class ReaderScreens {
    SPLASH_SCREEN,
    LOGIN_SCREEN,
    CREATE_ACCOUNT_SCREEN,
    READER_HOME_SCREEN,
    SEARCH_SCREEN,
    DETAILS_SCREEN,
    UPDATE_SCREEN,
    READER_STATS_SCREEN;

    companion object {
        fun fromRoute(route: String): ReaderScreens = when (route?.substringBefore("/")) {
            SPLASH_SCREEN.name -> SPLASH_SCREEN
            LOGIN_SCREEN.name -> LOGIN_SCREEN
            CREATE_ACCOUNT_SCREEN.name -> CREATE_ACCOUNT_SCREEN
            READER_HOME_SCREEN.name -> READER_HOME_SCREEN
            SEARCH_SCREEN.name -> SEARCH_SCREEN
            DETAILS_SCREEN.name -> DETAILS_SCREEN
            UPDATE_SCREEN.name -> UPDATE_SCREEN
            READER_STATS_SCREEN.name -> READER_STATS_SCREEN

            else -> throw IllegalArgumentException("Route $route not recognized")
        }
    }
}