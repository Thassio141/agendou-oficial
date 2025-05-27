package br.com.agendou.ui.navigation

import androidx.compose.ui.unit.dp
import br.com.agendou.ui.screens.splash.SplashScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.agendou.ui.screens.auth.AuthNavigation
import br.com.agendou.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.padding(0.dp)
    ) {
        composable("splash") {
            SplashScreen(
                navController = navController,
                onTimeout = {
                    if (isAuthenticated) {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("auth") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            )
        }
        
        composable("auth") {
            AuthNavigation()
        }
        
        composable("home") {
            // Sua tela principal aqui
            // HomeScreen()
        }
//        composable(
//            route = "booking/{proId}",
//            arguments = listOf(navArgument("proId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val proId = backStackEntry.arguments!!.getString("proId")!!
//            BookingScreen(proId = proId, onBack = { navController.popBackStack() })
//        }
//        composable(
//            route = "profile/{proId}",
//            arguments = listOf(navArgument("proId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val proId = backStackEntry.arguments!!.getString("proId")!!
//            ProfileScreen(proId = proId, onBack = { navController.popBackStack() })
//        }
    }
}
