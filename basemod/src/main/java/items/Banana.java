package items;

import net.minecraft.item.Food;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
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
		
		// Instantiate a Food.Builder
		Food.Builder foodBuilder = new Food.Builder();
		// Specify how much saturation this food provides
		foodBuilder.saturation(0.3F);
		// Specify how much hunger this food provides
		foodBuilder.hunger(4);
		// Create a resistance EffectInstance
		EffectInstance resistance = new EffectInstance(Effects.RESISTANCE, 1600);
		// Add the resistance EffectInstance to our food and make it happen 100% of the time
		foodBuilder.effect(resistance, 1.0F);
		// Create a regeneration EffectInstance
		EffectInstance regeneration = new EffectInstance(Effects.REGENERATION, 400);
		// Add the regeneration effect to our food and make it happen 50% of the time
		foodBuilder.effect(regeneration, 0.5F);
		// Specify the effects of eating this item
		properties.food(foodBuilder.build());
	}
	
	
	
}
