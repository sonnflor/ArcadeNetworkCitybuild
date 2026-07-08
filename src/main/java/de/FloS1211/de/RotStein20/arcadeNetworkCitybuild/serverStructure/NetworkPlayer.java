package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.serverStructure;

import java.util.UUID;

public class NetworkPlayer {
  private final UUID uuid;
  private final String server;

  public NetworkPlayer(UUID uuid, String server) {
    this.uuid = uuid;
    this.server = server;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getServer() {
    return server;
  }
}
