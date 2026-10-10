package eu.kanade.presentation.more.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.kanade.tachiyomi.util.system.toast
import kotlinx.coroutines.launch
import mihon.domain.extension.repository.ExtensionStoreRepository
import tachiyomi.presentation.core.components.material.padding
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

internal class ExtensionsStep : OnboardingStep {

    override val isComplete: Boolean = true

    private val repository: ExtensionStoreRepository = Injekt.get()
    private var isRepoAdded by mutableStateOf(false)

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            val stores = repository.getAll()
            if (stores.isEmpty()) {
                repository.insertFromPreference(
                    indexUrl = KEIYOUSHI_REPO_URL,
                    name = "Keiyoushi",
                )
                repository.refreshAll()
            }
            isRepoAdded = true
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Extension,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Extension Repository",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Text(
                text = "Extensions provide access to hundreds of manga, manhwa, webtoon, and novel sources. ytclip-hub is pre-configured with the official Keiyoushi repository.",
                style = MaterialTheme.typography.bodyMedium,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Keiyoushi Repository",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "https://raw.githubusercontent.com/keiyoushi/extensions/repo",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (isRepoAdded) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            repository.refreshAll()
                            context.toast("Repository updated")
                        }
                    },
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    Text(
                        text = "Keiyoushi Configured",
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            repository.insertFromPreference(
                                indexUrl = KEIYOUSHI_REPO_URL,
                                name = "Keiyoushi",
                            )
                            repository.refreshAll()
                            isRepoAdded = true
                            context.toast("Keiyoushi repository enabled")
                        }
                    },
                ) {
                    Text("Enable Keiyoushi Repository")
                }
            }
        }
    }

    companion object {
        const val KEIYOUSHI_REPO_URL = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/index.min.json"
    }
}
