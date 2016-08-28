package de.wellnerbou.metrics.app

import com.librato.metrics.LibratoBatchBuilder
import de.wellnerbou.metrics.collect.MetricCollectorJson
import de.wellnerbou.metrics.config.JsonConfiguration
import groovyx.gpars.GParsPool

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
class App {

    public static final DEFAULT_INTERVAL = 10 * 1000
    private config
    private LibratoBatchBuilder batchBuilder;

    App(options) {
        createContext(options)
    }

    def createContext(options) {
        assert options != null
        assert options.c != null
        config = new JsonConfiguration().load(options.c)
        this.config.sources.each { it ->
            it.collector = new MetricCollectorJson(addr: it.url)
        }

        // assuming target=librato (no other implementation so far)
        batchBuilder = new LibratoBatchBuilder(options.u, options.t)
    }

    def start() {
        new Timer().schedule({
            GParsPool.withPool {
                config.sources.eachParallel { monitoringSource ->
                    try {
                        Map metrics = (monitoringSource.collector as MetricCollectorJson).collectMetrics(monitoringSource.metrics as String[])
                        def sender = new LibratoSender(libratoBatch: batchBuilder.build())
                        sender.send(monitoringSource.name, metrics)
                    } catch (Exception e) {
                        e.printStackTrace()
                    }
                }
            }
        } as TimerTask, 0, DEFAULT_INTERVAL as Integer)
    }
}
