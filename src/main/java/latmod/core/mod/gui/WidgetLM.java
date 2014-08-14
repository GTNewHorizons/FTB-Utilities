package latmod.core.mod.gui;
import latmod.core.mod.gui.GuiLM;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class WidgetLM
{
	public final GuiLM gui;
	public final int posX, posY, width, height;
	
	public WidgetLM(GuiLM g, int x, int y, int w, int h)
	{
		gui = g;
		posX = x;
		posY = y;
		width = w;
		height = h;
	}
	
	public boolean isAt(int x, int y)
	{ return x >= posX && y >= posY && x <= posX + width && y <= posY + height; }
	
	public boolean mouseOver(int mx, int my)
	{ return isAt(mx - gui.getPosX(), my - gui.getPosY()); }
	
	public void render(int tx, int ty, double rw, double rh)
	{ gui.drawTexturedModalRect(gui.getPosX() + posX, gui.getPosY() + posY, tx, ty, (int)(width * rw), (int)(height * rh)); }
	
	public void render(int tx, int ty)
	{ render(tx, ty, 1D, 1D); }
	
	public boolean mousePressed(int mx, int my, int b)
	{
		return false;
	}
	
	public void voidMousePressed(int mx, int my, int b)
	{
	}
	
	public boolean keyPressed(int key, char keyChar)
	{
		return false;
	}
}