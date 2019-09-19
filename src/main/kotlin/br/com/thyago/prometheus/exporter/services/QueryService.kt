package br.com.thyago.prometheus.exporter.services

import br.com.thyago.prometheus.exporter.data.GaugeQuery
import br.com.thyago.prometheus.exporter.data.PrometheusQuery
import br.com.thyago.prometheus.exporter.data.QueryResult
import io.micrometer.core.instrument.Gauge
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.sql.SQLException

@Service
class QueryService(@Autowired private val jdbcTemplate: JdbcTemplate) {

    private val log = LogManager.getLogger(this.javaClass)

    @Autowired
    lateinit var gauges: List<GaugeQuery>

    @Scheduled(fixedRateString = "\${query.interval.gauge}")
    fun queryGauge() {
        gauges.forEach(
            runQuery {
                query,
                res -> Gauge.builder(
                    query.monitoringQuery.name,
                    res.value,
                    { res.value.toDouble() }
                )
                .description(query.monitoringQuery.description)
                .tags(*res.labelValues.toTypedArray())
                .register(query.meterRegistry)
            }
        )
    }

    private fun <T : PrometheusQuery> runQuery(populateMetric: (T, QueryResult) -> Unit): (T) -> Unit {
        return { it ->
            log.info("Running Query: {}", it.monitoringQuery.query)
            log.info(
                "Creating data for {}: {} - {}",
                it.monitoringQuery.type,
                it.monitoringQuery.name,
                it.monitoringQuery.description
            )
            jdbcTemplate.query(it.monitoringQuery.query, mapRow(it)).forEach { res ->
                populateMetric(it, res)
            }
        }
    }

    private fun mapRow(prometheusQuery: PrometheusQuery): RowMapper<QueryResult> = RowMapper {
            rs: ResultSet, _ ->
        val labelValues = prometheusQuery.monitoringQuery.labels
            .map { columnLabel ->
                try {
                    rs.getString(columnLabel)
                } catch (e: SQLException) {
                    log.error("Failed to run query", e.errorCode)
                    ""
                }
            }
        QueryResult(labelValues, rs.getLong(prometheusQuery.monitoringQuery.labels.size + 1))
    }
}
