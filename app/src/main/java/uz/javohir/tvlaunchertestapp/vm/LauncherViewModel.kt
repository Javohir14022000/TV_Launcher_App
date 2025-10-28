package uz.javohir.tvlaunchertestapp.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.javohir.tvlaunchertestapp.model.AppInfo
import uz.javohir.tvlaunchertestapp.repository.AppRepository
import uz.javohir.tvlaunchertestapp.utils.AppCategory
import uz.javohir.tvlaunchertestapp.utils.SharedPreferencesManager

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application.applicationContext)

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(
        SharedPreferencesManager.getSelectedCategory(application.applicationContext)
    )
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _shouldLaunchLastApp = MutableStateFlow(false)
    val shouldLaunchLastApp: StateFlow<Boolean> = _shouldLaunchLastApp.asStateFlow()

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            _isLoading.value = true
            val installedApps = repository.getInstalledApps()
            _apps.value = installedApps
            _isLoading.value = false
        }
    }

    fun searchApps(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        SharedPreferencesManager.saveSelectedCategory(getApplication(), category)
    }

    fun getFilteredApps(): List<AppInfo> {
        val query = _searchQuery.value
        val category = _selectedCategory.value

        var filtered = _apps.value

        if (category != AppCategory.ALL.displayName) {
            filtered = filtered.filter {
                it.category.displayName == category
            }
        }

        if (query.isNotEmpty()) {
            filtered = filtered.filter {
                it.appName.contains(query, ignoreCase = true)
            }
        }

        return filtered
    }

    fun launchApp(packageName: String) {
        repository.launchApp(packageName)
    }

    fun checkAndLaunchLastApp(): String? {
        return repository.getLastOpenedApp()
    }

    fun getCategories(): List<String> {
        return AppCategory.values().map { it.displayName }
    }
}