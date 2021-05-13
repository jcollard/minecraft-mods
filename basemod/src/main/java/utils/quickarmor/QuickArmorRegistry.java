package utils.quickarmor;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Supplier;

import info.BaseMod;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class QuickArmorRegistry {

	public static final DeferredRegister<Item> ARMOR = new DeferredRegister<>(ForgeRegistries.ITEMS, BaseMod.MODID);
	
	public static void init(IEventBus bus) throws IOException {
		for(QuickArmor armor : QuickArmor.getAllArmor()) {
			for(Supplier<Item> item : QuickArmorBuilder.build(armor)) {
				ARMOR.register(armor.getSafeRegistryName(), QuickArmorBuilder.build(item));
			}
		}
		ARMOR.register(bus);
	}
	
	public static class QuickArmorBuilder extends Item {

		public static List<Supplier<Item>> build(QuickArmor armor) {
			Properties p = new Item.Properties();
			armor.initializeProperties(p);
			return () -> new ArmorItem(armor.getMaterial(), null, p);
		}
		
		
		public QuickArmorBuilder(QuickArmor armor, Properties properties) {
			super(properties);
		}
		
	}
}
