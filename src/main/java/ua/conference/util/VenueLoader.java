package ua.conference.util;

import ua.conference.model.Venue;
import ua.conference.exception.InvalidDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class VenueLoader {
    private static final Logger log = LoggerFactory.getLogger(VenueLoader.class);

    public static List<Venue> loadVenues(String filePath) {
        List<Venue> venues = new ArrayList<>();
        Path path = Path.of(filePath);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 0;

            String header = reader.readLine();
            if (header != null) {
                lineNumber++;
            }

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

        } catch (NoSuchFileException e) {
            log.warn("File not found at path: {}. Skipping read operation.", filePath);
        } catch (IOException e) {
            log.error("I/O error occurred while reading file: {}", filePath, e);
        } finally {
            log.info("Finished CSV processing attempt for path: {}", filePath);
        }

        return venues;
    }

    public static Venue parseVenueLine(String line, int lineNumber) throws InvalidDataException {
        if (line == null || line.isBlank()) {
            throw new InvalidDataException("Line " + lineNumber + ": Line is empty or null.");
        }

        String[] parts = line.split(",");

        try {
            String venueCode = parts[0].trim();
            String name = parts[1].trim();
            int capacity = Integer.parseInt(parts[2].trim());

            return Venue.of(venueCode, name, capacity);

        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidDataException("Line " + lineNumber + ": Invalid venue data in line '" + line + "'", e);
        }
    }
}