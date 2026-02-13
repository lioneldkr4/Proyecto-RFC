package com.example.rfcmultiplataforma

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.key.*

import kotlinx.datetime.LocalDate

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

    // Estados reactivos
    var nombres by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val nombreFocus = remember { FocusRequester() }
    val paternoFocus = remember { FocusRequester() }
    val maternoFocus = remember { FocusRequester() }
    val anioFocus = remember { FocusRequester() }
    val mesFocus = remember { FocusRequester() }
    val diaFocus = remember { FocusRequester() }


    // RFC derivado automáticamente del estado
    val rfc by remember {
        derivedStateOf {

            if (nombres.isBlank() || apellidoPaterno.isBlank()) {
                return@derivedStateOf ""
            }

            val fechaParcial = buildString {
                if (anio.length >= 2) {
                    append(anio.takeLast(2))
                }
                if (mes.isNotBlank()) {
                    append(mes)
                }
                if (dia.isNotBlank()) {
                    append(dia)
                }
            }

            generator.generarRFCParcial(
                nombres = nombres,
                apellidoPaterno = apellidoPaterno,
                apellidoMaterno = apellidoMaterno,
                fechaParcial = fechaParcial
            )

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
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nombreFocus)
                .onPreviewKeyEvent {
                    if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Next)
                        true
                    } else {
                        false
                    }
                }
        )


        OutlinedTextField(
            value = apellidoPaterno,
            onValueChange = { apellidoPaterno = it },
            label = { Text("Apellido Paterno") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(paternoFocus)
                .onPreviewKeyEvent {
                    if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Next)
                        true
                    } else {
                        false
                    }
                }
        )

        OutlinedTextField(
            value = apellidoMaterno,
            onValueChange = { apellidoMaterno = it },
            label = { Text("Apellido Materno") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(maternoFocus)
                .onPreviewKeyEvent {
                    if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(FocusDirection.Next)
                        true
                    } else {
                        false
                    }
                }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = anio,
                onValueChange = {
                    anio = it.filter { c -> c.isDigit() }.take(4)
                },
                label = { Text("Año (YYYY)") },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(anioFocus)
                    .onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
                            if (it.isShiftPressed) {
                                focusManager.moveFocus(FocusDirection.Previous)
                            } else {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                            true
                        } else {
                            false
                        }
                    }
            )


            OutlinedTextField(
                value = mes,
                onValueChange = {
                    mes = it.filter { c -> c.isDigit() }.take(2)
                },
                label = { Text("Mes (MM)") },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(mesFocus)
                    .onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
                            if (it.isShiftPressed) {
                                focusManager.moveFocus(FocusDirection.Previous)
                            } else {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                            true
                        } else {
                            false
                        }
                    }
            )


            OutlinedTextField(
                value = dia,
                onValueChange = {
                    dia = it.filter { c -> c.isDigit() }.take(2)
                },
                label = { Text("Día (DD)") },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(diaFocus)
                    .onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
                            if (it.isShiftPressed) {
                                focusManager.moveFocus(FocusDirection.Previous)
                            } else {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                            true
                        } else {
                            false
                        }
                    }
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
