package br.com.agendou.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.agendou.domain.enums.Role
import br.com.agendou.ui.screens.auth.AuthNavigation
import br.com.agendou.ui.screens.bookings.BookingFormScreen
import br.com.agendou.ui.screens.home.ClientHomeScreen
import br.com.agendou.ui.screens.home.ProfessionalHomeScreen
import br.com.agendou.ui.screens.splash.SplashScreen
import br.com.agendou.ui.screens.service.ServiceManagementScreen
import br.com.agendou.ui.viewmodels.AuthViewModel
import br.com.agendou.ui.viewmodels.UserViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState(initial = false)
    val currentUser by userViewModel.currentUser.collectAsState(initial = null)

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
                        // Redireciona baseado no role do usuário
                        when (currentUser?.role) {
                            Role.CLIENT -> {
                                navController.navigate("client_home") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                            Role.PROFESSIONAL -> {
                                navController.navigate("professional_home") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                            else -> {
                                navController.navigate("auth") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
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
            AuthNavigation(
                onAuthSuccess = { user ->
                    when (user.role) {
                        Role.CLIENT -> {
                            navController.navigate("client_home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                        Role.PROFESSIONAL -> {
                            navController.navigate("professional_home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
        
        // Fluxo do Cliente
        composable("client_home") {
            ClientHomeScreen(
                onNavigateToBooking = { professionalId ->
                    navController.navigate("booking_form/$professionalId")
                },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = "booking_form/{professionalId}",
            arguments = listOf(navArgument("professionalId") { type = NavType.StringType })
        ) { backStackEntry ->
            val professionalId = backStackEntry.arguments?.getString("professionalId") ?: ""
            BookingFormScreen(
                professionalId = professionalId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onBookingSuccess = {
                    navController.popBackStack("client_home", inclusive = false)
                }
            )
        }
        
        // Fluxo do Profissional
        composable("professional_home") {
            ProfessionalHomeScreen(
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onManageServices = {
                    navController.navigate("manage_services")
                }
            )
        }

        composable("manage_services") {
            ServiceManagementScreen(
                professionalId = currentUser?.id ?: "",
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
