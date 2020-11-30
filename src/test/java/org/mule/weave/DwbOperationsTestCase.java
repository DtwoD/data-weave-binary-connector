package org.mule.weave;

import org.junit.Test;
import org.mule.functional.api.flow.FlowRunner;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DwbOperationsTestCase extends MuleArtifactFunctionalTestCase {

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

}
