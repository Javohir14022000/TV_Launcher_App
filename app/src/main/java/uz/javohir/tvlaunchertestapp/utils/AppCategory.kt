package uz.javohir.tvlaunchertestapp.utils

enum class AppCategory(val displayName: String) {
    ALL("Barchasi"),
    GAMES("O'yinlar"),
    SOCIAL("Ijtimoiy tarmoqlar"),
    MEDIA("Media"),
    TOOLS("Asboblar"),
    SYSTEM("Tizim"),
    OTHER("Boshqalar");

    companion object {
        fun fromPackageName(packageName: String): AppCategory {
            return when {
                packageName.contains("game", ignoreCase = true) -> GAMES
                packageName.contains("social", ignoreCase = true) ||
                        packageName.contains("facebook", ignoreCase = true) ||
                        packageName.contains("instagram", ignoreCase = true) ||
                        packageName.contains("twitter", ignoreCase = true) ||
                        packageName.contains("telegram", ignoreCase = true) ||
                        packageName.contains("whatsapp", ignoreCase = true) -> SOCIAL
                packageName.contains("video", ignoreCase = true) ||
                        packageName.contains("music", ignoreCase = true) ||
                        packageName.contains("youtube", ignoreCase = true) ||
                        packageName.contains("spotify", ignoreCase = true) ||
                        packageName.contains("media", ignoreCase = true) -> MEDIA
                packageName.contains("tool", ignoreCase = true) ||
                        packageName.contains("util", ignoreCase = true) ||
                        packageName.contains("calculator", ignoreCase = true) ||
                        packageName.contains("calendar", ignoreCase = true) -> TOOLS
                else -> OTHER
            }
        }
    }
}