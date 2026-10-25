package ua.conference.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.conference.exception.InvalidDataException;
import ua.conference.model.Venue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VenueLoader {
    private static final Logger log = LoggerFactory.getLogger(VenueLoader.class);

    private VenueLoader() {
    }

    public static List<Venue> loadVenues(String filePath) {
        List<Venue> venues = new ArrayList<>();
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            log.warn("File not found at path: {}. Skipping read operation.", filePath);
            return venues;
        }

        int lineNumber = 0;
        BufferedReader reader = null;

        try {
            reader = Files.newBufferedReader(path);
            String header = reader.readLine();
            lineNumber++;

            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Venue venue = parseVenueLine(line, lineNumber);
                    venues.add(venue);
                } catch (InvalidDataException e) {
                    log.error("Line {}: Skipped invalid data - {}", lineNumber, e.getMessage());
                }
            }
            log.info("Loaded {} rooms", venues.size());
        } catch (IOException e) {
            log.error("Failed to read file: {}", filePath, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("Failed to close reader resource", e);
                }
            }
        }

        return venues;
    }

    public static Venue parseVenueLine(String line, int lineNumber) throws InvalidDataException {
        if (line == null || line.isBlank()) {
            throw new InvalidDataException("Line " + lineNumber + ": Line is empty or null.");
        }

        String[] parts = line.split(",");
        if (parts.length < 3) {
            throw new InvalidDataException("Line " + lineNumber + ": Expected 3 columns (venueCode, name, capacity), got: " + line);
        }

        String venueCode = parts[0].trim();
        String name = parts[1].trim();
        String capacityStr = parts[2].trim();

        try {
            int capacity = Integer.parseInt(capacityStr);
            return Venue.of(venueCode, name, capacity);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Line " + lineNumber + ": Invalid venue data in line '" + line + "'", e);
        }
    }
}