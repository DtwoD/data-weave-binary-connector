package org.mule.weave;

import org.junit.Test;
import org.mule.functional.api.flow.FlowRunner;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.mule.runtime.weave.dwb.api.IWeaveValue;
import org.mule.runtime.weave.dwb.api.WeaveDOMReader;
import org.mule.runtime.weave.dwb.api.WeaveStreamFactoryService;
import org.mule.runtime.weave.dwb.api.WeaveStreamWriter;
import org.mule.weave.utils.PrinterVisitor;

import javax.inject.Inject;

public class DwbOperationsTestCase extends MuleArtifactFunctionalTestCase {

  @Inject
  private WeaveStreamFactoryService dwbService;

  /**
   * Specifies the mule config xml with the flows that are going to be executed in the tests, this file lives in the test resources.
   */
  @Override
  protected String getConfigFile() {
    return "test-mule-config.xml";
  }

  @Test
  public void read() throws Exception {
    runAndValidate("DW: a *fun* language", flowRunner("readTest"));
  }

  @Test
  public void extract() throws Exception {
    String testMessage = "DataWeave Rul3z!";
    runAndValidate(testMessage, flowRunner("extractTest").withVariable("someMessage", testMessage));
  }

  @Test
  public void write() throws Exception {
    String testMessage = "DataWeave Rul3z!";
    runAndValidate(testMessage, flowRunner("writeTest").withVariable("someMessage", testMessage));
  }

  @Test
  public void writeAndRead() throws Exception {
    runAndValidate("DW: a *fun* language", flowRunner("writeAndReadTest"));
  }

  @Test
  public void writeCustom() throws Exception {
    runAndValidate("This is the actual message", flowRunner("writeCustomTest"));
  }

  @Test
  public void writeAndReadCustom() throws Exception {
    runAndValidate("--This is the actual message--", flowRunner("writeAndReadCustomTest"));
  }

  private void runAndValidate(String expectedPayload, FlowRunner flowRunner) throws Exception {
    String payloadValue = ((String) flowRunner
            .run()
            .getMessage()
            .getPayload()
            .getValue());
    assertThat(payloadValue, is(expectedPayload));
  }

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
    String msg = (String) wdr.read().evaluateAsObject().get("message").evaluate();

    System.out.println(msg);
  }
}
