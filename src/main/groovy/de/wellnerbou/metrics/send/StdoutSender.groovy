package de.wellnerbou.metrics.send

class StdoutSender implements Sender {

    void send(String source, Map<String, Number> metrics) {
        long epoch = System.currentTimeMillis() / 1000
        metrics.each { entry ->
            println source + ": time:" + epoch + ", " + entry
        }
    }
}
