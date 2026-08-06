package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.remoteConsole;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.ArcadeNetworkCitybuild;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure.ProxyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandPoller {
  private static final JavaPlugin plugin = ArcadeNetworkCitybuild.getInstance();
  private static final HttpClient client = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(5))
      .build();
  public static long fastPollingUntil = 0;
  private static final AtomicBoolean polling = new AtomicBoolean(false);

  private static long getDelayTicks() {
    long now = System.currentTimeMillis();
    if (now < fastPollingUntil) {
      return 20L * 5;      // 5 Sekunden
    }
    return 20L * 120;        // 2 Minuten
  }

  private static void poll() {
    if (!polling.compareAndSet(false, true)) return;

    String serverName = ProxyManager.getServerName();
    String url = "https://myberry.v6.rocks/arcade-network-console/commands.php?server="
        + URLEncoder.encode(serverName, StandardCharsets.UTF_8);

    String apiKey = "aWd-u-G;n&CLeRiGzK>[b<:fN;;[5@FH{3K|Cb_GSx[n6Ze35+p?G=)^(lOf]F61?E]Fm7bYGS+DzUz%jSJH)1$D6TQe7hle7vC~{+Gs%jUr-N+#cvOP?WAFki9pN1V=/p~nvkb$/,:i*9n:+MYun}oOGfXspe>oE~Sj+Sa)qy[0c#4O58Ebk~E5%]hF^5mYamv-UvVt";

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .timeout(Duration.ofSeconds(10))
        .header("X-API-Key", apiKey)
        .GET()
        .build();

    client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
        .thenAccept(response -> {
          if (response.statusCode() != 200) {
            plugin.getLogger().warning("Polling fehlgeschlagen: HTTP " + response.statusCode());
            return;
          }

          String body = response.body();
          if (body == null || body.isBlank()) return;

          fastPollingUntil = System.currentTimeMillis() + (10 * 60 * 1000);

          String[] lines = body.split("\\R");
          for (String line : lines) {
            String command = line.trim();
            if (command.isEmpty()) continue;

            Bukkit.getScheduler().runTask(plugin, () -> {
              ConsoleCommandSender console = Bukkit.getConsoleSender();
              Bukkit.dispatchCommand(console, command);
            });
          }
        })
        .exceptionally(ex -> {
          plugin.getLogger().warning("Polling-Fehler: " + ex.getMessage());
          return null;
        })
        .whenComplete((r, ex) -> {
          polling.set(false);
          scheduleNext();
        });
  }

  private static void scheduleNext() {
    Bukkit.getScheduler().runTaskLaterAsynchronously(
        plugin,
        CommandPoller::poll,
        getDelayTicks()
    );
  }

  public static void start() {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, CommandPoller::poll);
  }
}

