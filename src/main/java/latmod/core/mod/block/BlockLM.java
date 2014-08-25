package latmod.core.mod.block;
import java.util.*;

import latmod.core.mod.LMMod;
import latmod.core.mod.tile.TileLM;
import latmod.core.util.FastList;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.*;

public abstract class BlockLM extends BlockContainer
{
	public final String blockName;
	public ArrayList<ItemStack> blocksAdded = new ArrayList<ItemStack>();
	public final LMMod mod;
	
	public BlockLM(LMMod bm, String s, Material m)
	{
		super(m);
		mod = bm;
		blockName = s;
		setBlockName(mod.getBlockName(s));
		setHardness(1.8F);
		setResistance(3F);
		isBlockContainer = false;
	}
	
	@SideOnly(Side.CLIENT)
	public abstract CreativeTabs getCreativeTabToDisplayOn();
	public abstract TileLM createNewTileEntity(World w, int m);
	
	public void onPostLoaded()
	{ blocksAdded.add(new ItemStack(this)); }
	
	public int damageDropped(int i)
	{ return i; }
	
	public boolean hasTileEntity(int m)
	{ return isBlockContainer; }
	
	public String getUnlocalizedName(int m)
	{ return mod.getBlockName(blockName); }
	
	public void addAllDamages(int until)
	{
		for(int i = 0; i < until; i++)
		blocksAdded.add(new ItemStack(this, 1, i));
	}
	
	@SuppressWarnings("all")
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item j, CreativeTabs c, List l)
	{ l.addAll(blocksAdded); }

	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase el, ItemStack is)
	{
		if(isBlockContainer && el instanceof EntityPlayer)
		{
			TileLM tile = (TileLM) w.getTileEntity(x, y, z);
			if(tile != null) tile.onPlacedBy((EntityPlayer)el, is);
		}
	}
	
	public float getPlayerRelativeBlockHardness(EntityPlayer ep, World w, int x, int y, int z)
	{
		if(isBlockContainer)
		{
			TileLM tile = (TileLM) w.getTileEntity(x, y, z);
			if(tile != null && !tile.isMinable(ep)) return -1F;
		}
		
		return super.getPlayerRelativeBlockHardness(ep, w, x, y, z);
	}
	
	public float getBlockHardness(World w, int x, int y, int z)
	{
		if(isBlockContainer)
		{
			TileLM tile = (TileLM) w.getTileEntity(x, y, z);
			if(tile != null && !tile.isMinable(null)) return -1F;
		}
		
		return super.getBlockHardness(w, x, y, z);
	}
	
	public float getExplosionResistance(Entity e, World w, int x, int y, int z, double ex, double ey, double ez)
	{
		if(isBlockContainer)
		{
			TileLM tile = (TileLM) w.getTileEntity(x, y, z);
			if(tile != null && tile.isExplosionResistant()) return 1000000F;
		}
		
		return super.getExplosionResistance(e, w, x, y, z, ex, ey, ez);
	}
	
	public int getMobilityFlag()
	{ return isBlockContainer ? 2 : 0; }
	
	public void breakBlock(World w, int x, int y, int z, Block b, int m)
	{
		if(!w.isRemote && isBlockContainer)
		{ TileLM tile = (TileLM) w.getTileEntity(x, y, z);
		if(tile != null) tile.onBroken(); }
		super.breakBlock(w, x, y, z, b, m);
	}

	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer ep, int s, float x1, float y1, float z1)
	{
		if(!isBlockContainer) return false;
		TileLM tile = (TileLM) w.getTileEntity(x, y, z);
		return (tile != null) ? tile.onRightClick(ep, ep.getHeldItem(), s, x1, y1, z1) : false;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir)
	{ blockIcon = ir.registerIcon(mod.assets + blockName); }

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int s, int m)
	{ return blockIcon; }

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int s)
	{ return getIcon(s, iba.getBlockMetadata(x, y, z)); }

	public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
	{ return null; }
	
	public boolean rotateBlock(World w, int x, int y, int z, ForgeDirection side)
	{ return false; }

	public boolean onBlockEventReceived(World w, int x, int y, int z, int eventID, int param)
	{
		if(isBlockContainer)
		{
			TileLM t = (TileLM) w.getTileEntity(x, y, z);
			if(t != null) return t.receiveClientEvent(eventID, param);
		}
		return false;
	}

	public boolean recolourBlock(World w, int x, int y, int z, ForgeDirection side, int col)
	{
		if(isBlockContainer)
		{
			TileLM t = (TileLM) w.getTileEntity(x, y, z);
			if(t != null) return t.recolourBlock(side, col);
		}
		return false;
	}
	
	public void loadRecipes()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void addInfo(ItemStack is, EntityPlayer ep, FastList<String> l)
	{
	}
	
	public void onNeighborBlockChange(World w, int x, int y, int z, Block b)
	{
		if(isBlockContainer)
		{
			TileLM t = (TileLM) w.getTileEntity(x, y, z);
			if(t != null) t.onNeighborBlockChange();
		}
	}
	
	public final Item getItem()
	{ return Item.getItemFromBlock(this); }
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z)
	{ setBlockBoundsBasedOnState(w, x, y, z); return super.getCollisionBoundingBoxFromPool(w, x, y, z); }
	
	@SideOnly(Side.CLIENT)
	public IIcon getBlockIcon() { return blockIcon; }
}