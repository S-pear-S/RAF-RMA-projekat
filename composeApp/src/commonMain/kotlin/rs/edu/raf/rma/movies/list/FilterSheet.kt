package rs.edu.raf.rma.movies.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rs.edu.raf.rma.movies.domain.Genre
import rs.edu.raf.rma.movies.domain.MovieFilter

private val SORT_OPTIONS = listOf(
    "imdb_rating" to "IMDb Rejting",
    "year" to "Godina",
    "title" to "Naziv",
    "imdb_votes" to "Broj glasova",
    "tmdb_rating" to "TMDB Rejting",
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterSheet(
    currentFilter: MovieFilter,
    genres: List<Genre>,
    onApply: (MovieFilter) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedGenreId by remember { mutableStateOf(currentFilter.genreId) }
    var minYear by remember { mutableStateOf(currentFilter.minYear?.toString() ?: "") }
    var maxYear by remember { mutableStateOf(currentFilter.maxYear?.toString() ?: "") }
    var minRating by remember { mutableStateOf(currentFilter.minRating?.toString() ?: "") }
    var sortBy by remember { mutableStateOf(currentFilter.sortBy ?: "imdb_rating") }
    var sortOrder by remember { mutableStateOf(currentFilter.sortOrder ?: "desc") }
    var sortExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Filteri", style = MaterialTheme.typography.titleMedium)

        if (genres.isNotEmpty()) {
            Text("Žanr", style = MaterialTheme.typography.labelLarge)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                genres.forEach { genre ->
                    FilterChip(
                        selected = selectedGenreId == genre.id,
                        onClick = {
                            selectedGenreId = if (selectedGenreId == genre.id) null else genre.id
                        },
                        label = { Text(genre.name) },
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = minYear,
                onValueChange = { minYear = it.filter { c -> c.isDigit() } },
                label = { Text("Min godina") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = maxYear,
                onValueChange = { maxYear = it.filter { c -> c.isDigit() } },
                label = { Text("Max godina") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
        }

        OutlinedTextField(
            value = minRating,
            onValueChange = { minRating = it },
            label = { Text("Min rejting (0-10)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        ExposedDropdownMenuBox(
            expanded = sortExpanded,
            onExpandedChange = { sortExpanded = it },
        ) {
            OutlinedTextField(
                value = SORT_OPTIONS.find { it.first == sortBy }?.second ?: sortBy,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sortiraj po") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(sortExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
            )
            ExposedDropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                SORT_OPTIONS.forEach { (value, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = { sortBy = value; sortExpanded = false },
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { sortOrder = if (sortOrder == "asc") "desc" else "asc" },
                modifier = Modifier.weight(1f),
            ) {
                Text(if (sortOrder == "asc") "Rastuće ↑" else "Opadajuće ↓")
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = {
                    selectedGenreId = null
                    minYear = ""
                    maxYear = ""
                    minRating = ""
                    sortBy = "imdb_rating"
                    sortOrder = "desc"
                    onApply(MovieFilter())
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("Obriši sve")
            }
            Button(
                onClick = {
                    onApply(
                        currentFilter.copy(
                            genreId = selectedGenreId,
                            minYear = minYear.toIntOrNull(),
                            maxYear = maxYear.toIntOrNull(),
                            minRating = minRating.toDoubleOrNull(),
                            sortBy = sortBy,
                            sortOrder = sortOrder,
                        )
                    )
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("Primeni")
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}
