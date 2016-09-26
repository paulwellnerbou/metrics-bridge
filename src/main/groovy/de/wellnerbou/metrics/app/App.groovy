package de.wellnerbou.metrics.app

import de.wellnerbou.metrics.collect.MetricCollectorJson
import de.wellnerbou.metrics.config.JsonConfiguration
import de.wellnerbou.metrics.send.Sender
import de.wellnerbou.metrics.send.SenderFactory
import de.wellnerbou.metrics.send.SingleInstanceSenderFactory
import de.wellnerbou.metrics.send.StdoutSender
import groovy.util.logging.Slf4j
import groovyx.gpars.GParsPool
import jsr166y.ForkJoinPool

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
@Slf4j
class App {

    public static final DEFAULT_INTERVAL = 30 * 1000 // 30 seconds
    public static final PREDEFINED_TARGETS = ["stdout": ["class": StdoutSender.class.getName()]]

    private config

    App(options) {
        createContext(options)
    }

    def createContext(options) {
        assert options != null
        assert options.c != null
        log.info "Loading config file: ${options.c}"
        println "Loading config file: ${options.c}"
        config = new JsonConfiguration().load(options.c)
        if(!config.targets) {
            config.targets = [:]
        }
        config.targets += PREDEFINED_TARGETS
        this.config.bridges.each { bridge ->
            bridge.collector = new MetricCollectorJson(addr: bridge.source.url)
            if(bridge."target-ref") {
                bridge.senderFactory = resolveSender(config, bridge."target-ref")
            } else {
                bridge.senderFactory = createSenderFactory(bridge.target."class" as String, bridge.target.params)
            }
            assert bridge.senderFactory != null
        }
    }

    def SenderFactory createSenderFactory(final String className, final params) {
        assert className instanceof String
        def clazz
        def senderFactory
        try {
            clazz = Class.forName(className)
        } catch (Exception e) {
            log.debug("No class found for $className: " + e.getMessage())
            throw e
        }
        assert clazz != null : "No class found for name " + className
        if(Sender.isAssignableFrom(clazz)) {
            senderFactory = new SingleInstanceSenderFactory(sender: clazz.newInstance() as Sender)
        } else if (SenderFactory.isAssignableFrom(clazz)) {
            senderFactory = clazz.newInstance() as SenderFactory
            if(params) {
                senderFactory.params = params;
            }
        } else {
            throw new RuntimeException("No valid Sender or SenderFactory: ${className} ($clazz)")
        }
        return senderFactory
    }

    def resolveSender(final config, final ref) {
        assert ref instanceof String
        def className = config.targets."$ref"."class"
        def params = config.targets."$ref".params
        assert className instanceof String
        return createSenderFactory(className, params)
    }

    def start() {
        assert config.bridges != null
        new Timer().scheduleAtFixedRate({
            log.trace "Collecting metrics from ${config.bridges}..."
            GParsPool.withPool (config.bridges.size) {
                config.bridges.eachParallel { bridge ->
                    try {
                        Map metrics = (bridge.collector as MetricCollectorJson).collectMetrics(bridge.source.metrics as String[])
                        long epoch = System.currentTimeMillis() / 1000
                        (bridge.senderFactory as SenderFactory).newSender().send(epoch, bridge.source.name as String, metrics)
                    } catch (Exception e) {
                        e.printStackTrace()
                    }
                }
            }
            log.debug "done."
        } as TimerTask, 0, DEFAULT_INTERVAL as Integer)
    }
}
