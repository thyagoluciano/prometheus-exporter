package br.com.thyago.prometheus.exporter.data

import com.fasterxml.jackson.annotation.JsonProperty

enum class MonitoringType {
    @JsonProperty("gauge")
    GAUGE,
    @JsonProperty("counter")
    COUNTER,
    @JsonProperty("summary")
    SUMMARY,
    @JsonProperty("histogram")
    HISTOGRAM
}
