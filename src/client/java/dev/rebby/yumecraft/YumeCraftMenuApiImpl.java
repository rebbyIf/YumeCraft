package dev.rebby.yumecraft;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.rebby.yumecraft.gui.ModConfigScreen;

public class YumeCraftMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new ModConfigScreen();
    }
}
