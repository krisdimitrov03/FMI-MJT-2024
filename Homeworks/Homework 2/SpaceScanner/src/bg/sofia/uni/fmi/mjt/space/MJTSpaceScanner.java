package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.algorithm.SymmetricBlockCipher;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class MJTSpaceScanner implements SpaceScannerAPI {

    private List<Mission> missions;

    private List<Rocket> rockets;

    private final SymmetricBlockCipher cipher;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        loadMissions(missionsReader);
        loadRockets(rocketsReader);

        cipher = new Rijndael(secretKey);
    }

    private void loadMissions(Reader reader) {
        try (var rocketsBufferedReader = new BufferedReader(reader)) {
            missions = rocketsBufferedReader.lines().skip(1).map(Mission::of).toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadRockets(Reader reader) {
        try (var rocketsBufferedReader = new BufferedReader(reader)) {
            rockets = rocketsBufferedReader.lines().skip(1).map(Rocket::of).toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Collection<Mission> getAllMissions() {
        return Collections.unmodifiableCollection(missions);
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        if (missionStatus == null) {
            throw new IllegalArgumentException("missionStatus is null");
        }

        return missions.stream()
                .filter(m -> m.missionStatus().equals(missionStatus))
                .toList();
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from or to is null");
        }

        Map<String, Long> result = missions.stream()
                .filter(m -> m.date().isAfter(from) || m.date().equals(from))
                .filter(m -> m.date().isBefore(to) || m.date().equals(to))
                .filter(m -> m.missionStatus().equals(MissionStatus.SUCCESS))
                .collect(Collectors.groupingBy(Mission::company, Collectors.counting()));

        return result.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions.stream()
                .collect(Collectors.groupingBy(Mission::getCountry))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is less than ot equal to 0");
        }

        if (missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("missionStatus or rocketStatus is null");
        }

        return missions.stream()
                .filter(m -> m.missionStatus().equals(missionStatus))
                .filter(m -> m.rocketStatus().equals(rocketStatus))
                .filter(m -> m.cost().isPresent())
                .sorted(Comparator.comparingDouble(e -> e.cost().get()))
                .limit(n)
                .toList();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        return missions.stream()
                .collect(Collectors.groupingBy(Mission::company,
                        Collectors.groupingBy(Mission::location,
                                Collectors.counting())))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().entrySet().stream()
                                .max(Comparator.comparingLong(Map.Entry::getValue))
                                .get()
                                .getKey()));
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("from or to is null");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("to is before from");
        }

        return missions.stream()
                .filter(m -> m.date().isAfter(from) || m.date().equals(from))
                .filter(m -> m.date().isBefore(to) || m.date().equals(to))
                .collect(Collectors.groupingBy(Mission::company))
                .entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .filter(Mission::isSuccessful)
                                .collect(Collectors.groupingBy(Mission::location,
                                        Collectors.counting()))
                                .entrySet()
                                .stream()
                                .max(Map.Entry.comparingByValue())))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().isPresent() ? e.getValue().get().getKey() : ""));
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return Collections.unmodifiableCollection(rockets);
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is less than or equal to 0");
        }

        return rockets.stream()
                .sorted(Comparator.comparingDouble(r -> r.height().isPresent() ? r.height().get() : 0))
                .toList()
                .reversed()
                .stream()
                .limit(n)
                .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets.stream()
                .collect(Collectors.toMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n,
                                                                          MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is less than or equal to 0");
        }

        if (missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("missionStatus or rocketStatus is null");
        }

        return getTopNMostExpensiveMissions(n).stream()
                .map(m -> rockets.stream()
                        .filter(r -> m.detail().rocketName().equals(r.name()))
                        .toList()
                        .getFirst()
                        .wiki())
                .toList().stream()
                .map(s -> s.orElse(""))
                .toList();
    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        if (outputStream == null || from == null || to == null) {
            throw new IllegalArgumentException("outputStream, from or to is null");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("to is before from");
        }

        if (rockets.isEmpty() || missions.isEmpty()) {
            return;
        }

        String rocketName = getMostReliableRocketName(from, to);

        cipher.encrypt(new ByteArrayInputStream(rocketName.getBytes()), outputStream);
    }

    private List<Mission> getTopNMostExpensiveMissions(int n) {
        return missions.stream()
                .sorted(Comparator.comparingDouble(e -> e.cost().isPresent() ? e.cost().get() : 0))
                .toList()
                .reversed()
                .stream()
                .limit(n)
                .toList();
    }

    private String getMostReliableRocketName(LocalDate from, LocalDate to) {
        Optional<Rocket> rocket = rockets.stream()
                .max(Comparator.comparingDouble(r -> calculateRocketReliability(r, from, to)));

        return rocket.isPresent() ? rocket.get().name() : "";
    }

    private double calculateRocketReliability(Rocket rocket, LocalDate from, LocalDate to) {
        long successfulMissionsCount = getRocketSuccessfulMissions(rocket, from, to).size();
        long unsuccessfulMissionsCount = getRocketUnsuccessfulMissions(rocket, from, to).size();
        long allMissionsCount = getRocketAllMissions(rocket, from, to).size();

        return (double) (2 * successfulMissionsCount + unsuccessfulMissionsCount) / (2 * allMissionsCount);
    }

    private List<Mission> getRocketSuccessfulMissions(Rocket rocket, LocalDate from, LocalDate to) {
        return getRocketAllMissions(rocket, from, to)
                .stream()
                .filter(Mission::isSuccessful)
                .toList();
    }

    private List<Mission> getRocketUnsuccessfulMissions(Rocket rocket, LocalDate from, LocalDate to) {
        return getRocketAllMissions(rocket, from, to)
                .stream()
                .filter(m -> !m.isSuccessful())
                .toList();
    }

    private List<Mission> getRocketAllMissions(Rocket rocket, LocalDate from, LocalDate to) {
        return missions.stream()
                .filter(m -> m.detail().rocketName().equals(rocket.name()))
                .filter(m -> m.date().isAfter(from) || m.date().equals(from))
                .filter(m -> m.date().isBefore(to) || m.date().equals(to))
                .toList();
    }

}
