package eu.kanade.presentation.more.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource

internal class ExtensionsStep(
    private val onNavigateToExtensions: () -> Unit,
) : OnboardingStep {

    override val isComplete: Boolean = true

    private val recommendedExtensions = listOf(
        ExtensionRecommendation(
            name = "Keiyoushi (Manga/Manhwa/Novel)",
            url = "https://raw.githubusercontent.com/keiyoushi/extensions/repo",
            description = "All-in-one source for manga, manhwa, and novels",
        ),
        ExtensionRecommendation(
            name = "Keiyoushi v2 (Latest)",
            url = "https://raw.githubusercontent.com/keiyoushi/extensions-source/repo",
            description = "Latest extension repository with faster updates",
        ),
    )

    @Composable
    override fun Content() {
        val clipboardManager = LocalClipboardManager.current

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
        ) {
            Text(
                text = stringResource(MR.strings.onboarding_extensions_title, stringResource(MR.strings.app_name)),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                stringResource(MR.strings.onboarding_extensions_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            Text(
                text = "Recommended Extension Stores",
                style = MaterialTheme.typography.titleMedium,
            )

            recommendedExtensions.forEach { extension ->
                ExtensionCard(
                    extension = extension,
                    onCopy = { clipboardManager.setText(AnnotatedString(extension.url)) },
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
            )

            Button(
                onClick = onNavigateToExtensions,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Go to Extensions Tab")
            }

            Text(
                text = "After adding the extension store URL, browse available extensions and install the ones you want. Sources will appear in the Browse tab.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    @Composable
    private fun ExtensionCard(
        extension: ExtensionRecommendation,
        onCopy: () -> Unit,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = extension.name,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = extension.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = onCopy) {
                        Text("📋")
                    }
                }
                Text(
                    text = extension.url,
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

private data class ExtensionRecommendation(
    val name: String,
    val url: String,
    val description: String,
)
