package org.mule.weave.api;

import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.weave.dwb.api.IWeaveValue;
import org.mule.runtime.weave.dwb.api.WeaveProcessor;

import java.io.InputStream;
import java.util.Map;

/**
 * This class mocks the REDEFINES usage.
 */
public class DwbCustomProcessor implements WeaveProcessor {

    @Override
    public Object get(InputStream inputStream, String key, Map<String, IWeaveValue<?>> map) {
        String message = IOUtils.toString(inputStream);
        int length = message.length();
        if ("raw".equals(key)) {
            return message.substring(1, length - 1);
        } else if ("sanitized".equals(key)) {
            return message.substring(3, length - 3);
        } else {
            return message;
        }
    }
}
