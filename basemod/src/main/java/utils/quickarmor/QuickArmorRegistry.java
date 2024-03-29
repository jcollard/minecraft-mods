package utils.quickarmor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Supplier;

import info.BaseMod;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class QuickArmorRegistry {

	public static final DeferredRegister<Item> ARMOR = new DeferredRegister<>(ForgeRegistries.ITEMS, BaseMod.MODID);
	
	public static void init(IEventBus bus) throws IOException {
		for(QuickArmor armor : QuickArmor.getAll()) {
			for(ArmorRegistryItem item : QuickArmorBuilder.build(armor)) {
				ARMOR.register(item.name, item.supplier);
			}
		}
		ARMOR.register(bus);
	}
	
	private static class ArmorRegistryItem {
		private final Supplier<Item> supplier;
		private final String name;
		private ArmorRegistryItem(Supplier<Item> supplier, String name) {
			this.supplier = supplier;
			this.name = name;
		}
	}
	
	public static class QuickArmorBuilder extends Item {

		public static List<ArmorRegistryItem> build(QuickArmor armor) {
			Properties p = new Item.Properties();
			p.group(ItemGroup.COMBAT);
			armor.initializeProperties(p);
			
			List<ArmorRegistryItem> items = new LinkedList<>();
			//TODO: Probably should normalize these strings throughout the code to variables
			Supplier<Item> s = () -> new ArmorItem(armor.getMaterial(), EquipmentSlotType.CHEST, p); 
			items.add(new ArmorRegistryItem(s, armor.getSafeRegistryName() + "_chestplate"));
			s =() -> new ArmorItem(armor.getMaterial(), EquipmentSlotType.LEGS, p);
			items.add(new ArmorRegistryItem(s, armor.getSafeRegistryName() + "_leggings"));
			s = () -> new ArmorItem(armor.getMaterial(), EquipmentSlotType.FEET, p);
			items.add(new ArmorRegistryItem(s, armor.getSafeRegistryName() + "_boots"));
			s = () -> new ArmorItem(armor.getMaterial(), EquipmentSlotType.HEAD, p);
			items.add(new ArmorRegistryItem(s, armor.getSafeRegistryName() + "_helmet"));
			return items;
		}
		
		
		public QuickArmorBuilder(QuickArmor armor, Properties properties) {
			super(properties);
		}
		
	}
}
