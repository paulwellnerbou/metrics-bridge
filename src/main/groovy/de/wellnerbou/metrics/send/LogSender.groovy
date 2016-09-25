package de.wellnerbou.metrics.send

import groovy.util.logging.Slf4j

@Slf4j
class LogSender implements Sender {

    void send(long epoch, String source, Map<String, Number> metrics) {
        metrics.each { entry ->
            log.info(source + ": time:" + epoch + ", " + entry)
        }
    }
}
