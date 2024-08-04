package net.chrotos.wakatime;

import com.google.gson.Gson;
import lombok.NonNull;
import net.chrotos.wakatime.Metadata;
import org.bukkit.Bukkit;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

public class ApiClient {
    private final Gson gson = new Gson();
    private final URI baseUrl;
    private final HttpClient httpClient;
    private final String apiKey;

    public ApiClient(@NonNull String baseUrl, @NonNull String apiKey) {
        this.baseUrl = URI.create(baseUrl);
        this.httpClient = HttpClient.newBuilder().build();
        this.apiKey = apiKey;
    }

    public CompletableFuture<Boolean> sendHeartbeat(@NonNull Heartbeat heartbeat, @NonNull String user) {
        String hostname = Bukkit.getMotd();
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {}

        HttpRequest request = HttpRequest.newBuilder()
                .uri(baseUrl.resolve("v1/users/" + user + "/heartbeats"))
                //.uri(baseUrl.resolve("v1/users/current/heartbeats"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(apiKey.getBytes()))
                .header("X-Machine-Name", hostname)
                .header("User-Agent", getUserAgent())
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(heartbeat)))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> response.statusCode() == 201);
    }

    private String getUserAgent() {
        return getOs() + "-" + Bukkit.getName() + "/" + Bukkit.getBukkitVersion() + " " + Bukkit.getName() + "-wakatime/" + Metadata.VERSION;
    }

    private String getOs() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return "win";
        } else if (os.contains("mac")) {
            return "mac";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return "linux";
        } else {
            return "Unknown";
        }
    }
}
