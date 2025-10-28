package uz.javohir.tvlaunchertestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import uz.javohir.tvlaunchertestapp.ui.screens.LauncherScreen
import uz.javohir.tvlaunchertestapp.ui.theme.TVLauncherTestAppTheme
import uz.javohir.tvlaunchertestapp.vm.LauncherViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lastAppPackage = viewModel.checkAndLaunchLastApp()

        if (lastAppPackage != null && !isTaskRoot) {

            viewModel.launchApp(lastAppPackage)

            finish()
            return
        }

        setContent {
            TVLauncherTestAppTheme {
                LauncherScreen(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadApps()
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TVLauncherTestAppTheme {
        Greeting("Android")
    }
}