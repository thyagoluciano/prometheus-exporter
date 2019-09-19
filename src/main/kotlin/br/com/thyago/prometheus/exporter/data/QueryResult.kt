package br.com.thyago.prometheus.exporter.data

data class QueryResult(val labelValues: List<String>, val value: Long)
