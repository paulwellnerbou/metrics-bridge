package de.wellnerbou.metrics.collect

import spock.lang.Specification

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
class MetricCollectorJsonTest extends Specification {

    def "Collect from local file"() {
        when:
        def metricCollectorJson = new MetricCollectorJson(
                addr: "src/test/resources/mem.json"
        )

        def result = metricCollectorJson.collectMetrics('mem')
        println result

        then:
        result.mem == 1600360
    }

    def "Collect from local file, two metrics"() {
        when:
        def metricCollectorJson = new MetricCollectorJson(
                addr: "src/test/resources/mem.json"
        )

        def result = metricCollectorJson.collectMetrics('mem', 'mem.free')
        println result

        then:
        result.mem == 1600360
        result."mem.free" == 1600350
    }

    def "test creation with addr and auth separately"() {
        when:
        def metricCollectorJson = new MetricCollectorJson(
                addr: "http://example.com/metrics",
                auth: "user:pass"
        )

        then:
        metricCollectorJson.auth == "user:pass"
        metricCollectorJson.addr == "http://example.com/metrics"
    }

    def "test creation with addr and auth in url"() {
        when:
        def metricCollectorJson = new MetricCollectorJson(
                addr: "http://user:pass@example.com/metrics"
        )

        then:
        metricCollectorJson.auth == "user:pass"
        metricCollectorJson.addr == "http://example.com/metrics"
    }
}
