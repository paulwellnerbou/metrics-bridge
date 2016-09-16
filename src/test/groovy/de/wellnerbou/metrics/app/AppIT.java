package de.wellnerbou.metrics.app;

public class AppIT {

	public static void main(String[] args) {
		final String config = "./private-data/config-...json";
		CliApp.main(new String[]{"-c", config});
	}

}
