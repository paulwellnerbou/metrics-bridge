package de.wellnerbou.metrics.app

import de.wellnerbou.metrics.app.App

class CliApp {

//    static String email = "paul@wellnerbou.de"
//    static String apiToken = "6cbd8939edeb9d89112751bcaeda752d6e7b163dbdac82daddd93fcbb8df724d"

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
        if (!options || !options.u || !options.t || !options.c) {
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
        cli.u(longOpt: 'user', 'Librato user/email address', args: 1)
        cli.t(longOpt: 'token', 'Librato api token', args: 1)
        cli.c(longOpt: 'config', 'Path to JSON config file', args: 1)
        return cli
    }
}
