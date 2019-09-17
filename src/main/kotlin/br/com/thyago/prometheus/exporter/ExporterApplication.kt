package br.com.thyago.prometheus.exporter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExporterApplication

fun main(args: Array<String>) {
    runApplication<ExporterApplication>(*args)
}
