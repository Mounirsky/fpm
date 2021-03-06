package com.mappy.fpm.batches.tomtom.helpers;

import com.mappy.fpm.batches.AbstractTest;
import com.mappy.fpm.batches.tomtom.TomtomFolder;
import com.mappy.fpm.batches.tomtom.helpers.TownTagger.Centroid;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TownTaggerTest extends AbstractTest {

    private TownTagger townTagger;

    @Before
    public void setup() {

        TomtomFolder tomtomFolder = mock(TomtomFolder.class);
        when(tomtomFolder.getFile("sm.shp")).thenReturn("src/test/resources/tomtom/town/Anderlecht___________sm.shp");

        townTagger = new TownTagger(tomtomFolder);
    }

    @Test
    public void should_load_centroids() {

        Centroid centroid = townTagger.get(10560000388234L);
        assertCentroid(centroid, 10560000388234L, "Sint-Gillis", 8, 8, new Double[]{4.3451859, 50.8251293});

        centroid = townTagger.get(10560000455427L);
        assertCentroid(centroid, 10560000455427L, "Vorst", 9, 8, new Double[]{4.3134424, 50.8055758});

        centroid = townTagger.get(10560000718742L);
        assertCentroid(centroid, 10560000718742L, "Anderlecht", 8, 7, new Double[]{4.307077, 50.8366041});
    }

    @Test
    public void should_load_capitals() {
        List<Centroid> capitals = townTagger.getCapital(7);

        assertThat(capitals).extracting(Centroid::getName).containsOnly("Antwerpen", "Brussel", "Halle", "Hasselt", "Leuven", "Maaseik", "Mechelen", "Tongeren", "Turnhout");
        assertThat(capitals).extracting(Centroid::getAdminclass).containsOnly(0, 2, 7);
    }

    @Test
    public void should_return_village_with_get_place_of_citytyp_0() {
        assertThat(townTagger.get(10560000379424L).getPlace()).isEqualTo(of("village"));
    }

    @Test
    public void should_return_city_with_get_place_of_citytyp_1_and_dispclass_lower_than_8() {
        assertThat(townTagger.get(10560000718742L).getPlace()).isEqualTo(of("city"));
    }

    @Test
    public void should_return_town_with_get_place_of_citytyp_1_and_dispclass_greater_than_8() {
        assertThat(townTagger.get(10560000388234L).getPlace()).isEqualTo(of("town"));
    }

    @Test
    public void should_return_city_with_get_place_of_citytyp_2_and_dispclass_lower_than_8() {
        assertThat(townTagger.get(10560000710744L).getPlace()).isEqualTo(of("city"));
    }

    @Test
    public void should_return_town_with_get_place_of_citytyp_2_and_dispclass_greater_than_8() {
        assertThat(townTagger.get(10560000712665L).getPlace()).isEqualTo(of("town"));
    }

    @Test
    public void should_return_hamlet_with_get_place_of_citytyp_32() {
        assertThat(townTagger.get(10560000308734L).getPlace()).isEqualTo(of("hamlet"));
    }

    @Test
    public void should_return_neighbourhood_with_get_place_of_citytyp_64() {
        assertThat(townTagger.get(10560000407632L).getPlace()).isEqualTo(of("neighbourhood"));
    }

    private void assertCentroid(Centroid centroid, Long id, String name, Integer adminClass, Integer dispClass, Double[] point) {
        assertThat(centroid.getId()).isEqualTo(id);
        assertThat(centroid.getName()).isEqualTo(name);
        assertThat(centroid.getAdminclass()).isEqualTo(adminClass);
        assertThat(centroid.getCitytyp()).isEqualTo(1);
        assertThat(centroid.getDispclass()).isEqualTo(dispClass);
        assertThat(centroid.getPoint().getX()).isEqualTo(point[0]);
        assertThat(centroid.getPoint().getY()).isEqualTo(point[1]);
    }
}