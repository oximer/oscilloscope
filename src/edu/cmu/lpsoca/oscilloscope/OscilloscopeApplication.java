package edu.cmu.lpsoca.oscilloscope;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import edu.cmu.lpsoca.oscilloscope.services.*;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by urbano on 4/5/16.
 */
public class OscilloscopeApplication extends ResourceConfig {

    public OscilloscopeApplication() {
        packages("edu.cmu.lpsoca.oscilloscope");
        register(BoardService.class);
        register(CommandService.class);
        register(StartService.class);
        register(StopService.class);
        register(SampleService.class);
        register(JacksonJaxbJsonProvider.class);
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

    }
}
