package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.gui;

public enum GuiButtonType {
  CUSTOM("custom"),
  NEXT_PAGE("next_page"),
  PREV_PAGE("prev_page"),
  CLOSE("close"),
  ACCEPT("accept"),
  REJECT("reject");
  private final String type;

  GuiButtonType(String type) {this.type = type;}

  public String getType() {
    return type;
  }
}
