package io.github.cgh;

import org.apache.commons.validator.UrlValidator;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.hash.Hashing.murmur3_32;
import static java.nio.charset.StandardCharsets.UTF_8;

@Path("/shortened-urls")
public class ShortenedUrlsResource {

    @Inject
    Jedis jedis;

    @Inject
    UrlValidator urlValidator;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{id}")
    public Response redirect(@PathParam("id") String id) throws URISyntaxException {
        String url = jedis.get(id);
        return url != null ? Response.temporaryRedirect(new URI(url)).build()
                : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/{url: .*}")
    public Response save(@PathParam("url") String url) {
        if (urlValidator.isValid(url)) {
            String id = murmur3_32().hashString(url, UTF_8).toString();
            jedis.set(id, url);
            return Response.ok("localhost:8080/shortened-urls/" + id).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}