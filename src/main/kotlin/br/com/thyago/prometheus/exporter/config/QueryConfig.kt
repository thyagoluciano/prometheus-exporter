package br.com.thyago.prometheus.exporter.config

import br.com.thyago.prometheus.exporter.data.GaugeQuery
import br.com.thyago.prometheus.exporter.data.MonitoringQuery
import br.com.thyago.prometheus.exporter.data.MonitoringType
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class QueryConfig(
    @Autowired private val monitoringQueries: List<MonitoringQuery>,
    @Autowired private val meterRegistry: MeterRegistry
) {
    @Bean
    fun gauges(): List<GaugeQuery> = monitoringQueries
        .filter { it.type === MonitoringType.GAUGE }
        .map {
            GaugeQuery(meterRegistry, it)
        }
}
