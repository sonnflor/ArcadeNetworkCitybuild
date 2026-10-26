package de.FloS1211.de.RotStein20.arcadeNetwork.clickableMessages;

public record ClickAction(
    String type,
    String[] args,
    boolean expiresAfterUse
) {}
