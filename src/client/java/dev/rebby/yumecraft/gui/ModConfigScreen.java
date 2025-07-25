package dev.rebby.yumecraft.gui;

import dev.rebby.yumecraft.YumeCraft;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ModConfigScreen extends BaseOwoScreen<FlowLayout> {

    public List<Pair<Identifier, Integer>> localConfigList = new ArrayList<>();
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        createLocalConfigList();
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    private void createLocalConfigList(){
        localConfigList.clear();
        for (Identifier id : YumeCraft.CONFIG.sleepingTeleportation().keySet()) {
            localConfigList.add(new Pair<>(id, YumeCraft.CONFIG.sleepingTeleportation().get(id)));
        }
    }

    private void saveLocalConfigList(){
        Map<Identifier, Integer> newMap = new HashMap<>();
        for (Pair<Identifier, Integer> pair : localConfigList) {
            newMap.put(pair.getLeft(), pair.getRight());
        }

        YumeCraft.CONFIG.sleepingTeleportation(newMap);
    }



    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .alignment(HorizontalAlignment.CENTER, VerticalAlignment.TOP);

        FlowLayout centralComponent = Containers.verticalFlow(Sizing.fill(90), Sizing.content());

        centralComponent.padding(Insets.of(10));

        centralComponent.child(Containers.horizontalFlow(Sizing.fill(90), Sizing.content())
                .child(Components.button(Text.of("Save"),
                        buttonComponent ->
                                saveLocalConfigList()))
        .child(Components.button(Text.of("Close"),
                buttonComponent -> this.close())));


        FlowLayout dimensionList = Containers.verticalFlow(Sizing.fill(90), Sizing.content());

        for (Pair<Identifier, Integer> pair : localConfigList) {
            dimensionList.child(getDimensionComponent(pair));
        }

//        FlowLayout list = Components.list(localConfigList,
//                flowLayout -> flowLayout.alignment(HorizontalAlignment.LEFT, VerticalAlignment.TOP),
//                ModConfigScreen::getDimensionComponent, false);

        centralComponent.child(dimensionList);

        centralComponent.child(Components.button(Text.literal("+"), buttonComponent ->
        {
            Pair<Identifier, Integer> pair = new Pair<>(Identifier.ofVanilla("empty"), 0);
            localConfigList.addLast(pair);
            dimensionList.child(getDimensionComponent(pair));
        }));

        ScrollContainer<FlowLayout> scrollContainer = Containers.verticalScroll(Sizing.fill(90), Sizing.fill(100), centralComponent);

        rootComponent.child(scrollContainer);
    }

    private Component getDimensionComponent(Pair<Identifier, Integer> pair) {
        FlowLayout container = Containers.horizontalFlow(Sizing.fill(90), Sizing.content());

        TextBoxComponent idTextBox = Components.textBox(Sizing.fill(40), pair.getLeft().toString());
        idTextBox.onChanged().subscribe(value -> pair.setLeft(Identifier.of(value)));

        TextBoxComponent numTextBox = Components.textBox(Sizing.fill(40), Integer.toString(pair.getRight()));
        numTextBox.onChanged().subscribe(value -> {
            try {
                pair.setRight(Math.max(Integer.parseInt(value), 0));
                if (pair.getRight() == 0) {
                    numTextBox.text("0");
                }
            } catch (NumberFormatException e) {
                pair.setRight(0);
                numTextBox.text("0");
            }
        });

        return container.child(
                Components.button(Text.literal("-"), buttonComponent -> {
                    localConfigList.remove(pair);
                    container.remove();
                }))
                .child(idTextBox)
                .child(numTextBox);
    }


}
