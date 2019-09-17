package br.com.thyago.prometheus.exporter

import br.com.thyago.prometheus.exporter.config.PrometheusExporterApplicationConfig
import org.apache.logging.log4j.LogManager
import org.springframework.boot.SpringApplication
import java.net.InetAddress


object PrometheusExporterApplication {
    private val logger = LogManager.getLogger(this.javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        val app = SpringApplication.run(
            PrometheusExporterApplicationConfig::class.java, *args
        )

        val applicationName = app.environment.getProperty("spring.application.name")
        val contextPath = app.environment.getProperty("server.servlet.context-path")
        val port = app.environment.getProperty("server.port")
        val hostAddress = InetAddress.getLocalHost().hostAddress

        logger.info(
            """|
                   |------------------------------------------------------------
                   |Application '$applicationName' is running! Access URLs:
                   |   Local:      http://127.0.0.1:$port$contextPath
                   |   External:   http://$hostAddress:$port$contextPath
                   |------------------------------------------------------------""".trimMargin()
        )

    }
}
