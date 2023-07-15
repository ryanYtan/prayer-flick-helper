package com.prayerflickhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
public class PrayerFlickHelperOverlay extends Overlay  {
    private final PrayerFlickHelperPlugin plugin;
    private final PrayerFlickHelperConfig config;
    private final Client client;

    @Inject
    private PrayerFlickHelperOverlay(PrayerFlickHelperPlugin plugin, PrayerFlickHelperConfig config, Client client)
    {
        this.plugin = plugin;
        this.config = config;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        return null;
    }
}
