package de.wellnerbou.metrics.collect

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Slf4j
class MetricCollectorJson {

    public static final String METRICS_BRIDGE_NAMESPACE_PREFIX = "-metrics-"
    public static final String COLLECT_TIMESTAMP = METRICS_BRIDGE_NAMESPACE_PREFIX + ".collect.timestamp"
    public static final String RESPONSE_TIME = METRICS_BRIDGE_NAMESPACE_PREFIX + ".response.time"
    public static final int DEFAULT_TIMEOUT = 5000
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
        def entries = [:]
        try {
            def fetched = fetch()
            def json = jsonSlurper.parseText(fetched)
            entries = jsonFields.collectEntries {
                [(it): json."$it"]
            }
        } catch (Exception e) {
            log.warn("Exception fetching metrics: {}", e.getMessage(), e)
        }
        def elapsedTime = System.currentTimeMillis() - before
        log.info("Fetching metrics from {} took {} seconds", addr, elapsedTime/1000.0)

        entries["response.time"] = elapsedTime
        entries[RESPONSE_TIME] = elapsedTime
        entries[COLLECT_TIMESTAMP] = before
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
            conn.setConnectTimeout(DEFAULT_TIMEOUT)
            conn.setReadTimeout(DEFAULT_TIMEOUT)
        }
        return conn.content.text
    }
}
