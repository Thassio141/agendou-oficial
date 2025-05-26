package br.com.agendou.ui.navigation

import androidx.compose.ui.unit.dp
import br.com.agendou.ui.screens.splash.SplashScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph() {
    // Cria o NavController
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.padding(0.dp)
    ) {
        composable("splash") {
            // Chama sua SplashScreen
            SplashScreen(navController)
        }
//        composable("auth") {
//            AuthScreen(onSuccess = { navController.navigate("home") {
//                popUpTo("auth") { inclusive = true }
//            }})
//        }
//        composable("home") {
//            HomeScreen(onSelectPro = { proId ->
//                navController.navigate("booking/$proId")
//            })
//        }
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
