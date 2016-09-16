package de.wellnerbou.metrics.app

import spock.lang.Specification

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
class CliAppTest extends Specification {
    public static final String TEST_CONFIG = "src/test/resources/config.json"

    def "create App without params"() {
        when:
        def app = CliApp.createApp()

        then:
        app == null
    }

    def "create with params"() {
        when:
        def app = CliApp.createApp("-c", TEST_CONFIG)

        then:
        app != null
        app.config != null
    }

    def "create with longOpt params"() {
        when:
        def app = CliApp.createApp("--config=src/test/resources/config.json")

        then:
        app != null
        app.config != null
    }
}
