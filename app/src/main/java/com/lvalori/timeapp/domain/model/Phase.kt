package com.lvalori.timeapp.domain.model


enum class Phase(val label: String) {
    SCARNITURA("Scarnitura"),
    SBASSATURA("Sbassatura"),
    APPLICAZIONE("Applicazione"),
    CUCITURA("Cucitura"),
    CUSTOM("Personalizzata");

    companion object {
        fun getDefaultPhases() = listOf(
            SCARNITURA,
            SBASSATURA,
            APPLICAZIONE,
            CUCITURA
        )
    }
}