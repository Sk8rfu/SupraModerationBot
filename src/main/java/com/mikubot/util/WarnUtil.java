package com.mikubot.util;

import java.io.*;
import java.util.*;

public class WarnUtil {

    private static final File FILE = new File("warnings.txt");

    // Формат на файла:
    // userId:reason1|reason2|reason3

    private static Map<String, List<String>> load() {
        Map<String, List<String>> data = new HashMap<>();

        if (!FILE.exists()) {
            return data;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(":", 2);
                if (parts.length != 2) continue;

                String userId = parts[0];
                String reasonsPart = parts[1];

                List<String> reasons = new ArrayList<>();
                if (!reasonsPart.isEmpty()) {
                    String[] split = reasonsPart.split("\\|");
                    reasons.addAll(Arrays.asList(split));
                }

                data.put(userId, reasons);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static void save(Map<String, List<String>> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (Map.Entry<String, List<String>> entry : data.entrySet()) {
                String userId = entry.getKey();
                List<String> reasons = entry.getValue();

                String joined = String.join("|", reasons);
                writer.write(userId + ":" + joined);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Добавяне на предупреждение
    public static void addWarn(String userId, String reason) {
        Map<String, List<String>> data = load();
        data.computeIfAbsent(userId, k -> new ArrayList<>()).add(reason);
        save(data);
    }

    // Изчистване на предупреждения
    public static void clearWarns(String userId) {
        Map<String, List<String>> data = load();
        data.remove(userId);
        save(data);
    }

    // Взимане на предупреждения
    public static List<String> getWarns(String userId) {
        Map<String, List<String>> data = load();
        return data.getOrDefault(userId, new ArrayList<>());
    }
}
