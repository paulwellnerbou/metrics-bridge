package de.wellnerbou.metrics.send

class StdoutSender implements Sender {

    void send(long epoch, String source, Map<String, Number> metrics) {
        metrics.each { entry ->
            println source + ": time:" + epoch + ", " + entry
        }
    }
}
