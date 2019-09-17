package br.com.thyago.prometheus.exporter.data

data class MonitoringQuery(
    val type: MonitoringType,
    val name: String,
    val labels: List<String>,
    val query: String,
    val description: String
)
