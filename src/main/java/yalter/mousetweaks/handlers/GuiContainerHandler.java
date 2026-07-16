package yalter.mousetweaks.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
import yalter.mousetweaks.MouseTweaks;
import yalter.mousetweaks.impl.mixin.AccessGuiContainer;
import yalter.mousetweaks.impl.IGuiScreenHandler;
import yalter.mousetweaks.impl.MouseButton;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;
import yalter.mousetweaks.api.MouseTweaksIgnore;

import java.util.List;

public class GuiContainerHandler implements IGuiScreenHandler {
    protected Minecraft mc;
    protected GuiContainer guiContainer;

    public GuiContainerHandler(GuiContainer guiContainer) {
        this.mc = Minecraft.getMinecraft();
        this.guiContainer = guiContainer;
    }

    private int getDisplayWidth() {
        return mc.displayWidth;
    }

    private int getDisplayHeight() {
        return mc.displayHeight;
    }

    private int getRequiredMouseX() {
        return (Mouse.getX() * guiContainer.width) / getDisplayWidth();
    }

    private int getRequiredMouseY() {
        return guiContainer.height - ((Mouse.getY() * guiContainer.height) / getDisplayHeight()) - 1;
    }

    @Override
    public boolean isMouseTweaksDisabled() {
        return guiContainer.getClass().isAnnotationPresent(MouseTweaksIgnore.class)
                || !(guiContainer instanceof AccessGuiContainer)
                || MouseTweaks.instance.isMouseTweakDisabled(guiContainer.getClass());
    }

    @Override
    public boolean isWheelTweakDisabled() {
        return guiContainer.getClass().isAnnotationPresent(MouseTweaksDisableWheelTweak.class)
                || MouseTweaks.instance.isWheelTweakDisabled(guiContainer.getClass());
    }

    @Override
    public List<Slot> getSlots() {
        return guiContainer.inventorySlots.inventorySlots;
    }

    @Override
    public Slot getSlotUnderMouse() {
        return ((AccessGuiContainer) guiContainer).invokeGetSlotAtPosition(getRequiredMouseX(), getRequiredMouseY());
    }

    @Override
    public boolean disableRMBDraggingFunctionality() {
        var access = (AccessGuiContainer) guiContainer;

        access.setIgnoreMouseUp(true);

        if (access.getDragSplitting()) {
            if (access.getDragSplittingButton() == 1) {
                access.setIgnoreMouseUp(false);
                return true;
            }
        }

        return false;
    }

    @Override
    public void clickSlot(Slot slot, MouseButton mouseButton, boolean shiftPressed) {
        ((AccessGuiContainer) this.guiContainer).invokeHandleMouseClick(
                slot,
                slot.slotNumber,
                mouseButton.getValue(),
                shiftPressed ? ClickType.QUICK_MOVE : ClickType.PICKUP);
    }

    @Override
    public boolean isCraftingOutput(Slot slot) {
        return (slot instanceof SlotCrafting
                || slot instanceof SlotFurnaceOutput
                || slot instanceof SlotMerchantResult
                || (guiContainer.inventorySlots instanceof ContainerRepair && slot.slotNumber == 2));
    }

    @Override
    public boolean isIgnored(Slot slot) {
        return false;
    }

    @Override
    public int isSlotPrioritized(Slot slot, ItemStack stack) {
        if (stack.getItem().getItemBurnTime(stack) != 0) {
            if (slot instanceof SlotFurnaceFuel)
                return 1;
            return 0;
        }
        return -1;
    }
}
