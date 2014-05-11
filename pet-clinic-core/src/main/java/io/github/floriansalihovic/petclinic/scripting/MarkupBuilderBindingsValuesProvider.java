package io.github.floriansalihovic.petclinic.scripting;

import groovy.xml.*;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.scripting.api.*;
import org.slf4j.*;

import javax.script.*;
import java.io.*;

@Component
@Service
public class MarkupBuilderBindingsValuesProvider implements BindingsValuesProvider {

    private final static Logger logger = LoggerFactory.getLogger(MarkupBuilderBindingsValuesProvider.class);

    @Override
    public void addBindings(Bindings bindings) {
        final PrintWriter out = (PrintWriter) bindings.get("out");
        if (null == out) {
            logger.error("Expected print writer is not available.");
        } else {
            logger.info("Providing binding markupBuilder:{}.", MarkupBuilder.class.getName());
            bindings.put("markupBuilder", new MarkupBuilder(out));
        }
    }
}
