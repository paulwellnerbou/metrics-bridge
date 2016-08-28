package de.wellnerbou.metrics.send.librato

import com.librato.metrics.BatchResult
import com.librato.metrics.LibratoBatch
import com.librato.metrics.PostResult
import de.wellnerbou.metrics.Sender
import de.wellnerbou.metrics.send.StdoutSender

class LibratoSender implements Sender {

    private LibratoBatch libratoBatch
    private StdoutSender stdoutSender = new StdoutSender()

    void send(String source, Map<String, Number> metrics) {
        stdoutSender.send(source, metrics)
        long epoch = System.currentTimeMillis() / 1000

        metrics.each { entry ->
            libratoBatch.addGaugeMeasurement(entry.key, entry.value)
        }

        BatchResult result = libratoBatch.post(source, epoch)
        if (!result.success()) {
            for (PostResult post : result.getFailedPosts()) {
                println "Could not POST to Librato: {}", post
            }
        }
    }
}
