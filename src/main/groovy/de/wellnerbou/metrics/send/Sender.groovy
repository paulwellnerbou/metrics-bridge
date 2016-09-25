package de.wellnerbou.metrics.send

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
interface Sender {
    void send(long epoch, String source, Map<String, Number> metrics)
}
