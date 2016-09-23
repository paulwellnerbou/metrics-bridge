package de.wellnerbou.librato.metrics;

import com.librato.metrics.DefaultHttpPoster;
import com.librato.metrics.HttpPoster;
import com.librato.metrics.LibratoBatch;
import com.librato.metrics.Sanitizer;

import java.util.concurrent.TimeUnit;

/**
 * This class is here until https://github.com/librato/librato-java/pull/31 is accepted
 *
 * @author Paul Wellner Bou &lt;paul@wellnerbou.de&gt;
 */
public class LibratoBatchBuilder {

	public static final String LIBRATO_METRICS_URL_V1 = "https://metrics-api.librato.com/v1/metrics";
	private static final String DEFAULT_USER_AGENT = "Librato Java Library";
	private static final int DEFAULT_POST_BATCH_SIZE = 300;
	private static final long DEFAULT_TIMEOUT_SECONDS = 10L;
	private Long seconds = DEFAULT_TIMEOUT_SECONDS;
	private int batchSize = DEFAULT_POST_BATCH_SIZE;
	private Sanitizer sanitizer = Sanitizer.NO_OP;
	private HttpPoster httpPoster;

	public LibratoBatchBuilder(HttpPoster httpPoster) {
		this.httpPoster = httpPoster;
	}

	public LibratoBatchBuilder withTimeoutInSeconds(Long seconds) {
		this.seconds = seconds;
		return this;
	}

	public LibratoBatchBuilder withBatchSize(int batchSize) {
		this.batchSize = batchSize;
		return this;
	}

	public LibratoBatchBuilder withSanitizer(Sanitizer sanitizer) {
		this.sanitizer = sanitizer;
		return this;
	}

	public LibratoBatch build() {
		return new LibratoBatch(batchSize, sanitizer, seconds, TimeUnit.SECONDS, DEFAULT_USER_AGENT, httpPoster);
	}
}
