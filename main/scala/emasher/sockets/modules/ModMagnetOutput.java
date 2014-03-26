/* Decompiled through IntelliJad */
// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packfields(3) packimports(3) splitstr(64) radix(10) lradix(10) 
// Source File Name:   ModMagnetOutput.java

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

public class ModMagnetOutput extends ModMagnet {

    public ModMagnetOutput(int id) {
        super(id, "sockets:magnetOutput");
    }

    public String getLocalizedName() {
        return "Magnet Output";
    }

    public void getToolTip(List l) {
        l.add("Combined Magnet, Fluid, Item, and Energy output modules");
    }

    public void getIndicatorKey(List l) {
        l.add((new StringBuilder()).append(SocketsMod.PREF_BLUE).append("Tank to output from").toString());
        l.add((new StringBuilder()).append(SocketsMod.PREF_GREEN).append("Inventory to output from").toString());
        l.add((new StringBuilder()).append(SocketsMod.PREF_RED).append("Magnet Enabled").toString());
        l.add((new StringBuilder()).append(SocketsMod.PREF_DARK_PURPLE).append("Magnet Enabled").toString());
    }

    public void addRecipe() {
        GameRegistry.addShapelessRecipe(new ItemStack(SocketsMod.module, 1, moduleID), new Object[]{
                new ItemStack(SocketsMod.module, 1, 10), new ItemStack(SocketsMod.module, 1, 47)
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

    public boolean isFluidInterface() {
        return true;
    }

    public boolean canExtractFluid() {
        return true;
    }

    public boolean isItemInterface() {
        return true;
    }

    public boolean canExtractItems() {
        return true;
    }

    public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side) {
        EnergyInsert(ts, config, side);
        FluidInsert(ts, config, side);
        ItemInsert(ts, config, side);
    }

    private void EnergyInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side) {
        ts.outputEnergy(1000, side);
    }

    private void FluidInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side) {
        if (config.tank < 0 || config.tank > 2) {
            return;
        } else {
            ts.tryInsertFluid(config.tank, side);
            return;
        }
    }

    private void ItemInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side) {
        if (config.inventory < 0 || config.inventory > 2)
            return;
        if (ts.tryInsertItem(ts.getStackInInventorySlot(config.inventory), side))
            ts.extractItemInternal(true, config.inventory, 1);
    }

    public FluidStack fluidExtract(int amount, boolean doExtract, SideConfig config, SocketTileAccess ts) {
        if (config.tank != -1)
            return ts.drainInternal(config.tank, amount, doExtract);
        else
            return null;
    }

    public ItemStack itemExtract(int amount, boolean doExtract, SideConfig config, SocketTileAccess ts) {
        if (config.inventory != -1)
            return ts.extractItemInternal(doExtract, config.inventory, amount);
        else
            return null;
    }

    public int extractEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts) {
        return ts.useEnergy(amount, simulate);
    }

    public boolean canDirectlyExtractItems(SideConfig config, SocketTileAccess ts) {
        return config.inventory >= 0 && config.inventory <= 2;
    }
}
