package uz.javohir.tvlaunchertestapp.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.javohir.tvlaunchertestapp.model.AppInfo
import uz.javohir.tvlaunchertestapp.utils.AppCategory
import uz.javohir.tvlaunchertestapp.utils.SharedPreferencesManager

class AppRepository(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {

        val allApps = mutableSetOf<AppInfo>()

        try {
            val launcherApps = getLauncherApps()
            allApps.addAll(launcherApps)

            // TV ilovalarini ham qo'shish uchun
            // val tvApps = getTVApps()
            // allApps.addAll(tvApps)

        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }

        allApps.toList().sortedBy { it.appName.lowercase() }
    }

    private fun getLauncherApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return queryAndMapApps(intent, "LAUNCHER")
    }

    private fun getTVApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)
        }

        return queryAndMapApps(intent, "TV")
    }

    private fun queryAndMapApps(intent: Intent, category: String): List<AppInfo> {
        return try {
            val resolveInfos = packageManager.queryIntentActivities(intent, 0)

            resolveInfos.mapNotNull { resolveInfo ->
                try {
                    createAppInfo(resolveInfo)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun createAppInfo(resolveInfo: ResolveInfo): AppInfo {
        val appInfo = resolveInfo.activityInfo.applicationInfo
        val packageName = resolveInfo.activityInfo.packageName
        val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

        return AppInfo(
            appName = resolveInfo.loadLabel(packageManager).toString(),
            packageName = packageName,
            icon = resolveInfo.loadIcon(packageManager),
            isSystemApp = isSystem,
            category = if (isSystem) AppCategory.SYSTEM else AppCategory.fromPackageName(packageName)
        )
    }

    fun launchApp(packageName: String): Boolean {
        return try {
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)

                SharedPreferencesManager.saveLastOpenedApp(context, packageName)

                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getLastOpenedApp(): String? {
        return SharedPreferencesManager.getLastOpenedApp(context)
    }
}