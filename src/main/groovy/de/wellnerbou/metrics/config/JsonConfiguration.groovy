package de.wellnerbou.metrics.config

import groovy.json.JsonSlurper

/**
 * @author Paul Wellner Bou <paul@wellnerbou.de>
 */
class JsonConfiguration {

    JsonSlurper jsonSlurper = new JsonSlurper()

    Object load(String uri) {
        assert uri != null
        InputStream inputStream = getInputStream(uri)
        jsonSlurper.parse(inputStream)
    }

    protected InputStream getInputStream(String uri) {
        assert uri != null
        InputStream is;
        if(new File(uri).exists()) {
            is = new File(uri).newInputStream()
        }
        if(is == null && uri.startsWith("/")) {
            is = this.getClass().getResourceAsStream("/config.json");
        }
        if(is == null) {
            is = uri.toURL().newInputStream()
        }
        return is
    }
}
