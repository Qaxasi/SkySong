package com.mycompany.SkySong.testutils.common;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class BaseWireMock {

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(options().port(8080))
            .build();
}