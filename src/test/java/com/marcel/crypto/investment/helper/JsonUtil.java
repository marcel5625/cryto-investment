package com.marcel.crypto.investment.helper;

import org.assertj.core.api.AssertProvider;
import org.springframework.boot.test.json.JsonContentAssert;

public class JsonUtil {

    public static AssertProvider<JsonContentAssert> forJson(String json) {
        return () -> new JsonContentAssert(JsonUtil.class, json);
    }
}
