package com.gamezone.model;

import com.gamezone.persistence.AccessoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccessoryModuleTest {
    @TempDir
    Path tempDir;

    @Test
    void controllerNormalizesConnectionAndConsoleIds() {
        Controller controller = new Controller(
                "A1", "Control", 50.0, 4,
                List.of(" C1 ", "C1", "C2"), " wireless ");

        assertEquals("Wireless", controller.getConnectionType());
        assertEquals(List.of("C1", "C2"), controller.getCompatibleConsoleIds());
        assertTrue(controller.isCompatibleWith(" C1 "));
        assertFalse(controller.isCompatibleWith("C3"));
    }

    @Test
    void accessoryCompatibilityListCannotBeModifiedThroughGetter() {
        Controller controller = new Controller("A1", "Control", 50.0, 4, List.of("C1"), "Wired");

        assertThrows(UnsupportedOperationException.class,
                () -> controller.getCompatibleConsoleIds().add("C2"));
    }

    @Test
    void rejectsInvalidAccessorySpecificValues() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Controller("A1", "Control", 50.0, 4, List.of(), "Bluetooth")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Cable("A2", "Cable", 10.0, 4, List.of(), 0.0, "HDMI")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Cable("A2", "Cable", 10.0, 4, List.of(), Double.NaN, "HDMI")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Cable("A2", "Cable", 10.0, 4, List.of(), 2.0, "  ")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Memory("A3", "Memory", 20.0, 4, List.of(), 0, "microSD")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Memory("A3", "Memory", 20.0, 4, List.of(), 64, "  "))
        );
    }

    @Test
    void rejectsBlankCompatibilityIdentifiers() {
        assertThrows(IllegalArgumentException.class,
                () -> new Controller("A1", "Control", 50.0, 4, List.of("C1", " "), "Wired"));
    }

    @Test
    void accessoryRepositoryRoundTripsAllSupportedTypes() throws IOException {
        Path file = tempDir.resolve("accessories.csv");
        AccessoryRepository repository = new AccessoryRepository(file);
        List<Accessory> accessories = new ArrayList<>(List.of(
                new Controller("A1", "Control", 50.0, 4, List.of("C1", "C2"), "Wireless"),
                new Cable("A2", "Cable", 10.0, 8, List.of("C1"), 2.5, "HDMI"),
                new Memory("A3", "Memory", 20.0, 6, List.of("C2"), 128, "microSD")
        ));

        repository.saveAll(accessories);
        List<Accessory> loaded = repository.loadAll();

        assertEquals(3, loaded.size());
        assertInstanceOf(Controller.class, loaded.get(0));
        assertInstanceOf(Cable.class, loaded.get(1));
        assertInstanceOf(Memory.class, loaded.get(2));
        assertEquals("Wireless", ((Controller) loaded.get(0)).getConnectionType());
        assertEquals(2.5, ((Cable) loaded.get(1)).getLengthMeters(), 0.0001);
        assertEquals("HDMI", ((Cable) loaded.get(1)).getConnectorType());
        assertEquals(128, ((Memory) loaded.get(2)).getCapacityGb());
        assertEquals("microSD", ((Memory) loaded.get(2)).getMemoryType());
        assertEquals(List.of("C1", "C2"), loaded.get(0).getCompatibleConsoleIds());
    }
}
