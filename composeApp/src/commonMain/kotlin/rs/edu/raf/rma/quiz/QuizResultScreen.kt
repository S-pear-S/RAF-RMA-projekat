package rs.edu.raf.rma.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun QuizResultScreen(
    score: Double,
    correctAnswers: Int,
    totalQuestions: Int,
    timeUsedSeconds: Int,
    onPlayAgain: () -> Unit,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Rezultat",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = String.format("%.2f", score),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            text = "/ 100",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(32.dp))

        ResultRow(label = "Tačnih odgovora", value = "$correctAnswers / $totalQuestions")
        ResultRow(label = "Netačnih odgovora", value = "${totalQuestions - correctAnswers} / $totalQuestions")
        ResultRow(
            label = "Iskorišćeno vreme",
            value = "${timeUsedSeconds}s / 60s",
        )
        ResultRow(
            label = "Preostalo vreme",
            value = "${60 - timeUsedSeconds}s",
        )

        Spacer(Modifier.height(48.dp))

        Button(onClick = onPlayAgain) {
            Text("Igraj ponovo")
        }

        Spacer(Modifier.height(12.dp))

        androidx.compose.material3.OutlinedButton(onClick = onBack) {
            Text("Nazad")
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
    Spacer(Modifier.height(8.dp))
}
