package committee.nova.encounter.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import committee.nova.encounter.Encounter;

import net.neoforged.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class EncounterConfigManager {
    public static final Path encounterDir = FMLPaths.CONFIGDIR.get().resolve("encounter");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static Optional<EncounterConfig> getEncounterConfig() {
        if (!encounterDir.toFile().isDirectory()) {
            try {
                Files.createDirectories(encounterDir);
            } catch (IOException e) {
                Encounter.LOGGER.error("Failed to create directory for encounter config!", e);
                return Optional.empty();
            }
        }
        final Path cfgPath = encounterDir.resolve("encounter.json");
        EncounterConfig config = new EncounterConfig();
        if (cfgPath.toFile().isFile()) {
            try {
                config = GSON.fromJson(FileUtils.readFileToString(cfgPath.toFile(),
                        StandardCharsets.UTF_8), EncounterConfig.class);
                return Optional.of(config);
            } catch (IOException e) {
                Encounter.LOGGER.error("Failed to parse encounter config \"{}\"", cfgPath, e);
                FileUtils.deleteQuietly(cfgPath.toFile());
                return createOffsetConfig(cfgPath, config) ? Optional.of(config) : Optional.empty();
            }
        } else return createOffsetConfig(cfgPath, config) ? Optional.of(config) : Optional.empty();
    }

    public static boolean createOffsetConfig(Path cfgPath, EncounterConfig config) {
        try {
            FileUtils.write(cfgPath.toFile(), GSON.toJson(config), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            Encounter.LOGGER.error("Failed to create encounter config \"{}\"", cfgPath, e);
        }
        return false;
    }
}
