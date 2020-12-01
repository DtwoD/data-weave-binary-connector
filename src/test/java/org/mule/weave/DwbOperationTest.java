package org.mule.weave;
import org.junit.jupiter.api.Test;
import org.mule.runtime.weave.dwb.api.IWeaveValue;
import org.mule.runtime.weave.dwb.api.WeaveDOMReader;
import org.mule.runtime.weave.dwb.api.WeaveStreamFactoryService;
import org.mule.runtime.weave.dwb.api.WeaveStreamWriter;
import org.mule.weave.utils.PrinterVisitor;
import org.mule.weave.v2.el.dwb.WeaveStreamFactoryServiceImpl;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;

public class DwbOperationTest {

    private WeaveStreamFactoryService dwbService = new WeaveStreamFactoryServiceImpl();

    @Test
    public void testPrintWrite() throws Exception{
        ByteArrayOutputStream bot = new ByteArrayOutputStream();
        ByteArrayOutputStream bot2 = new ByteArrayOutputStream();

        WeaveStreamWriter wsw = dwbService.getFactory().createStreamWriter(bot);
        wsw.writeStartDocument()
                .writeStartObject()
                .writeKey("message")
                .writeString("B2B Rul3z")
                .writeEndObject();

        WeaveStreamWriter wsw2 = dwbService.getFactory().createStreamWriter(bot2);
        wsw2.writeStartDocument()
                .writeStartObject()
                .writeKey("message2")
                .writeString("Mul3 Rul3z")
                .writeEndObject()
                .writeEndDocument();

        wsw.mergeStream(wsw2.getResult());
        wsw.writeString("TEST STRING");

        wsw.writeEndDocument();

        WeaveDOMReader wdr = dwbService.getFactory().createDOMReader(wsw.getResult());
        IWeaveValue read_data = wdr.read();
        PrinterVisitor visitor = new PrinterVisitor();
        read_data.accept(visitor);
        System.out.println(visitor.toString());
    }
}
