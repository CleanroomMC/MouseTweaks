package yalter.mousetweaks.impl.mixin;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author ZZZank
 */
@Mixin(GuiContainer.class)
public interface AccessGuiContainer {

    @Invoker
    void invokeHandleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type);

    @Invoker
    Slot invokeGetSlotAtPosition(int x, int y);

    @Accessor
    void setIgnoreMouseUp(boolean value);

    @Accessor
    boolean getDragSplitting();

    @Accessor
    int getDragSplittingButton();
}
