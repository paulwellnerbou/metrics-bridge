package de.wellnerbou.metrics.send.librato

import com.librato.metrics.BatchResult
import com.librato.metrics.LibratoBatch
import com.librato.metrics.PostResult
import de.wellnerbou.librato.metrics.LibratoBatchBuilder
import de.wellnerbou.metrics.send.Sender
import de.wellnerbou.metrics.send.SenderFactory
import de.wellnerbou.metrics.send.StdoutSender

class LibratoSenderFactory implements SenderFactory {

    def params
    private LibratoBatchBuilder libratoBatchBuilder

    void setParams(params) {
        libratoBatchBuilder = new LibratoBatchBuilder(params.email, params.key)
    }

    @Override
    Sender newSender() {
        return new LibratoSender()
    }

    class LibratoSender implements Sender {
        private StdoutSender stdoutSender = new StdoutSender()

        void send(String source, Map<String, Number> metrics) {
            LibratoBatch libratoBatch = libratoBatchBuilder.build()
            stdoutSender.send(source, metrics)
            long epoch = System.currentTimeMillis() / 1000

            metrics.each { entry ->
                libratoBatch.addGaugeMeasurement(entry.key, entry.value)
            }

            BatchResult result = libratoBatch.post(source, epoch)
            if (!result.success()) {
                for (PostResult post : result.getFailedPosts()) {
                    println "Could not POST to Librato: " + post
                }
            }
            // see https://github.com/librato/librato-java/issues/32
            libratoBatch.httpPoster.close()
        }
    }
}
