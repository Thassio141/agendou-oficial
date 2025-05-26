package br.com.agendou.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.agendou.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    delayMillis: Long = 2500L,
    onTimeout: () -> Unit = {
        navController.navigate("auth") {
            popUpTo("splash") { inclusive = true }
        }
    }
) {
    // Após o delay, dispara a navegação
    LaunchedEffect(Unit) {
        delay(delayMillis)
        onTimeout()
    }

    // Background degradê semelhante à imagem
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0A2535), Color(0xFF13425A)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logotipo branco
            Image(
                painter = painterResource(id = R.drawable.logotipo_agendou_branco_png),
                contentDescription = "Logo Agendou",
                modifier = Modifier
                    .size(480.dp)
            )
            Spacer(Modifier.height(24.dp))
            // Animação de carregamento
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
