package de.wellnerbou.metrics.collect

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Slf4j
class MetricCollectorJson {

    public static final String METRICS_BRIDGE_NAMESPACE_PREFIX = "-metrics-"
    String addr
    String auth
    def JsonSlurper jsonSlurper = new JsonSlurper();

    public void setAddr(String addr) {
        if(addr.contains("@")) {
            addr.replaceAll(/^[\w]*:\/\/([^@]*)@.*$/) { all, auth ->
                this.auth = auth
            }
            this.addr = addr.replace("//${auth}@", "//")
        } else {
            this.addr = addr
        }
    }

    Map<String, Number> collectMetrics(String[] jsonFields) {
        def before = System.currentTimeMillis()
        def fetched = fetch()
        def elapsedTime = System.currentTimeMillis() - before
        log.info("Fetching metrics from {} took {} seconds", addr, elapsedTime/1000.0)
        def json = jsonSlurper.parseText(fetched)
        def entries = jsonFields.collectEntries {
            [(it): json."$it"]
        }
        entries["response.time"] = elapsedTime
        entries[METRICS_BRIDGE_NAMESPACE_PREFIX + ".response.time"] = elapsedTime
        entries[METRICS_BRIDGE_NAMESPACE_PREFIX + ".collect.timestamp"] = before
        return entries
    }

    protected String fetch() {
        if(new File(addr).exists()) {
            return new File(addr).text
        } else {
            return httpFetch();
        }
    }

    protected String httpFetch() {
        def conn = addr.toURL().openConnection()
        if (auth?.length() > 0) {
            conn.setRequestProperty("Authorization", "Basic " + auth.getBytes().encodeBase64().toString())
        }
        return conn.content.text
    }
}
