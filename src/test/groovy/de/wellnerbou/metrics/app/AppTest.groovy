package de.wellnerbou.metrics.app

import de.wellnerbou.metrics.collect.MetricCollectorJson
import de.wellnerbou.metrics.send.StdoutSender
import de.wellnerbou.metrics.send.librato.LibratoSenderFactory
import spock.lang.Specification

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
class AppTest extends Specification {

    def "test init context"() {
        when:
        def options = [c: CliAppTest.TEST_CONFIG]
        def app = new App(options)

        then:
        app.config.bridges[0].collector instanceof MetricCollectorJson
        app.config.bridges[1].collector instanceof MetricCollectorJson
        app.config.bridges[0].senderFactory.newSender() instanceof StdoutSender
        app.config.bridges[1].senderFactory.newSender() instanceof StdoutSender
        app.config.bridges[2].senderFactory.newSender() instanceof StdoutSender
    }

    def "test init context with librato sender"() {
        when:
        def options = [c: "src/test/resources/config-librato.json"]
        def app = new App(options)

        then:
        app.config.bridges[0].collector instanceof MetricCollectorJson
        app.config.bridges[0].senderFactory instanceof LibratoSenderFactory
    }
}
