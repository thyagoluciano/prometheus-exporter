package br.com.thyago.prometheus.exporter.data

interface PrometheusQuery {
    val monitoringQuery: MonitoringQuery
}
