package de.wellnerbou.metrics.config

import spock.lang.Specification

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
class JsonConfigurationTest extends Specification {
    def "test load"() {
        when:
        JsonConfiguration jsonConfiguration = new JsonConfiguration()
        def configuration = jsonConfiguration.load()

        then:
        configuration != null
        configuration.sources[0].name == "name"
        configuration.sources[0].url == "url"
        configuration.sources[0].metrics == ["mem", "mem.free"]
    }
}
