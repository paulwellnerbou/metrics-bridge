package de.wellnerbou.metrics.send

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
interface Sender {
    void send(String source, Map<String, Number> metrics)
}
