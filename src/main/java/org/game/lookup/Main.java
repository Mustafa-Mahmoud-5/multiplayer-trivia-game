package org.game.lookup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.game.common.jsonProtocol.QuestionsResponse;
import org.game.common.jsonProtocol.RequestQuestions;
import org.game.common.models.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 4444;
    private static final String QUESTIONS_FILE_PATH = "src/main/java/org/game/lookup/questions.json";

    private final Gson gson = new GsonBuilder().create();
    private final List<Question> allQuestions;

    public Main() {
        this.allQuestions = Collections.unmodifiableList(loadQuestions());
    }

    public static void main(String[] args) {
        Main server = new Main();
        server.start();
    }

    public void start() {
        int workersCount = Math.max(4, Runtime.getRuntime().availableProcessors() * 2);
        ExecutorService workers = Executors.newFixedThreadPool(workersCount);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Lookup server is running on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                workers.submit(() -> handleClient(socket));
            }
        } catch (IOException e) {
            System.err.println("Failed to start lookup server on port " + PORT);
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (
                socket;
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                QuestionsResponse response = handleRequest(line);
                out.println(gson.toJson(response));
            }
        } catch (Exception e) {
            System.err.println("Error while handling lookup client: " + e.getMessage());
        }
    }

    private QuestionsResponse handleRequest(String rawJson) {
        UUID requestId = null;

        try {
            JsonObject rawRequest = gson.fromJson(rawJson, JsonObject.class);
            if (rawRequest == null) {
                return QuestionsResponse.error(UUID.randomUUID(), "INVALID_REQUEST");
            }

            RequestQuestions request = gson.fromJson(rawRequest, RequestQuestions.class);
            if (request == null) {
                return QuestionsResponse.error(UUID.randomUUID(), "INVALID_REQUEST");
            }

            requestId = request.getRequestId();
            if (requestId == null) {
                return QuestionsResponse.error(UUID.randomUUID(), "MISSING_REQUEST_ID");
            }

            if (request.getCount() <= 0) {
                return QuestionsResponse.error(requestId, "INVALID_COUNT");
            }

            if (rawRequest.has("category") && !rawRequest.get("category").isJsonNull() && request.getCategory() == null) {
                return QuestionsResponse.error(requestId, "INVALID_CATEGORY");
            }

            if (rawRequest.has("difficulty") && !rawRequest.get("difficulty").isJsonNull() && request.getDifficulty() == null) {
                return QuestionsResponse.error(requestId, "INVALID_DIFFICULTY");
            }

            List<Question> filtered = filterQuestions(request);

            if (filtered.size() < request.getCount()) {
                return QuestionsResponse.error(requestId, "NOT_ENOUGH_QUESTIONS");
            }

            Collections.shuffle(filtered);
            List<Question> selected = new ArrayList<>(filtered.subList(0, request.getCount()));

            return QuestionsResponse.ok(requestId, selected);
        } catch (JsonParseException e) {
            return QuestionsResponse.error(requestId != null ? requestId : UUID.randomUUID(), "INVALID_JSON");
        } catch (Exception e) {
            return QuestionsResponse.error(requestId != null ? requestId : UUID.randomUUID(), "INTERNAL_ERROR");
        }
    }

    private List<Question> filterQuestions(RequestQuestions request) {
        List<Question> filtered = new ArrayList<>();

        for (Question question : allQuestions) {
            boolean matchesCategory = request.getCategory() == null
                    || request.getCategory().name().equalsIgnoreCase(question.getCategory());

            boolean matchesDifficulty = request.getDifficulty() == null
                    || request.getDifficulty().name().equalsIgnoreCase(question.getDifficulty());

            if (matchesCategory && matchesDifficulty) {
                filtered.add(question);
            }
        }

        return filtered;
    }

    private List<Question> loadQuestions() {
        String json = readQuestionsJson();
        Question[] questions = gson.fromJson(json, Question[].class);

        if (questions == null || questions.length == 0) {
            throw new IllegalStateException("questions.json is empty. Add at least one question.");
        }

        List<Question> result = new ArrayList<>();
        Collections.addAll(result, questions);
        return result;
    }

    private String readQuestionsJson() {
        try (InputStream in = Main.class.getResourceAsStream("/org/game/lookup/questions.json")) {
            if (in != null) {
                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException ignored) {
            // Fallback to local source path below.
        }

        try {
            return Files.readString(Path.of(QUESTIONS_FILE_PATH), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read questions.json", e);
        }
    }
}
