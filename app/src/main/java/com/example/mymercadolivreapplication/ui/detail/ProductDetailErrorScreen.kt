package com.example.mymercadolivreapplication.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymercadolivreapplication.R
import com.example.mymercadolivreapplication.ui.theme.DarkGray
import com.example.mymercadolivreapplication.ui.theme.MyMercadoLivreApplicationTheme
import com.example.mymercadolivreapplication.ui.theme.Typography
import com.example.mymercadolivreapplication.ui.theme.YellowCustom

@Composable
fun ProductDetailErrorScreen(
    onBackToSearch: () -> Unit,
    onGoToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_not_found),
            contentDescription = "Não encontramos os detalhes deste produto.",
        )

        Text(
            text = "Não encontramos os detalhes deste produto.",
            style = Typography.titleMedium,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Pode ser que este item tenha sido removido ou esteja temporariamente indisponível.",
            style = Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onBackToSearch,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = YellowCustom,
                contentColor = DarkGray
            )
        ) {
            Text("Voltar para resultados da busca")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onGoToHome,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = YellowCustom,
                contentColor = DarkGray
            )
        ) {
            Text("Ir para tela inicial")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailErrorScreenPreview() {
    MyMercadoLivreApplicationTheme {
        ProductDetailErrorScreen(
            onBackToSearch = {},
            onGoToHome = {}
        )
    }
}