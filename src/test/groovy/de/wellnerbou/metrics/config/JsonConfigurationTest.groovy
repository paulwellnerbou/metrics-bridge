package de.wellnerbou.metrics.config

import de.wellnerbou.metrics.send.StdoutSender
import spock.lang.Specification

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
class JsonConfigurationTest extends Specification {
    def "test load"() {
        when:
        def configuration = new JsonConfiguration().load('/config.json')

        then:
        configuration != null
        configuration.bridges[0].source.name == "host-without-auth"
        configuration.bridges[0].source.url == "http://example.com/metrics"
        configuration.bridges[0].source.metrics == ["metric1", "metric2"]
        configuration.bridges[0].target."class" == StdoutSender.class.name
        configuration.bridges[1]."target-ref" == "stdout"
        configuration.targets."test-stdout"."class" == StdoutSender.class.name
    }
}
