package com.prayerflickhelper;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Prayer Flick Helper"
)
public class PrayerFlickHelperPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private PrayerFlickHelperConfig config;
	@Inject
	private PrayerFlickHelperOverlay prayerFlickHelperOverlay;
	@Inject
	private OverlayManager overlayManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("PrayerFlickHelper started");
		overlayManager.add(prayerFlickHelperOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("PrayerFlickHelper end");
		overlayManager.remove(prayerFlickHelperOverlay);
	}

	@Provides
	PrayerFlickHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PrayerFlickHelperConfig.class);
	}
}
