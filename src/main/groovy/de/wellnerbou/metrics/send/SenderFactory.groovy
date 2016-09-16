package de.wellnerbou.metrics.send

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
interface SenderFactory {
    Sender newSender();
}
