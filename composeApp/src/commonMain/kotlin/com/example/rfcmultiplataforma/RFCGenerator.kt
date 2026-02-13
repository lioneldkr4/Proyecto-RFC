package com.example.rfcmultiplataforma

import kotlinx.datetime.LocalDate
import kotlin.random.Random


class RFCGenerator {


    private fun normalizarTexto(texto: String): String {
        return texto
            .uppercase()
            .replace("Á", "A")
            .replace("É", "E")
            .replace("Í", "I")
            .replace("Ó", "O")
            .replace("Ú", "U")
            .replace("Ü", "U")
            .replace("Ñ", "N")
            .trim()
    }


    private fun obtenerNombreValido(nombres: String): String {
        val lista = nombres.split(" ").filter { it.isNotBlank() }

        if (lista.isEmpty()) return ""

        return when (lista.first()) {
            "JOSE", "MARIA" ->
                if (lista.size > 1) lista[1] else lista.first()
            else -> lista.first()
        }
    }

    // ---------------------------
    // Construye las 4 letras iniciales
    // ---------------------------
    private fun construirIniciales(
        paterno: String,
        materno: String,
        nombre: String
    ): String {

        val primeraLetraPaterno = paterno.firstOrNull() ?: 'X'
        val primeraVocalPaterno = primeraVocalInterna(paterno)
        val primeraLetraMaterno = materno.firstOrNull() ?: 'X'
        val primeraLetraNombre = nombre.firstOrNull() ?: 'X'

        return buildString {
            append(primeraLetraPaterno)
            append(primeraVocalPaterno)
            append(primeraLetraMaterno)
            append(primeraLetraNombre)
        }
    }

    private fun primeraVocalInterna(texto: String): Char {
        val vocales = listOf('A', 'E', 'I', 'O', 'U')

        for (i in 1 until texto.length) {
            if (texto[i] in vocales) {
                return texto[i]
            }
        }

        return 'X'
    }


    fun generarRFCParcial(
        nombres: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        anio: String,
        mes: String,
        dia: String
    ): String {

        val nombreNormalizado = normalizarTexto(nombres)
        val paternoNormalizado = normalizarTexto(apellidoPaterno)
        val maternoNormalizado = normalizarTexto(apellidoMaterno)

        val nombreValido = obtenerNombreValido(nombreNormalizado)

        val iniciales = construirIniciales(
            paternoNormalizado,
            maternoNormalizado,
            nombreValido
        )

        // Construcción progresiva de fecha
        var fechaConstruida = ""

        if (anio.length == 4) {
            fechaConstruida += anio.takeLast(2)
        }

        if (mes.length == 2) {
            fechaConstruida += mes
        }

        if (dia.length == 2) {
            fechaConstruida += dia
        }


        val fechaCompleta = anio.length == 4 && mes.length == 2 && dia.length == 2

        if (fechaCompleta && !fechaEsValida(anio, mes, dia)) {
            return "Fecha inválida"
        }


        val fechaParcial = construirFechaParcial(anio, mes, dia)

        val rfcParcial = iniciales + fechaParcial

        if (fechaCompleta) {
            return filtrarPalabrasInconvenientes(rfcParcial) + generarHomoclave()
        }

        return filtrarPalabrasInconvenientes(rfcParcial)
    }

    private fun construirFechaParcial(
        anio: String,
        mes: String,
        dia: String
    ): String {

        return buildString {
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
    }

    private fun fechaEsValida(
        anio: String,
        mes: String,
        dia: String
    ): Boolean {

        if (anio.length != 4 || mes.length != 2 || dia.length != 2) {
            return false
        }

        return try {
            LocalDate(
                year = anio.toInt(),
                monthNumber = mes.toInt(),
                dayOfMonth = dia.toInt()
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun generarHomoclave(): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..3)
            .map { caracteres.random() }
            .joinToString("")
    }

    private fun filtrarPalabrasInconvenientes(rfc: String): String {

        val palabrasInconvenientes = setOf(
            "BUEI", "BUEY", "CACA", "CAGA", "CAGO", "CAKA",
            "COGE", "COGI", "COJA", "COJE", "COJO", "CULO",
            "FETO", "GUEY", "JOTO", "KACA", "KAGA", "KOGE",
            "KOJO", "KAKA", "KULO", "MAME", "MAMO", "MEAR",
            "MIAR", "MEON", "MION", "MOCO", "MULA", "PEDA",
            "PEDO", "PENE", "PITO", "PUTA", "PUTO", "QULO"
        )

        val primerasCuatro = rfc.take(4)

        if (primerasCuatro in palabrasInconvenientes) {
            return primerasCuatro
                .replaceRange(3, 4, "X") + rfc.drop(4)
        }

        return rfc
    }
}
