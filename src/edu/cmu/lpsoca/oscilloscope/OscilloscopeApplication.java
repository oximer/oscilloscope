package edu.cmu.lpsoca.oscilloscope;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import edu.cmu.lpsoca.oscilloscope.services.BoardService;
import edu.cmu.lpsoca.oscilloscope.services.SampleService;
import edu.cmu.lpsoca.oscilloscope.services.StartService;
import edu.cmu.lpsoca.oscilloscope.services.StopService;
import org.apache.log4j.BasicConfigurator;
import org.glassfish.jersey.server.ResourceConfig;

import javax.servlet.ServletException;

/**
 * Created by urbano on 4/5/16.
 */
public class OscilloscopeApplication extends ResourceConfig {

    public OscilloscopeApplication() throws ServletException {
        packages("edu.cmu.lpsoca.oscilloscope");
        register(BoardService.class);
        register(StartService.class);
        register(StopService.class);
        register(SampleService.class);
        register(JacksonJaxbJsonProvider.class);
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        setApplicationName("Oscilloscope and Identification Service");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e.getCause() + e.getMessage());
        }
        BasicConfigurator.configure();
    }
}
