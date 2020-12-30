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



        WeaveStreamWriter wsw2 = dwbService.getFactory().createStreamWriter(bot2);
        wsw2.writeStartDocument()
            .writeStartObject()
                .writeKey("message")
                .writeString("Mul3 Rul3z")
            .writeEndObject()
        .writeEndDocument();

        /*
        wsw.writeKey("object_2")
            .mergeStream(wsw2.getResult())
            .writeKey("object_3")
            .writeString("TEST STRING")
            .writeEndObject();*/

        wsw.writeStartDocument()
            .writeStartObject()
                .beginTransaction()
                    .writeKey("MyObject")
                    .commit()
                    .writeStartObject()
                        .writeKey("MyKEY")
                        .writeString("MyVALUE")
                    .writeEndObject()
                    .writeKey("MyArray")
                    .commit()
                    .writeStartArray()
                        .writeInt(1)
                    .writeEndArray()
                    .writeKey("MyMerge")
                    .commit()
                    .mergeStream(wsw2.getResult())
                .endTransaction()
            .writeEndObject()
        .writeEndDocument();

        WeaveDOMReader wdr = dwbService.getFactory().createDOMReader(wsw.getResult());
        IWeaveValue read_data = wdr.read();
        PrinterVisitor visitor = new PrinterVisitor();
        read_data.accept(visitor);
        System.out.println(visitor.toString());

    }
}
