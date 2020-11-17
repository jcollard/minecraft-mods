package utils.quickitem;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.google.common.base.Supplier;

import info.BaseMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class QuickItemRegistry {
	
	private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, BaseMod.MODID);
	
	public static void init() throws IOException {
		for(QuickItem item : QuickItem.getAllItems()) {
			ITEMS.register(item.getSafeRegistryName(), QuickItemBuilder.build(item));
		}
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
		
	public static class QuickItemBuilder extends Item {
		
		private final QuickItem quickItem;

		public static Supplier<QuickItemBuilder> build(QuickItem item) {
			Properties p = new Item.Properties();
			item.initializeProperties(p);
			return () -> new QuickItemBuilder(item, p);
		}

		public QuickItemBuilder(QuickItem item, Properties p) {
			super(p);
			this.quickItem = item;
			this.quickItem.getSafeRegistryName();
			this.quickItem.setItem(this);
			
		}

		@Override
		public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
			quickItem.onUse(worldIn, livingEntityIn, stack, count);
		}
		
		public void _onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
			super.onUse(worldIn, livingEntityIn, stack, count);
		}

		@Override
		public IItemPropertyGetter getPropertyGetter(ResourceLocation key) {
			return quickItem.getPropertyGetter(key);
		}
		
		public IItemPropertyGetter _getPropertyGetter(ResourceLocation key) {
			return super.getPropertyGetter(key);
		}

		@Override
		public boolean hasCustomProperties() {
			return quickItem.hasCustomProperties();
		}
		
		public boolean _hasCustomProperties() {
			return super.hasCustomProperties();
		}

		@Override
		public boolean updateItemStackNBT(CompoundNBT nbt) {
			return quickItem.updateItemStackNBT(nbt);
		}
		
		public boolean _updateItemStackNBT(CompoundNBT nbt) {
			return super.updateItemStackNBT(nbt);
		}

		@Override
		public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos,
				PlayerEntity player) {
			return quickItem.canPlayerBreakBlockWhileHolding(state, worldIn, pos, player);
		}
		
		public boolean _canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos,
				PlayerEntity player) {
			return super.canPlayerBreakBlockWhileHolding(state, worldIn, pos, player);
		}

		@Override
		public Item asItem() {
			return quickItem.asItem();
		}
		
		public Item _asItem() {
			return super.asItem();
		}

		@Override
		public float getDestroySpeed(ItemStack stack, BlockState state) {
			return quickItem.getDestroySpeed(stack, state);
		}
		
		public float _getDestroySpeed(ItemStack stack, BlockState state) {
			return super.getDestroySpeed(stack, state);
		}

		@Override
		public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
			quickItem.world = worldIn;
			quickItem.onRightClick(playerIn, handIn);
			return quickItem.onItemRightClick(worldIn, playerIn, handIn);
		}
		
		public ActionResult<ItemStack> _onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}

		@Override
		public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
			return quickItem.onItemUseFinish(stack, worldIn, entityLiving);
		}
		
		public ItemStack _onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
			return super.onItemUseFinish(stack, worldIn, entityLiving);
		}

		@Override
		public boolean isDamageable() {
			return quickItem.isDamageable();
		}
		
		public boolean _isDamageable() {
			return super.isDamageable();
		}

		@Override
		public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
			return quickItem.hitEntity(stack, target, attacker);
		}
		
		   /**
		    * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
		    * the damage on the stack.
		    */		
		public boolean _hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
			return super.hitEntity(stack, target, attacker);
		}

		@Override
		public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
				LivingEntity entityLiving) {
			return quickItem.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}
		
		public boolean _onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
				LivingEntity entityLiving) {
			return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		}

		@Override
		public boolean canHarvestBlock(BlockState blockIn) {
			return quickItem.canHarvestBlock(blockIn);
		}
		
		public boolean _canHarvestBlock(BlockState blockIn) {
			return super.canHarvestBlock(blockIn);
		}

		@Override
		public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target,
				Hand hand) {
			return quickItem.itemInteractionForEntity(stack, playerIn, target, hand);
		}
		
		public boolean _itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target,
				Hand hand) {
			return super.itemInteractionForEntity(stack, playerIn, target, hand);
		}

		@Override
		public ITextComponent getName() {
			return quickItem.getName();
		}
		
		public ITextComponent _getName() {
			return super.getName();
		}

		@Override
		protected String getDefaultTranslationKey() {
			return quickItem.getDefaultTranslationKey();
		}
		
		protected String _getDefaultTranslationKey() {
			return super.getDefaultTranslationKey();
		}

		@Override
		public String getTranslationKey() {
			return quickItem.getTranslationKey();
		}
		
		public String _getTranslationKey() {
			return super.getTranslationKey();
		}

		@Override
		public String getTranslationKey(ItemStack stack) {
			return quickItem.getTranslationKey(stack);
		}
		
		public String _getTranslationKey(ItemStack stack) {
			return super.getTranslationKey(stack);
		}

		@Override
		public boolean shouldSyncTag() {
			return quickItem.shouldSyncTag();
		}
		
		public boolean _shouldSyncTag() {
			return super.shouldSyncTag();
		}


		@Override
		public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
			quickItem.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		}
		
		public void _inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
			super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		}

		@Override
		public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
			quickItem.onCreated(stack, worldIn, playerIn);
		}
		
		public void _onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
			super.onCreated(stack, worldIn, playerIn);
		}

		@Override
		public boolean isComplex() {
			return quickItem.isComplex();
		}
		
		public boolean _isComplex() {
			return super.isComplex();
		}

		@Override
		public UseAction getUseAction(ItemStack stack) {
			return quickItem.getUseAction(stack);
		}
		
		public UseAction _getUseAction(ItemStack stack) {
			return super.getUseAction(stack);
		}

		@Override
		public int getUseDuration(ItemStack stack) {
			return quickItem.getUseDuration(stack);
		}
		
		public int _getUseDuration(ItemStack stack) {
			return super.getUseDuration(stack);
		}

		@Override
		public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
			quickItem.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		}
		
		public void _onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
			super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		}

		@Override
		public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
			quickItem.addInformation(stack, worldIn, tooltip, flagIn);
		}
		
		public void _addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
			super.addInformation(stack, worldIn, tooltip, flagIn);
		}

		@Override
		public ITextComponent getDisplayName(ItemStack stack) {
			return quickItem.getDisplayName(stack);
		}
		
		public ITextComponent _getDisplayName(ItemStack stack) {
			return super.getDisplayName(stack);
		}

		@Override
		public boolean hasEffect(ItemStack stack) {
			return quickItem.hasEffect(stack);
		}
		
		public boolean _hasEffect(ItemStack stack) {
			return super.hasEffect(stack);
		}

		@Override
		public Rarity getRarity(ItemStack stack) {
			return quickItem.getRarity(stack);
		}
		
		public Rarity _getRarity(ItemStack stack) {
			return super.getRarity(stack);
		}

		@Override
		public boolean isEnchantable(ItemStack stack) {
			return quickItem.isEnchantable(stack);
		}
		
		public boolean _isEnchantable(ItemStack stack) {
			return super.isEnchantable(stack);
		}

		@Override
		public int getItemEnchantability() {
			return quickItem.getItemEnchantability();
		}
		
		public int _getItemEnchantability() {
			return super.getItemEnchantability();
		}

		@Override
		public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
			quickItem.fillItemGroup(group, items);
		}
		
		public void _fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
			super.fillItemGroup(group, items);
		}

		@Override
		protected boolean isInGroup(ItemGroup group) {
			return quickItem.isInGroup(group);
		}
		
		protected boolean _isInGroup(ItemGroup group) {
			return super.isInGroup(group);
		}

		@Override
		public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
			return quickItem.getIsRepairable(toRepair, repair);
		}
		
		public boolean _getIsRepairable(ItemStack toRepair, ItemStack repair) {
			return super.getIsRepairable(toRepair, repair);
		}

		@Override
		public boolean isRepairable(ItemStack stack) {
			return quickItem.isRepairable(stack);
		}
		
		public boolean _isRepairable(ItemStack stack) {
			return super.isRepairable(stack);
		}

		@Override
		public Set<ToolType> getToolTypes(ItemStack stack) {
			return quickItem.getToolTypes(stack);
		}
		
		public Set<ToolType> _getToolTypes(ItemStack stack) {
			return super.getToolTypes(stack);
		}

		@Override
		public int getHarvestLevel(ItemStack stack, ToolType tool, PlayerEntity player, BlockState blockState) {
			return quickItem.getHarvestLevel(stack, tool, player, blockState);
		}
		
		public int _getHarvestLevel(ItemStack stack, ToolType tool, PlayerEntity player, BlockState blockState) {
			return super.getHarvestLevel(stack, tool, player, blockState);
		}

		@Override
		public Set<ResourceLocation> getTags() {
			return quickItem.getTags();
		}
		
		public Set<ResourceLocation> _getTags() {
			return super.getTags();
		}

		@Override
		public boolean isCrossbow(ItemStack stack) {
			return quickItem.isCrossbow(stack);
		}
		
		public boolean _isCrossbow(ItemStack stack) {
			return super.isCrossbow(stack);
		}

		@Override
		public ItemStack getDefaultInstance() {
			return quickItem.getDefaultInstance();
		}
		
		public ItemStack _getDefaultInstance() {
			return super.getDefaultInstance();
		}

		@Override
		public boolean isIn(Tag<Item> tagIn) {
			return quickItem.isIn(tagIn);
		}
		
		public boolean _isIn(Tag<Item> tagIn) {
			return super.isIn(tagIn);
		}

		@Override
		public boolean isFood() {
			return quickItem.isFood();
		}
		
		public boolean _isFood() {
			return super.isFood();
		}

		@Override
		public Food getFood() {
			return quickItem.getFood();
		}
		
		public Food _getFood() {
			return super.getFood();
		}

		@Override
		public SoundEvent getDrinkSound() {
			return quickItem.getDrinkSound();
		}
		
		public SoundEvent _getDrinkSound() {
			return super.getDrinkSound();
		}

		@Override
		public SoundEvent getEatSound() {
			return super.getEatSound();
		}
		
		public SoundEvent _getEatSound() {
			return super.getEatSound();
		}

		@Override
		public ActionResultType onItemUse(ItemUseContext context) {
			quickItem.world = context.getWorld();
			quickItem.onUse(context);
			return quickItem.onItemUse(context);
		}
		
		public ActionResultType _onItemUse(ItemUseContext context) {
			return super.onItemUse(context);
		}
		
		
	}

	
}

