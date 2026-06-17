package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mute;

public class MuteEntry {
  private final String uuid;
  private final long muteTimestamp;
  private final long muteDuration;
  private final String muterUuid;
  private final String reason;

  public MuteEntry(String uuid, long muteTimestamp, long muteDuration, String muterUuid, String reason) {
    this.uuid = uuid;
    this.muteTimestamp = muteTimestamp;
    this.muteDuration = muteDuration;
    this.muterUuid = muterUuid;
    this.reason = reason;
  }

  public String getUuid() {
    return uuid;
  }

  public long getMuteTimestamp() {
    return muteTimestamp;
  }

  public long getMuteDuration() {
    return muteDuration;
  }

  public String getMuterUuid() {
    return muterUuid;
  }

  public String getReason() {
    return reason;
  }

  @Override
  public String toString() {
    return uuid+"|"+muteTimestamp+"|"+muteDuration+"|"+muterUuid+"|"+reason.replace("|","/");
  }

  public static MuteEntry fromString(String string) {
    String[] strings = string.split("\\|");
    return new MuteEntry(strings[0],Long.parseLong(strings[1]),Long.parseLong(strings[2]),strings[3],strings[4]);
  }
}
