package de.wellnerbou.metrics.app

class CliApp {

    public static void main(String[] args) {
        App app = createApp(args)
        if(app != null) {
            app.start()
        }
    }

    protected static App createApp(String... args) {
        def app = null
        CliBuilder cli = createCli()
        def options = parseCliArgs(cli, args)
        if (!options || !options.c) {
            cli.usage();
        } else {
            app = new App(options)
        }
        return app
    }

    protected static OptionAccessor parseCliArgs(CliBuilder cli, String[] args) {
        return cli.parse(args)
    }

    protected static CliBuilder createCli() {
        def cli = new CliBuilder(usage: 'collectMetrics-metrics [options]')
        cli.c(longOpt: 'config', 'Path to JSON config file', args: 1)
        return cli
    }
}
