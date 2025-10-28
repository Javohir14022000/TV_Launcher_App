package uz.javohir.tvlaunchertestapp.model

import android.graphics.drawable.Drawable
import uz.javohir.tvlaunchertestapp.utils.AppCategory

data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable?,
    val isSystemApp: Boolean = false,
    val category: AppCategory = AppCategory.OTHER
)

