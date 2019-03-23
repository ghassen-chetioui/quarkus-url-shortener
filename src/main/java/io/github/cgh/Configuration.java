package io.github.cgh;

import org.apache.commons.validator.UrlValidator;
import redis.clients.jedis.Jedis;

import javax.enterprise.inject.Produces;

public class Configuration {

    @Produces
    public Jedis jedis() {
        return new Jedis("localhost");
    }

    @Produces
    public UrlValidator urlValidator() {
        return new UrlValidator(new String[]{"http", "https"});
    }

}
