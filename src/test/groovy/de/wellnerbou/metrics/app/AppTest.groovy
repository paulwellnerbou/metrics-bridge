package de.wellnerbou.metrics.app

import de.wellnerbou.metrics.send.StdoutSender
import spock.lang.Specification

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
class AppTest extends Specification {

    def "test init context"() {
        when:
        def options = [c: CliAppTest.TEST_CONFIG, u: "paul@wellnerbou.de", t: "6cbd8939edeb9d89112751bcaeda752d6e7b163dbdac82daddd93fcbb8df724d"]
        def app = new App(options)

        then:
        app.sender != null

        when:
        app.sender = new StdoutSender()
        app.start()

        then:
        app != null
    }
}
