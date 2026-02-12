package com.example.rfcmultiplataforma

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource

import rfcmultiplataforma.composeapp.generated.resources.Res
import rfcmultiplataforma.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            RFCScreen()
        }
    }
}

@Composable
fun RFCScreen() {

    val generator = remember { RFCGenerator() }

    var nombres by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }

    val rfc = remember(nombres, apellidoPaterno, apellidoMaterno, anio, mes, dia) {
        try {
            if (anio.length == 4 && mes.length == 2 && dia.length == 2) {
                val fecha = LocalDate(
                    anio.toInt(),
                    mes.toInt(),
                    dia.toInt()
                )
                generator.generarRFC(
                    nombres,
                    apellidoPaterno,
                    apellidoMaterno,
                    fecha
                )
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Generador de RFC",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = nombres,
            onValueChange = { nombres = it },
            label = { Text("Nombre(s)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidoPaterno,
            onValueChange = { apellidoPaterno = it },
            label = { Text("Apellido Paterno") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidoMaterno,
            onValueChange = { apellidoMaterno = it },
            label = { Text("Apellido Materno") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = anio,
                onValueChange = { anio = it.filter { c -> c.isDigit() }.take(4) },
                label = { Text("Año (YYYY)") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = mes,
                onValueChange = { mes = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("Mes (MM)") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = dia,
                onValueChange = { dia = it.filter { c -> c.isDigit() }.take(2) },
                label = { Text("Día (DD)") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "RFC Generado:",
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = rfc,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}