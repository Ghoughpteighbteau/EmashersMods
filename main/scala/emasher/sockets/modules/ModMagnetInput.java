/* Decompiled through IntelliJad */
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packfields(3) packimports(3) splitstr(64) radix(10) lradix(10) 
// Source File Name:   ModMagnetInput.java

package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

// Referenced classes of package emasher.sockets.modules:
//            ModMagnet

public class ModMagnetInput extends ModMagnet {

    public ModMagnetInput(int id) {
        super(id, "sockets:magnetInput");
    }

    public String getLocalizedName() {
        return "Magnet Input";
    }

    public void getToolTip(List l) {
        l.add("Combined Magnet, Fluid, Item, and Energy input modules");
    }

    public void getIndicatorKey(List l) {
        l.add((new StringBuilder()).append(SocketsMod.PREF_BLUE).append("Tank to input to").toString());
        l.add((new StringBuilder()).append(SocketsMod.PREF_GREEN).append("Inventory to input to").toString());
        l.add((new StringBuilder()).append(SocketsMod.PREF_RED).append("Magnet Enabled").toString());
        l.add((new StringBuilder()).append(SocketsMod.PREF_DARK_PURPLE).append("Magnet Enabled").toString());
    }

    public void addRecipe() {
        GameRegistry.addShapelessRecipe(new ItemStack(SocketsMod.module, 1, moduleID), new Object[]{
                new ItemStack(SocketsMod.module, 1, 9), new ItemStack(SocketsMod.module, 1, 47)
        });
    }

    public boolean hasTankIndicator() {
        return true;
    }

    public boolean hasInventoryIndicator() {
        return true;
    }

    public boolean hasRSIndicator() {
        return true;
    }

    public boolean hasLatchIndicator() {
        return true;
    }

    public boolean isEnergyInterface(SideConfig config) {
        return true;
    }

    public int receiveEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts) {
        return ts.addEnergy(amount, simulate);
    }

    public boolean isFluidInterface() {
        return true;
    }

    public boolean canInsertFluid() {
        return true;
    }

    public int fluidFill(FluidStack fluid, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side) {
        if (config.tank != -1)
            return ts.fillInternal(config.tank, fluid, doFill);
        else
            return 0;
    }

    public boolean isItemInterface() {
        return true;
    }

    public boolean canInsertItems() {
        return true;
    }

    public boolean canDirectlyInsertItems(SideConfig config, SocketTileAccess ts) {
        return config.inventory >= 0 && config.inventory <= 2;
    }

    public int itemFill(ItemStack item, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side) {
        if (config.inventory != -1)
            return ts.addItemInternal(item, doFill, config.inventory);
        else
            return 0;
    }
}
