package yalter.mousetweaks;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import moze_intel.projecte.gameObjs.gui.GUICondenser;
import moze_intel.projecte.gameObjs.gui.GUICondenserMK2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import thelm.packagedauto.client.gui.GuiEncoder;
import yalter.mousetweaks.util.Constants;
import yalter.mousetweaks.util.MTLog;

import java.util.Set;

@Mod(   modid = Constants.MOD_ID, // If this isn't here, the mod doesn't load.
        version = Constants.VERSION, // If this isn't here, FML complains in the log.
        useMetadata = true, // The rest of stuff is fine being exclusively in mcmod.info.
        clientSideOnly = true,
        dependencies = "required:mixinbooter@[11.0,)")
public class MouseTweaks {

    @Mod.Instance
    public static MouseTweaks instance;

    private final Set<Class<? extends GuiScreen>> mouseTweaksBlacklist = new ObjectOpenHashSet<>();
    private final Set<Class<? extends GuiScreen>> wheelTweaksBlacklist = new ObjectOpenHashSet<>();

    public void addMouseTweakBlacklist(Class<? extends GuiScreen> clazz) {
        this.mouseTweaksBlacklist.add(clazz);
    }

    public void addWheelTweaksBlacklist(Class<? extends GuiScreen> clazz) {
        this.wheelTweaksBlacklist.add(clazz);
    }

    public boolean isMouseTweakDisabled(Class<? extends GuiScreen> clazz) {
        return this.mouseTweaksBlacklist.contains(clazz);
    }

    public boolean isWheelTweakDisabled(Class<? extends GuiScreen> clazz) {
        return this.wheelTweaksBlacklist.contains(clazz);
    }

    @EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MTLog.init(event.getModLog());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (event.getSide() != Side.CLIENT) {
            MTLog.logger.info("MouseTweaks disabled because we are not running on the client");
            return;
        }
        if (Loader.isModLoaded("projecte")) {
            mouseTweaksBlacklist.add(GUICondenser.class);
            wheelTweaksBlacklist.add(GUICondenser.class);
            mouseTweaksBlacklist.add(GUICondenserMK2.class);
            wheelTweaksBlacklist.add(GUICondenserMK2.class);
        }
        if (Loader.isModLoaded("packagedauto")) {
            mouseTweaksBlacklist.add(GuiEncoder.class);
            wheelTweaksBlacklist.add(GuiEncoder.class);
        }
        Main.initialize();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Main.onUpdateInGame();
        }
    }

    @SubscribeEvent
    public void onGuiMouseInput(GuiScreenEvent.MouseInputEvent.Post event) {
        if (event.getGui() instanceof GuiContainer) {
            Main.onMouseInput();
        }
    }

    @SubscribeEvent
    public void syncConfigValues(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Constants.MOD_ID)) {
            ConfigManager.sync(Constants.MOD_ID, Type.INSTANCE);
        }
    }
}
