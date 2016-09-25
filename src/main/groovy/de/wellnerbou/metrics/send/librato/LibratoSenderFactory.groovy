package de.wellnerbou.metrics.send.librato

import com.librato.metrics.BatchResult
import com.librato.metrics.DefaultHttpPoster
import com.librato.metrics.LibratoBatch
import com.librato.metrics.PostResult
import de.wellnerbou.librato.metrics.LibratoBatchBuilder
import de.wellnerbou.metrics.send.Sender
import de.wellnerbou.metrics.send.SenderFactory
import de.wellnerbou.metrics.send.LogSender
import groovy.util.logging.Slf4j

@Slf4j
class LibratoSenderFactory implements SenderFactory {

    def params
    private LibratoBatchBuilder libratoBatchBuilder
    private DefaultHttpPoster httpPoster

    void setParams(params) {
        // see https://github.com/librato/librato-java/issues/32
        httpPoster = new DefaultHttpPoster(LibratoBatchBuilder.LIBRATO_METRICS_URL_V1, params.email, params.key)
        libratoBatchBuilder = new LibratoBatchBuilder(httpPoster)
    }

    @Override
    Sender newSender() {
        return new LibratoSender()
    }

    class LibratoSender implements Sender {
        private LogSender logSender = new LogSender()

        void send(long epoch, String source, Map<String, Number> metrics) {
            LibratoBatch libratoBatch = libratoBatchBuilder.build()
            logSender.send(epoch, source, metrics)

            metrics.each { entry ->
                libratoBatch.addGaugeMeasurement(entry.key, entry.value)
            }
            BatchResult result = libratoBatch.post(source, epoch)

            if (!result.success()) {
                for (PostResult post : result.getFailedPosts()) {
                    println "Could not POST to Librato: " + post
                }
            }
        }
    }
}
