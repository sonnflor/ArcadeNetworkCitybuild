package de.FloS1211.de.RotStein20.arcadeNetworkCitybuild.clickableMessages;

public record ClickAction(
    String type,
    String[] args,
    boolean expiresAfterUse
) {}
