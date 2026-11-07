package de.FloS1211.de.RotStein20.arcadeNetwork.utils;

import de.FloS1211.de.RotStein20.arcadeNetwork.ArcadeNetwork;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class MoliMessageExecutor implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
    String message = String.join(" ", args);
    if (args.length == 0) {
      return false;
    }

    // PowerShell außerhalb des Server-Hauptthreads starten
    Bukkit.getScheduler().runTaskAsynchronously(ArcadeNetwork.getInstance(), () -> {
      try {
        Path scriptPath = ArcadeNetwork.getInstance().getDataFolder().toPath()
            .resolve("moli/notify.ps1");
        ProcessBuilder processBuilder = new ProcessBuilder(
            "powershell.exe",
            "-NoProfile",
            "-ExecutionPolicy", "Bypass",
            "-File", scriptPath.toAbsolutePath().toString(),
            "-Title", "ArcadeNetwork",
            "-Message", message
        );
        processBuilder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        processBuilder.redirectError(ProcessBuilder.Redirect.DISCARD);
        processBuilder.start();
      } catch (Exception e) {
        Bukkit.getLogger().warning(
            "Windows-Benachrichtigung fehlgeschlagen: " + e.getMessage()
        );
      }
    });
    return true;
  }
}
