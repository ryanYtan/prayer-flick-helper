package com.prayerflickhelper;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
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

	public static List<HitsplatApplied> hitsplatZeroesOnPlayer = new CopyOnWriteArrayList<>();

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

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied)
	{
		if (hitsplatApplied.getHitsplat().getAmount() != 0)
		{
			return;
		}
		if (!hitsplatApplied.getHitsplat().isMine() || !hitsplatApplied.getActor().equals(client.getLocalPlayer()))
		{
			return;
		}
		// { hitsplat of "0" was applied by an enemy on the player }
		hitsplatZeroesOnPlayer.add(hitsplatApplied);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		int currentGameCycle = client.getGameCycle();
		hitsplatZeroesOnPlayer.removeIf(ha -> ha.getHitsplat().getDisappearsOnGameCycle() > currentGameCycle);
	}

	@Provides
	PrayerFlickHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PrayerFlickHelperConfig.class);
	}
}
