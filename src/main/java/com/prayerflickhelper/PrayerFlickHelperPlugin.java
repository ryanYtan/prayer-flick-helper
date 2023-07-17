package com.prayerflickhelper;

import com.google.inject.Provides;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.PlayerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

	public static List<HitsplatApplied> hitsplatsOnPlayer = new CopyOnWriteArrayList<>();
	public static AtomicInteger savedHealthRatio = new AtomicInteger(Integer.MAX_VALUE);

	@Override
	protected void startUp() throws Exception
	{
		log.info("PrayerFlickHelper started");
		//overlayManager.add(prayerFlickHelperOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("PrayerFlickHelper end");
		//overlayManager.remove(prayerFlickHelperOverlay);
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied)
	{
		if (!hitsplatApplied.getHitsplat().isMine() || !hitsplatApplied.getActor().equals(client.getLocalPlayer()))
		{
			return;
		}
		// { hitsplat of "0" was applied by an enemy on the player }
		hitsplatsOnPlayer.add(hitsplatApplied);
	}

	@Subscribe
	public void onPlayerChanged(PlayerChanged playerChanged)
	{
		playerChanged.getPlayer();
	}


	@Provides
	PrayerFlickHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PrayerFlickHelperConfig.class);
	}
}
