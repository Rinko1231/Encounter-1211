package committee.nova.encounter.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EncounterConfig {
    public List<EncounterEntry> encounterEntries = new ArrayList<>();

    public EncounterConfig() {
        this.encounterEntries.add(new EncounterEntry(
                "example:entity_1",
                Lists.newArrayList("example:avoid_1", "example:avoid_2"),
                Lists.newArrayList("example:hostile_1", "example:hostile_2")
        ));
        this.encounterEntries.add(new EncounterEntry(
                "example:entity_2",
                Lists.newArrayList("example:avoid_3", "example:avoid_4"),
                Lists.newArrayList("example:hostile_3", "example:hostile_4")
        ));
    }


    public static class EncounterEntry {
        @SerializedName("entity_id")
        public final String entityId;

        public final List<String> avoid = new ArrayList<>();

        public final List<String> hostile = new ArrayList<>();

        public EncounterEntry(String entityId, List<String> avoid, List<String> hostile) {
            this.entityId = entityId;
            this.avoid.addAll(avoid);
            this.hostile.addAll(hostile);
        }
    }
}
