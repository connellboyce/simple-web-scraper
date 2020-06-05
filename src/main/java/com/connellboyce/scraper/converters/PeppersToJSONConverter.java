package com.connellboyce.scraper.converters;

import com.connellboyce.scraper.scrapers.PepperScraper;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.util.Set;

public class PeppersToJSONConverter {
    ObjectMapper mapper = new ObjectMapper();

    public void convertToSystemOut(Set<PepperScraper.PepperDetail> pepperSet) {
        try {
            //mapper.enable(SerializationFeature.INDENT_OUTPUT);
            JsonGenerator jsonGenerator = mapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
            jsonGenerator.useDefaultPrettyPrinter().writeObject(pepperSet);
            mapper.writeValue(System.out,pepperSet);
            jsonGenerator.flush();
            jsonGenerator.close();
        } catch (IOException ioException){
            System.out.println(ioException.getMessage());
        }
    }

}
