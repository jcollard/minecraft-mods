package items;

import net.minecraft.item.Item.Properties;
import net.minecraft.item.Food;
import net.minecraft.item.Food.Builder;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import utils.quickitem.QuickItem;

public class BambooOfRegeneration extends QuickItem {
	
	{
		itemName = "Bamboo of Regeneration";
		texture = "bamboo";
	}

	@Override
	public void initializeProperties(Properties properties) {
		// Added this bamboo to the Food menu
		properties.group(ItemGroup.FOOD);
		
		// Creates a new Food Builder
		Food.Builder foodBuilder = new Food.Builder();
		
		// Create a Regeneration effect
		EffectInstance regeneration;
		regeneration = new EffectInstance(Effects.REGENERATION, 400);
		
		// Add the regeneration effect to our Food Builder
		foodBuilder.effect(regeneration, 1.0F);
		
		/// Add this food type to our item
		properties.food(foodBuilder.build());
		
	}
	
	
	

}