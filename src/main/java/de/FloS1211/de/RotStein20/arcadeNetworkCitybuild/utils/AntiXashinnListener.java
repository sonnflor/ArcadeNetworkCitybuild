package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AntiXashinnListener implements Listener {
  @EventHandler
  public void onXashinnJoin(PlayerJoinEvent event) {
    //Adrian kühn (Xashinn; uuid = d9aa2475-b663-46eb-933a-cdd8ed195af5) ist ein kleiner verschissener Hurensohn und hat unser Haus in die Luft gesprengt ohne jeglichen Grund oder Konversation dabor
    if (event.getPlayer().getUniqueId().equals(UUID.fromString("d9aa2475-b663-46eb-933a-cdd8ed195af5"))) {
      List<String> beleidigungen = List.of("Hurensohn","Kackbratze","Affe","Hexe","Ginger Hexe","Schlappschwanz","Flasche","Vollpfosten","Loser","Nichtznutz","Versager","Kotzbrocken","Taugenichts","Drecksack","Ekelpaket","Feigling","Geizhalz","Schmarotzer","Großmaul","Flachzange","Hornochse","Lappen","Memme","Lauch","Weichei","Heulsuse","Tölpel","Neger");
      List<String> adjektive = List.of("hässlicher","dummer","verkackter","verschissener","dreckiger","verfickter","verhuurter","nichtnutzender","lauchiger","behinderter","kack","kleiner","rothaariger","rotsackhaariger","3 cm schwänziger","scheiß");
      Random random = new Random();
      event.getPlayer().kick(Component.text(adjektive.get(random.nextInt(adjektive.size()))+beleidigungen.get(random.nextInt(beleidigungen.size()))));
    }
  }
}
