package br.com.thyago.prometheus.exporter.config

import br.com.thyago.prometheus.exporter.data.MonitoringQuery
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.File


@SpringBootApplication(
    scanBasePackages = ["br.com.thyago"]
)
@EnableScheduling
class PrometheusExporterApplicationConfig {
    @Bean
    fun monitoringQueries(): List<MonitoringQuery> {
        val returnable = ArrayList<MonitoringQuery>()
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

        try {
            val files = getResourceFolderFiles("queries")

            files
                ?.filter { it.isFile }
                ?.mapTo(returnable) {
                    mapper.readValue(it)
                }
        } catch (e: Exception) {
            print(e)
        }

        return returnable
    }

    private fun getResourceFolderFiles(folder: String): Array<File>? {
        val loader = Thread.currentThread().contextClassLoader
        val url = loader.getResource(folder)
        val path = url!!.path
        return File(path).listFiles()
    }
}
