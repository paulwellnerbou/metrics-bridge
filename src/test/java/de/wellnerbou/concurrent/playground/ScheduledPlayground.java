package de.wellnerbou.concurrent.playground;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
public class ScheduledPlayground {
	private static final Logger LOG = LoggerFactory.getLogger(ScheduledPlayground.class);

	public static void main(String[] args) {

		new Timer().scheduleAtFixedRate(
				new TimerTask() {
					@Override
					public void run() {

						new Timer(true).schedule(new TimerTask(){
							@Override
							public void run() {
								long epoch = System.currentTimeMillis();
								LOG.info("[{}] Running {}", epoch, this);
								LOG.info("[{}] Waiting 2 seconds", epoch, this);
								try {
									Thread.sleep(2000L);
								} catch (InterruptedException e) {
									LOG.error("[{}] {}", epoch, e.getMessage(), e);
								}
								LOG.info("[{}] Done.", epoch);
							}
						}, new Date());
					}
				}, new Date(), 1000
		);
	}
}
