package de.wellnerbou.metrics.collect

import groovy.json.JsonSlurper

class MetricCollectorJson {

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
        def json = jsonSlurper.parseText(fetch())
        return jsonFields.collectEntries {
            [(it): json."$it"]
        }
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
