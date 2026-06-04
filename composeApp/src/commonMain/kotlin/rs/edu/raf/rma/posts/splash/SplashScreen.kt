package rs.edu.raf.rma.posts.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import rma_10_kotlin_predavanje.composeapp.generated.resources.Res
import rma_10_kotlin_predavanje.composeapp.generated.resources.compose_multiplatform

@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF576db4),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AppAnimatedLogo()
        }
    }
}

@Composable
private fun AppAnimatedLogo() {
    val infiniteTransition = rememberInfiniteTransition()
    val pulsate by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 452f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
    )

    Image(
        modifier = Modifier.size(pulsate.dp),
        painter = painterResource(Res.drawable.compose_multiplatform),
        contentDescription = "Finlab Logo",
    )
}

