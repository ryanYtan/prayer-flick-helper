package com.prayerflickhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
public class PrayerFlickHelperOverlay extends Overlay  {
    private final PrayerFlickHelperPlugin plugin;
    private final PrayerFlickHelperConfig config;
    private final Client client;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private PrayerFlickHelperOverlay(PrayerFlickHelperPlugin plugin, PrayerFlickHelperConfig config, Client client)
    {
        setPosition(OverlayPosition.TOP_LEFT);
        this.plugin = plugin;
        this.config = config;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
		if (PrayerFlickHelperPlugin.hitsplatsOnPlayer.isEmpty())
		{
			return panelComponent.render(graphics);
		}

        panelComponent.getChildren().clear();
		String textAttackBlockedWithPrayer = "BLOCKED";
		String textAttackNotBlockedWithPrayer = "MISSED";

		int textBoxSize = Math.max(
			graphics.getFontMetrics().stringWidth(textAttackBlockedWithPrayer),
			graphics.getFontMetrics().stringWidth(textAttackNotBlockedWithPrayer)
		);
        panelComponent.setPreferredSize(new Dimension(textBoxSize, 0));

		boolean prayerOn = isAnyProtectionPrayerOn();
		boolean hasZeroHitsplat = !PrayerFlickHelperPlugin.hitsplatsOnPlayer.isEmpty();
		LineComponent lc = LineComponent.builder()
			.left(prayerOn && hasZeroHitsplat ? textAttackBlockedWithPrayer : textAttackNotBlockedWithPrayer)
			.leftColor(prayerOn && hasZeroHitsplat ? Color.GREEN : Color.RED)
			.build();
		panelComponent.getChildren().add(lc);

		PrayerFlickHelperPlugin.hitsplatsOnPlayer.clear();

		return panelComponent.render(graphics);
    }

	private boolean isAnyProtectionPrayerOn()
	{
		boolean protectMagicOn = client.getVarbitValue(Varbits.PRAYER_PROTECT_FROM_MAGIC) == 1;
		boolean protectMeleeOn = client.getVarbitValue(Varbits.PRAYER_PROTECT_FROM_MELEE) == 1;
		boolean protectMissilesOn = client.getVarbitValue(Varbits.PRAYER_PROTECT_FROM_MISSILES) == 1;
		return protectMagicOn || protectMeleeOn || protectMissilesOn;
	}
}
