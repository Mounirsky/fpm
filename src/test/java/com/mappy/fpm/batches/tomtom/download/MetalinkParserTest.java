package com.mappy.fpm.batches.tomtom.download;

import com.mappy.fpm.batches.tomtom.download.MetalinkParser.Metalink;
import com.mappy.fpm.batches.tomtom.download.MetalinkParser.MetalinkUrl;

import org.junit.Test;

import static com.google.common.collect.Lists.*;
import static org.assertj.core.api.Assertions.*;

public class MetalinkParserTest {
    @Test
    public void should_parse_metalink() {
        Metalink metalink = MetalinkParser.parse(getClass().getResourceAsStream("/tomtom/download/Europe.metalink.xml"));

        assertThat(metalink.size()).isEqualTo(3);
        assertThat(metalink.forCountry("fra"))
                .isEqualTo(new Metalink(newArrayList(MetalinkUrl.parse("eur2016_03-shpd-2dcmnb-fra-f10.7z.001", "http://edelivery2.tomtom.com/eur2016_03-shpd-2dcmnb-fra-f10.7z.001"))));
        assertThat(metalink.forCountry("fra").forZone("f10"))
                .isEqualTo(new Metalink(newArrayList(MetalinkUrl.parse("eur2016_03-shpd-2dcmnb-fra-f10.7z.001", "http://edelivery2.tomtom.com/eur2016_03-shpd-2dcmnb-fra-f10.7z.001"))));

        assertThat(metalink.zones()).containsExactly("f10", "f11", "f12");
        assertThat(metalink.types()).containsExactly("2dcmnb");
    }
}
