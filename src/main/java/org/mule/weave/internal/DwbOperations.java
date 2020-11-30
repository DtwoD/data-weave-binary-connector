package org.mule.weave.internal;

import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.weave.dwb.api.IWeaveValue;
import org.mule.runtime.weave.dwb.api.WeaveDOMReader;
import org.mule.runtime.weave.dwb.api.WeaveStreamFactoryService;
//import org.mule.runtime.weave.dwb.api.WeaveStreamReader;
//import org.mule.weave.v2.module.dwb.reader.DefaultWeaveStreamReader;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mule.runtime.extension.api.annotation.param.MediaType.TEXT_PLAIN;


/**
 * This class covers both reading and writing dwb content.
 */
public class DwbOperations {

  @Inject
  private WeaveStreamFactoryService dwbService;

  /**
   * Example of a simple operation that receives a string parameter and returns a dwb formatted object with that string as the
   * value for a "message" key.
   */
  @MediaType(value = "application/dwb")
  public InputStream write(String message) {
    return dwbService.getFactory().createStreamWriter(new ByteArrayOutputStream())
      .writeStartDocument()
        .writeStartObject()
          .writeKey("message")
          .writeString(message)
        .writeEndObject()
      .writeEndDocument()
      .getResult();
  }

  /**
   * Example of an operation that receives a string parameter and returns a dwb formatted object with that string as the
   * value for a "message" key, with a custom processor for it.
   */
  @MediaType(value = "application/dwb")
  public InputStream writeCustom(String message) {
    return dwbService.getFactory().createStreamWriter(new ByteArrayOutputStream())
      .writeStartDocument()
      .writeStartObject()
      .writeKey("*")
      .writeBinary(message.getBytes())
      .writeEndObject()
      .writeStartSchema()
      .writeKey("processor")
      .writeString("org.mule.weave.api.DwbCustomProcessor")
      .writeKey("schemaPath")
      .writeString("/path/to/schema")
      .writeKey("path")
      .writeString("a.b.c")
      .writeEndSchema()
      .writeEndDocument()
      .getResult();
  }

  /**
   * Example of an operation that uses the DOM reader to extract a "message" key value from a dwb formatted object.
   */
  @MediaType(value = TEXT_PLAIN)
  public String extract(@Content(primary = true) InputStream data){
    WeaveDOMReader domReader = dwbService.getFactory().createDOMReader(data);
    String message = (String) domReader.read().evaluateAsObject().get("message").evaluate();
//    domReader.close();
    return message;
  }

  /**
   * Example of an operation that uses the stream reader to read a "message" key value from a dwb formatted object.
   */
  @MediaType(value = TEXT_PLAIN)
  public String read(@Content(primary = true) InputStream data) throws IOException {
    return dwbService.getFactory().createDOMReader(data).read().evaluate().toString();

//    // {
//    streamReader.next();
//    // message :
//    streamReader.next();
//    // value needed
//    streamReader.next();
//    String value = streamReader.getString();
//    streamReader.close();
//    return value;

//    IWeaveValue<String> value = streamReader.read();
//    return value.evaluate();
  }

  @MediaType(value = TEXT_PLAIN)
  public String readCustom(@Content(primary = true) InputStream data, String key) throws IOException {
    WeaveDOMReader domReader = dwbService.getFactory().createDOMReader(data);
    String value = (String) domReader.read().evaluateAsObject().get(key).evaluate();
//    domReader.close();
    return value;
  }

}
