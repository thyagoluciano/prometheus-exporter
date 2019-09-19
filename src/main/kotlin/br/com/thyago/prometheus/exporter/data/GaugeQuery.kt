package br.com.thyago.prometheus.exporter.data

import io.micrometer.core.instrument.MeterRegistry

data class GaugeQuery(
    val meterRegistry: MeterRegistry,
    override val monitoringQuery: MonitoringQuery
): PrometheusQuery
