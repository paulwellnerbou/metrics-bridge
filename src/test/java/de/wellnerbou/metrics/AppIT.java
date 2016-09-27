package de.wellnerbou.metrics;

import de.wellnerbou.metrics.app.CliApp;

public class AppIT {

	public static void main(String[] args) {
		final String config = "./private-data/config-...json";
		CliApp.main(new String[]{"-c", config});
	}
}
