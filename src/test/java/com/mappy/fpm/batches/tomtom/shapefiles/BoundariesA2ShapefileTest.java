package com.mappy.fpm.batches.tomtom.shapefiles;

import com.mappy.fpm.batches.AbstractTest;
import com.mappy.fpm.batches.tomtom.TomtomFolder;
import com.mappy.fpm.batches.tomtom.dbf.names.NameProvider;
import com.mappy.fpm.batches.tomtom.helpers.OsmLevelGenerator;
import net.morbz.osmonaut.osm.Relation;
import net.morbz.osmonaut.osm.RelationMember;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static com.google.common.collect.ImmutableMap.of;
import static com.mappy.fpm.batches.tomtom.Tomtom2OsmTestUtils.PbfContent;
import static com.mappy.fpm.batches.tomtom.Tomtom2OsmTestUtils.read;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BoundariesA2ShapefileTest extends AbstractTest {

    private static PbfContent pbfContent;

    @BeforeClass
    public static void setup() throws Exception {

        TomtomFolder tomtomFolder = mock(TomtomFolder.class);
        when(tomtomFolder.getFile("___a2.shp")).thenReturn("src/test/resources/tomtom/boundaries/a2/belbe2___________a2.shp");

        NameProvider nameProvider = mock(NameProvider.class);
        when(nameProvider.getAlternateNames(10560000000838L)).thenReturn(of("name", "Limburg"));

        OsmLevelGenerator osmLevelGenerator = mock(OsmLevelGenerator.class);
        when(osmLevelGenerator.getOsmLevel("belbe2", "2")).thenReturn("6");

        BoundariesA2Shapefile shapefile = new BoundariesA2Shapefile(tomtomFolder, nameProvider, osmLevelGenerator);

        shapefile.serialize("target/tests/");

        pbfContent = read(new File("target/tests/a2.osm.pbf"));
    }

    @Test
    public void should_have_members_with_tags() throws Exception {

        Relation limburg = pbfContent.getRelations().stream().filter(member -> member.getTags().hasKeyValue("ref:tomtom", "10560000000838")).findFirst().get();
        assertThat(limburg.getTags().size()).isEqualTo(7);
        assertThat(limburg.getTags().get("name")).isEqualTo("Limburg");
        assertThat(limburg.getTags().get("boundary")).isEqualTo("administrative");
        assertThat(limburg.getTags().get("ref:INSEE")).isEqualTo("70000");
        assertThat(limburg.getTags().get("type")).isEqualTo("boundary");
        assertThat(limburg.getTags().get("admin_level")).isEqualTo("6");
        assertThat(limburg.getTags().get("layer")).isEqualTo("6");

        Relation antwerpen = pbfContent.getRelations().stream().filter(member -> member.getTags().hasKeyValue("ref:tomtom", "10560000000848")).findFirst().get();
        assertThat(antwerpen.getTags().size()).isEqualTo(7);
        assertThat(antwerpen.getTags().get("name")).isEqualTo("Antwerpen");
        assertThat(antwerpen.getTags().get("boundary")).isEqualTo("administrative");
        assertThat(antwerpen.getTags().get("ref:INSEE")).isEqualTo("10000");
        assertThat(antwerpen.getTags().get("type")).isEqualTo("boundary");
        assertThat(antwerpen.getTags().get("admin_level")).isEqualTo("6");
        assertThat(antwerpen.getTags().get("layer")).isEqualTo("6");

        Relation vlaams = pbfContent.getRelations().stream().filter(member -> member.getTags().hasKeyValue("ref:tomtom", "10560000000821")).findFirst().get();
        assertThat(vlaams.getTags().size()).isEqualTo(7);
        assertThat(vlaams.getTags().get("name")).isEqualTo("Vlaams-Brabant");
        assertThat(vlaams.getTags().get("boundary")).isEqualTo("administrative");
        assertThat(vlaams.getTags().get("ref:INSEE")).isEqualTo("20001");
        assertThat(vlaams.getTags().get("type")).isEqualTo("boundary");
        assertThat(vlaams.getTags().get("admin_level")).isEqualTo("6");
        assertThat(vlaams.getTags().get("layer")).isEqualTo("6");
    }

    @Test
    public void should_have_relation_with_role_label_and_tags() throws Exception {
        List<RelationMember> labels = pbfContent.getRelations().stream()//
                .flatMap(relation -> relation.getMembers().stream())//
                .filter(relationMember -> relationMember.getRole().equals("label"))//
                .collect(toList());

        assertThat(labels).hasSize(3);

        RelationMember limburg = labels.stream().filter(member -> member.getEntity().getTags().hasKeyValue("ref:tomtom", "10560000000838")).findFirst().get();
        assertThat(limburg.getEntity().getTags().size()).isEqualTo(3);
        assertThat(limburg.getEntity().getTags().get("name")).isEqualTo("Limburg");
        assertThat(limburg.getEntity().getTags().get("ref:INSEE")).isEqualTo("70000");

        RelationMember antwerpen = labels.stream().filter(member -> member.getEntity().getTags().hasKeyValue("ref:tomtom", "10560000000848")).findFirst().get();
        assertThat(antwerpen.getEntity().getTags().size()).isEqualTo(3);
        assertThat(antwerpen.getEntity().getTags().get("name")).isEqualTo("Antwerpen");
        assertThat(antwerpen.getEntity().getTags().get("ref:INSEE")).isEqualTo("10000");

        RelationMember vlaams = labels.stream().filter(member -> member.getEntity().getTags().hasKeyValue("ref:tomtom", "10560000000821")).findFirst().get();
        assertThat(vlaams.getEntity().getTags().size()).isEqualTo(3);
        assertThat(vlaams.getEntity().getTags().get("name")).isEqualTo("Vlaams-Brabant");
        assertThat(vlaams.getEntity().getTags().get("ref:INSEE")).isEqualTo("20001");
    }

    @Test
    public void should_have_inner_boundaries_in_vlaams_brabant() throws Exception {
        Relation vlaamsBrabant = pbfContent.getRelations().stream().filter(member -> member.getTags().hasKeyValue("ref:tomtom", "10560000000821")).findFirst().get();
        assertThat(vlaamsBrabant.getMembers().stream().filter(relationMember -> relationMember.getRole().equals("inner")).collect(toList())).isNotEmpty();
    }

    @Test
    public void should_have_some_outer_boundaries_in_all_3_relations() throws Exception {
        Relation limburg = pbfContent.getRelations().stream().filter(member -> member.getTags().hasKeyValue("ref:tomtom", "10560000000838")).findFirst().get();
        assertThat(limburg.getMembers().stream().filter(relationMember -> relationMember.getRole().equals("outer")).collect(toList())).isNotEmpty();

        Relation antwerpen = pbfContent.getRelations().stream().filter(member -> member.getTags().hasKeyValue("ref:tomtom", "10560000000848")).findFirst().get();
        assertThat(antwerpen.getMembers().stream().filter(relationMember -> relationMember.getRole().equals("outer")).collect(toList())).isNotEmpty();

        Relation vlaams = pbfContent.getRelations().stream().filter(member -> member.getTags().hasKeyValue("ref:tomtom", "10560000000821")).findFirst().get();
        assertThat(vlaams.getMembers().stream().filter(relationMember -> relationMember.getRole().equals("outer")).collect(toList())).isNotEmpty();
    }
}