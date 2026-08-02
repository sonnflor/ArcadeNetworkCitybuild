package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.mail;

import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.Gui;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButton;
import de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui.GuiButtonExecutor;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MailButtonExecutor implements GuiButtonExecutor {
  @Override
  public void customAction(String buttonId, Gui gui, Player player, ClickType clickType) {
    if (buttonId.startsWith("mail")) {
      GuiButton button = (GuiButton) gui.getElement(buttonId);
      if (clickType.equals(ClickType.LEFT)) { //open mail
        String rawMessage = button.customData.get("message");
        rawMessage = "§n" + button.customData.get("caption") + "§r§0\n-------------------\n\n" + rawMessage;
        Book book = Book.book(
            Component.text(button.customData.get("caption")),
            Component.text("ArcadeNetwork-Mail: " + button.customData.get("author")),
            paginate(rawMessage)
        );
        player.openBook(book);
        if (button.customData.get("playerRole").equals("target") || button.customData.get("playerRole").equals("both")) {
          MailManager.setMailRead(Integer.parseInt(button.customData.get("id")));
        }
      } else if (clickType.equals(ClickType.RIGHT)) { //delete mail
        player.openInventory(Gui.getConfirmationGui(
            Map.of(
                "mailId", button.customData.get("id"),
                "postOfficeBox",gui.customData.get("postOfficeBox"),
                "playerRole", button.customData.get("playerRole"),
                "onlyFavorites", gui.customData.get("onlyFavorites")
            ),
            this,
            "Akzeptieren, um die Mail zu löschen"
            ).buildInventory());
      } else if (clickType.isShiftClick()) { //favorite mail
        boolean isFavorite = Boolean.parseBoolean(button.customData.get("favorite"));
        MailManager.setMailFavorite(Integer.parseInt(button.customData.get("id")), !isFavorite, button.customData.get("playerRole"));
        MailManager.openMail(player, gui.customData.get("postOfficeBox"),gui.customData.get("onlyFavorites").equals("true"));
      }
    } else if (buttonId.equals("sendMailButton")) {
      MailManager.sendMail(player);
    } else if (buttonId.equals("inbox")) {
      MailManager.openMail(player,"inbox",gui.customData.get("onlyFavorites").equals("true"));
    } else if (buttonId.equals("outbox")) {
      MailManager.openMail(player,"outbox",gui.customData.get("onlyFavorites").equals("true"));
    } else if (buttonId.equals("onlyFavorites")) {
      boolean onlyFavorites = Boolean.parseBoolean(gui.customData.get("onlyFavorites"));
      MailManager.openMail(player, gui.customData.get("postOfficeBox"), !onlyFavorites);
    }
  }

  @Override
  public void switchPage(String buttonId, Gui gui, int page, Player player, ClickType clickType) {

  }

  @Override
  public void closeGui(String buttonId, Gui gui, Player player, ClickType clickType) {

  }

  @Override
  public void accept(String buttonId, Gui gui, Player player, ClickType clickType) {
    MailManager.deleteMail(Integer.parseInt(gui.customData.get("mailId")), gui.customData.get("playerRole"));
    MailManager.openMail(player, gui.customData.get("postOfficeBox"),gui.customData.get("onlyFavorites").equals("true"));
  }

  @Override
  public void reject(String buttonId, Gui gui, Player player, ClickType clickType) {
    player.closeInventory();
    MailManager.openMail(player, gui.customData.get("postOfficeBox"),gui.customData.get("onlyFavorites").equals("true"));
  }

  @Override
  public void onSwitch(String buttonId, Gui gui, boolean state, Player player, ClickType clickType) {

  }

  private static final int CHARS_PER_LINE = 19;
  private static final int LINES_PER_PAGE = 14;
  private static final int CHARS_PER_PAGE = CHARS_PER_LINE * LINES_PER_PAGE;

  public static Component[] paginate(String text) {
    LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection();

    List<Component> pages = new ArrayList<>();
    StringBuilder page = new StringBuilder();

    int pagePos = 0;
    int lastSpaceIndex = -1;
    int lastSpacePagePos = -1;

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);

      // Legacy-Farbcodes übernehmen, aber nicht mitzählen
      if (c == '§' && i + 1 < text.length()) {
        page.append(c).append(text.charAt(++i));
        continue;
      }

      page.append(c);

      if (c == ' ') {
        lastSpaceIndex = page.length() - 1;
        lastSpacePagePos = pagePos;
      }

      if (c == '\n') {
        pagePos += CHARS_PER_LINE - (pagePos % CHARS_PER_LINE);
      } else {
        pagePos++;
      }

      if (pagePos > CHARS_PER_PAGE) {
        if (lastSpaceIndex != -1) {
          // Bis zur letzten Leerstelle auf die aktuelle Seite
          pages.add(serializer.deserialize(page.substring(0, lastSpaceIndex)));

          // Rest auf die nächste Seite übernehmen
          String rest = page.substring(lastSpaceIndex + 1);
          page.setLength(0);
          page.append(rest);

          pagePos -= (lastSpacePagePos + 1);

          lastSpaceIndex = -1;
          lastSpacePagePos = -1;
        } else {
          // Ein einzelnes Wort ist länger als eine Seite
          pages.add(serializer.deserialize(page.toString()));
          page.setLength(0);
          pagePos = 0;
        }
      }
    }

    if (!page.isEmpty()) {
      pages.add(serializer.deserialize(page.toString()));
    }

    return pages.toArray(Component[]::new);
  }
}
