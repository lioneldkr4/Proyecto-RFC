package com.example.rfcmultiplataforma

import kotlinx.datetime.LocalDate

class RFCGenerator {

    fun generarRFC(
        nombres: String,
        apellidoPaterno: String,
        apellidoMaterno: String?,
        fechaNacimiento: LocalDate
    ): String {

        val nombreNormalizado = normalizarTexto(nombres)
        val paternoNormalizado = normalizarTexto(apellidoPaterno)
        val maternoNormalizado = normalizarTexto(apellidoMaterno ?: "")

        val nombreValido = obtenerNombreValido(nombreNormalizado)

        val iniciales = construirIniciales(
            paternoNormalizado,
            maternoNormalizado,
            nombreValido
        )

        val fechaFormateada = formatearFecha(fechaNacimiento)

        val rfcParcial = iniciales + fechaFormateada

        return filtrarPalabrasInconvenientes(rfcParcial)
    }

    // ---------------------------
    // Normaliza texto (mayúsculas y sin acentos)
    // ---------------------------
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

    // ---------------------------
    // Ignora JOSE y MARIA si están al inicio
    // ---------------------------
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

    // ---------------------------
    // Obtiene primera vocal interna del apellido paterno
    // ---------------------------
    private fun primeraVocalInterna(texto: String): Char {
        val vocales = listOf('A', 'E', 'I', 'O', 'U')

        for (i in 1 until texto.length) {
            if (texto[i] in vocales) {
                return texto[i]
            }
        }

        return 'X'
    }

    // ---------------------------
    // Formatea fecha en YYMMDD
    // ---------------------------
    private fun formatearFecha(fecha: LocalDate): String {
        val year = fecha.year.toString().takeLast(2)
        val month = fecha.monthNumber.toString().padStart(2, '0')
        val day = fecha.dayOfMonth.toString().padStart(2, '0')

        return "$year$month$day"
    }

    // ---------------------------
    // Filtro básico de palabras inconvenientes
    // (lista reducida de ejemplo)
    // ---------------------------
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
                .replaceRange(1, 2, "X") + rfc.drop(4)
        }

        return rfc
    }
}
