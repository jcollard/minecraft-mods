package items;

import net.minecraft.item.Foods;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item.Properties;
import utils.quickitem.QuickItem;

public class Banana extends QuickItem {
	
	{
		// Specify which texture to use. This looks for assets/textures/banana.png
		texture = "banana";
		// Specify the name that will appear for this item
		itemName = "Banana of Greatness";
	}

	@Override
	public void initializeProperties(Properties properties) {
		// Specify which tab this item will appear in
		properties.group(ItemGroup.FOOD);
		
		// Specify the effects of eating this item
		properties.food(Foods.ENCHANTED_GOLDEN_APPLE);
	}
	
	
	
}
