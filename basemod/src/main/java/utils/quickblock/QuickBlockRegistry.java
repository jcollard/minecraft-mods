package utils.quickblock;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.base.Supplier;

import info.BaseMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import utils.quickitem.QuickItemRegistry;

public class QuickBlockRegistry {

	private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, BaseMod.MODID);

	public static void init(IEventBus bus) throws IOException {
		for (QuickBlock block : QuickBlock.getAllBlocks()) {
			RegistryObject<Block> ro = BLOCKS.register(block.getSafeRegistryName(), QuickBlockBuilder.build(block));
			QuickItemRegistry.ITEMS.register(block.getSafeRegistryName(),
					() -> new BlockItem(ro.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
			// BLOCKS.register(bus);
		}
		BLOCKS.register(bus);
	}

	public static class QuickBlockBuilder extends Block {

		private final QuickBlock quickBlock;

		public static Supplier<QuickBlockBuilder> build(QuickBlock block) {
			Properties p = Block.Properties.create(block.getDefaultMaterial(), block.getDefaultDyeColor());
			block.initializeProperties(p);
			return () -> new QuickBlockBuilder(block, p);
		}

		private QuickBlockBuilder(QuickBlock block, Properties p) {
			super(p);
			if (block == null) {
				throw new NullPointerException("Block must be non-null");
			}
			System.out.println("Creating QuickBlockBulder(" + block.blockName + ")");
			this.quickBlock = block;
			this.quickBlock.getSafeRegistryName();
			this.quickBlock.setDelegate(this);
		}

		@Override
		public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
			return this.quickBlock.canEntitySpawn(state, worldIn, pos, type);
		}

		@Override
		public boolean isAir(BlockState state) {
			return this.quickBlock.isAir(state);
		}

		@Override
		public int getLightValue(BlockState state) {
			if (this.quickBlock == null) {
				return this._getLightValue(state);
			}
			return this.quickBlock.getLightValue(state);
		}

		@Override
		public Material getMaterial(BlockState state) {

			return this.quickBlock.getMaterial(state);
		}

		@Override
		public MaterialColor getMaterialColor(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.getMaterialColor(state, worldIn, pos);
		}

		@Override
		public void updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags) {

			this.quickBlock.updateNeighbors(stateIn, worldIn, pos, flags);
		}

		@Override
		public boolean isIn(Tag<Block> tagIn) {

			return this.quickBlock.isIn(tagIn);
		}

		@Override
		public void updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags) {

			this.quickBlock.updateDiagonalNeighbors(state, worldIn, pos, flags);
		}

		@Override
		public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState,
				IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {

			return this.quickBlock.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}

		@Override
		public BlockState rotate(BlockState state, Rotation rot) {

			return this.quickBlock.rotate(state, rot);
		}

		@Override
		public BlockState mirror(BlockState state, Mirror mirrorIn) {

			return this.quickBlock.mirror(state, mirrorIn);
		}

		@Override
		public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.isNormalCube(state, worldIn, pos);
		}

		@Override
		public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.causesSuffocation(state, worldIn, pos);
		}

		@Override
		public boolean isViewBlocking(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.isViewBlocking(state, worldIn, pos);
		}

		@Override
		public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {

			return this.quickBlock.allowsMovement(state, worldIn, pos, type);
		}

		@Override
		public BlockRenderType getRenderType(BlockState state) {

			return this.quickBlock.getRenderType(state);
		}

		@Override
		public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {

			return this.quickBlock.isReplaceable(state, useContext);
		}

		@Override
		public boolean isReplaceable(BlockState p_225541_1_, Fluid p_225541_2_) {

			return this.quickBlock.isReplaceable(p_225541_1_, p_225541_2_);
		}

		@Override
		public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.getBlockHardness(blockState, worldIn, pos);
		}

		@Override
		public boolean ticksRandomly(BlockState state) {

			return this.quickBlock.ticksRandomly(state);
		}

		@Override
		public boolean hasTileEntity() {

			return this.quickBlock.hasTileEntity();
		}

		@Override
		public boolean needsPostProcessing(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.needsPostProcessing(state, worldIn, pos);
		}

		@Override
		public boolean isEmissiveRendering(BlockState p_225543_1_) {

			return this.quickBlock.isEmissiveRendering(p_225543_1_);
		}

		@Override
		public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {

			return this.quickBlock.isSideInvisible(state, adjacentBlockState, side);
		}

		@Override
		public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

			return this.quickBlock.getShape(state, worldIn, pos, context);
		}

		@Override
		public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
				ISelectionContext context) {

			return this.quickBlock.getCollisionShape(state, worldIn, pos, context);
		}

		@Override
		public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.getRenderShape(state, worldIn, pos);
		}

		@Override
		public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.getRaytraceShape(state, worldIn, pos);
		}

		@Override
		public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {

			return this.quickBlock.propagatesSkylightDown(state, reader, pos);
		}

		@Override
		public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.getOpacity(state, worldIn, pos);
		}

		@Override
		public boolean isTransparent(BlockState state) {
			if (this.quickBlock == null) {
				return this._isTransparent(state);
			}
			return this.quickBlock.isTransparent(state);
		}

		@Override
		public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {

			this.quickBlock.tick(state, worldIn, pos, rand);
		}

		@Override
		public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

			this.quickBlock.animateTick(stateIn, worldIn, pos, rand);
		}

		@Override
		public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {

			this.quickBlock.onPlayerDestroy(worldIn, pos, state);
		}

		@Override
		public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
				boolean isMoving) {

			this.quickBlock.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}

		@Override
		public int tickRate(IWorldReader worldIn) {

			return this.quickBlock.tickRate(worldIn);
		}

		@Override
		public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {

			return this.quickBlock.getContainer(state, worldIn, pos);
		}

		@Override
		public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {

			this.quickBlock.onBlockAdded(state, worldIn, pos, oldState, isMoving);
		}

		@Override
		public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {

			this.quickBlock.onReplaced(state, worldIn, pos, newState, isMoving);
		}

		@Override
		public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn,
				BlockPos pos) {

			return this.quickBlock.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
		}

		@Override
		public void spawnAdditionalDrops(BlockState state, World worldIn, BlockPos pos, ItemStack stack) {

			this.quickBlock.spawnAdditionalDrops(state, worldIn, pos, stack);
		}

		@Override
		public ResourceLocation getLootTable() {

			return this.quickBlock.getLootTable();
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, Builder builder) {

			return this.quickBlock.getDrops(state, builder);
		}

		@Override
		public void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {

			this.quickBlock.dropXpOnBlockBreak(worldIn, pos, amount);
		}

		@Override
		public float getExplosionResistance() {

			return this.quickBlock.getExplosionResistance();
		}

		@Override
		public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {

			this.quickBlock.onExplosionDestroy(worldIn, pos, explosionIn);
		}

		@Override
		public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {

			return this.quickBlock.isValidPosition(state, worldIn, pos);
		}

		@Override
		public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
				Hand handIn, BlockRayTraceResult hit) {

			return this.quickBlock.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}

		@Override
		public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {

			this.quickBlock.onEntityWalk(worldIn, pos, entityIn);
		}

		@Override
		public BlockState getStateForPlacement(BlockItemUseContext context) {

			return this.quickBlock.getStateForPlacement(context);
		}

		@Override
		public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {

			this.quickBlock.onBlockClicked(state, worldIn, pos, player);
		}

		@Override
		public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {

			return this.quickBlock.getWeakPower(blockState, blockAccess, pos, side);
		}

		@Override
		public boolean canProvidePower(BlockState state) {

			return this.quickBlock.canProvidePower(state);
		}

		@Override
		public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {

			this.quickBlock.onEntityCollision(state, worldIn, pos, entityIn);
		}

		@Override
		public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {

			return this.quickBlock.getStrongPower(blockState, blockAccess, pos, side);
		}

		@Override
		public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te,
				ItemStack stack) {

			this.quickBlock.harvestBlock(worldIn, player, pos, state, te, stack);
		}

		@Override
		public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
				ItemStack stack) {

			this.quickBlock.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}

		@Override
		public boolean canSpawnInBlock() {

			return this.quickBlock.canSpawnInBlock();
		}

		@Override
		public ITextComponent getNameTextComponent() {

			return this.quickBlock.getNameTextComponent();
		}

		@Override
		public String getTranslationKey() {

			return this.quickBlock.getTranslationKey();
		}

		@Override
		public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {

			return this.quickBlock.eventReceived(state, worldIn, pos, id, param);
		}

		@Override
		public PushReaction getPushReaction(BlockState state) {

			return this.quickBlock.getPushReaction(state);
		}

		@Override
		public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.getAmbientOcclusionLightValue(state, worldIn, pos);
		}

		@Override
		public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {

			this.quickBlock.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		}

		@Override
		public void onLanded(IBlockReader worldIn, Entity entityIn) {

			this.quickBlock.onLanded(worldIn, entityIn);
		}

		@Override
		public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {

			return this.quickBlock.getItem(worldIn, pos, state);
		}

		@Override
		public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {

			this.quickBlock.fillItemGroup(group, items);
		}

		@Override
		public IFluidState getFluidState(BlockState state) {

			return this.quickBlock.getFluidState(state);
		}

		@Override
		public float getSlipperiness() {

			return this.quickBlock.getSlipperiness();
		}

		@Override
		public float getSpeedFactor() {

			return this.quickBlock.getSpeedFactor();
		}

		@Override
		public float getJumpFactor() {

			return this.quickBlock.getJumpFactor();
		}

		@Override
		public long getPositionRandom(BlockState state, BlockPos pos) {

			return this.quickBlock.getPositionRandom(state, pos);
		}

		@Override
		public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {

			this.quickBlock.onProjectileCollision(worldIn, state, hit, projectile);
		}

		@Override
		public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {

			this.quickBlock.onBlockHarvested(worldIn, pos, state, player);
		}

		@Override
		public void fillWithRain(World worldIn, BlockPos pos) {

			this.quickBlock.fillWithRain(worldIn, pos);
		}

		@Override
		public boolean canDropFromExplosion(Explosion explosionIn) {

			return this.quickBlock.canDropFromExplosion(explosionIn);
		}

		@Override
		public boolean hasComparatorInputOverride(BlockState state) {

			return this.quickBlock.hasComparatorInputOverride(state);
		}

		@Override
		public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {

			return this.quickBlock.getComparatorInputOverride(blockState, worldIn, pos);
		}

		@Override
		protected void fillStateContainer(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {

			if (this.quickBlock == null) {
				this._fillStateContainer(builder);
				return;
			}
			this.quickBlock.fillStateContainer(builder);
		}

		@Override
		public StateContainer<Block, BlockState> getStateContainer() {

			return this.quickBlock.getStateContainer();
		}

		@Override
		public OffsetType getOffsetType() {

			return this.quickBlock.getOffsetType();
		}

		@Override
		public Vec3d getOffset(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return this.quickBlock.getOffset(state, worldIn, pos);
		}

		@Override
		public SoundType getSoundType(BlockState state) {

			return this.quickBlock.getSoundType(state);
		}

		@Override
		public Item asItem() {

			return this.quickBlock.asItem();
		}

		@Override
		public boolean isVariableOpacity() {

			return this.quickBlock.isVariableOpacity();
		}

		@Override
		public String toString() {

			return this.quickBlock.toString();
		}

		@Override
		public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
				ITooltipFlag flagIn) {

			this.quickBlock.addInformation(stack, worldIn, tooltip, flagIn);
		}

		@Override
		public float getSlipperiness(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {

			return this.quickBlock.getSlipperiness(state, world, pos, entity);
		}

		@Override
		public ToolType getHarvestTool(BlockState state) {

			return this.quickBlock.getHarvestTool(state);
		}

		@Override
		public int getHarvestLevel(BlockState state) {

			return this.quickBlock.getHarvestLevel(state);
		}

		@Override
		public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing,
				IPlantable plantable) {

			return this.quickBlock.canSustainPlant(state, world, pos, facing, plantable);
		}

		// Delegate Methods Pt. 2

		@SuppressWarnings("deprecation")
		public boolean _canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {

			return super.canEntitySpawn(state, worldIn, pos, type);
		}

		@SuppressWarnings("deprecation")
		public boolean _isAir(BlockState state) {

			return super.isAir(state);
		}

		@SuppressWarnings("deprecation")
		public int _getLightValue(BlockState state) {

			return super.getLightValue(state);
		}

		@SuppressWarnings("deprecation")
		public Material _getMaterial(BlockState state) {

			return super.getMaterial(state);
		}

		@SuppressWarnings("deprecation")
		public MaterialColor _getMaterialColor(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.getMaterialColor(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public void _updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags) {

			super.updateNeighbors(stateIn, worldIn, pos, flags);
		}

		public boolean _isIn(Tag<Block> tagIn) {

			return super.isIn(tagIn);
		}

		@SuppressWarnings("deprecation")
		public void _updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags) {

			super.updateDiagonalNeighbors(state, worldIn, pos, flags);
		}
 
		@SuppressWarnings("deprecation")
		public BlockState _updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState,
				IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {

			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}

		@SuppressWarnings("deprecation")
		public BlockState _rotate(BlockState state, Rotation rot) {

			return super.rotate(state, rot);
		}

		@SuppressWarnings("deprecation")
		public BlockState _mirror(BlockState state, Mirror mirrorIn) {

			return super.mirror(state, mirrorIn);
		}

		@SuppressWarnings("deprecation")
		public boolean _isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.isNormalCube(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public boolean _causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.causesSuffocation(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public boolean _isViewBlocking(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.isViewBlocking(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public boolean _allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {

			return super.allowsMovement(state, worldIn, pos, type);
		}

		@SuppressWarnings("deprecation")
		public BlockRenderType _getRenderType(BlockState state) {

			return super.getRenderType(state);
		}

		@SuppressWarnings("deprecation")
		public boolean _isReplaceable(BlockState state, BlockItemUseContext useContext) {

			return super.isReplaceable(state, useContext);
		}

		@SuppressWarnings("deprecation")
		public boolean _isReplaceable(BlockState p_225541_1_, Fluid p_225541_2_) {

			return super.isReplaceable(p_225541_1_, p_225541_2_);
		}

		@SuppressWarnings("deprecation")
		public float _getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {

			return super.getBlockHardness(blockState, worldIn, pos);
		}


		@SuppressWarnings("deprecation")
		public boolean _hasTileEntity() {

			return super.hasTileEntity();
		}

		@SuppressWarnings("deprecation")
		public boolean _needsPostProcessing(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.needsPostProcessing(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public boolean _isEmissiveRendering(BlockState p_225543_1_) {

			return super.isEmissiveRendering(p_225543_1_);
		}

		@SuppressWarnings("deprecation")
		public boolean _isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {

			return super.isSideInvisible(state, adjacentBlockState, side);
		}

		@SuppressWarnings("deprecation")
		public VoxelShape _getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

			return super.getShape(state, worldIn, pos, context);
		}

		@SuppressWarnings("deprecation")
		public VoxelShape _getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
				ISelectionContext context) {

			return super.getCollisionShape(state, worldIn, pos, context);
		}

		@SuppressWarnings("deprecation")
		public VoxelShape _getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.getRenderShape(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public VoxelShape _getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.getRaytraceShape(state, worldIn, pos);
		}

		public boolean _propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {

			return super.propagatesSkylightDown(state, reader, pos);
		}

		@SuppressWarnings("deprecation")
		public int _getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.getOpacity(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public boolean _isTransparent(BlockState state) {

			return super.isTransparent(state);
		}

		@SuppressWarnings("deprecation")
		public void _randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {

			super.randomTick(state, worldIn, pos, random);
		}

		@SuppressWarnings("deprecation")
		public void _tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {

			super.tick(state, worldIn, pos, rand);
		}

		public void _animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

			super.animateTick(stateIn, worldIn, pos, rand);
		}

		public void _onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {

			super.onPlayerDestroy(worldIn, pos, state);
		}

		@SuppressWarnings("deprecation")
		public void _neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
				boolean isMoving) {

			super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}

		public int _tickRate(IWorldReader worldIn) {

			return super.tickRate(worldIn);
		}

		@SuppressWarnings("deprecation")
		public INamedContainerProvider _getContainer(BlockState state, World worldIn, BlockPos pos) {

			return super.getContainer(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public void _onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState,
				boolean isMoving) {

			super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
		}

		@SuppressWarnings("deprecation")
		public void _onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}

		@SuppressWarnings("deprecation")
		public float _getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn,
				BlockPos pos) {

			return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public void _spawnAdditionalDrops(BlockState state, World worldIn, BlockPos pos, ItemStack stack) {

			super.spawnAdditionalDrops(state, worldIn, pos, stack);
		}

		public ResourceLocation _getLootTable() {

			return super.getLootTable();
		}

		@SuppressWarnings("deprecation")
		public List<ItemStack> _getDrops(BlockState state, Builder builder) {

			return super.getDrops(state, builder);
		}

		public void _dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {

			super.dropXpOnBlockBreak(worldIn, pos, amount);
		}

		@SuppressWarnings("deprecation")
		public float _getExplosionResistance() {

			return super.getExplosionResistance();
		}

		public void _onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {

			super.onExplosionDestroy(worldIn, pos, explosionIn);
		}

		@SuppressWarnings("deprecation")
		public boolean _isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {

			return super.isValidPosition(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public ActionResultType _onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
				Hand handIn, BlockRayTraceResult hit) {

			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}

		public void _onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {

			super.onEntityWalk(worldIn, pos, entityIn);
		}

		public BlockState _getStateForPlacement(BlockItemUseContext context) {

			return super.getStateForPlacement(context);
		}

		@SuppressWarnings("deprecation")
		public void _onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {

			super.onBlockClicked(state, worldIn, pos, player);
		}

		@SuppressWarnings("deprecation")
		public int _getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {

			return super.getWeakPower(blockState, blockAccess, pos, side);
		}

		@SuppressWarnings("deprecation")
		public boolean _canProvidePower(BlockState state) {

			return super.canProvidePower(state);
		}

		@SuppressWarnings("deprecation")
		public void _onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {

			super.onEntityCollision(state, worldIn, pos, entityIn);
		}

		@SuppressWarnings("deprecation")
		public int _getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {

			return super.getStrongPower(blockState, blockAccess, pos, side);
		}

		public void _harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te,
				ItemStack stack) {

			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}

		public void _onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
				ItemStack stack) {

			super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}

		public boolean _canSpawnInBlock() {

			return super.canSpawnInBlock();
		}

		public ITextComponent _getNameTextComponent() {

			return super.getNameTextComponent();
		}

		public String _getTranslationKey() {

			return super.getTranslationKey();
		}

		@SuppressWarnings("deprecation")
		public boolean _eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {

			return super.eventReceived(state, worldIn, pos, id, param);
		}

		@SuppressWarnings("deprecation")
		public PushReaction _getPushReaction(BlockState state) {

			return super.getPushReaction(state);
		}

		@SuppressWarnings("deprecation")
		public float _getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.getAmbientOcclusionLightValue(state, worldIn, pos);
		}

		public void _onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {

			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		}

		public void _onLanded(IBlockReader worldIn, Entity entityIn) {

			super.onLanded(worldIn, entityIn);
		}

		@SuppressWarnings("deprecation")
		public ItemStack _getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {

			return super.getItem(worldIn, pos, state);
		}

		public void _fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {

			super.fillItemGroup(group, items);
		}

		@SuppressWarnings("deprecation")
		public IFluidState _getFluidState(BlockState state) {

			return super.getFluidState(state);
		}

		@SuppressWarnings("deprecation")
		public float _getSlipperiness() {

			return super.getSlipperiness();
		}

		public float _getSpeedFactor() {

			return super.getSpeedFactor();
		}

		public float _getJumpFactor() {

			return super.getJumpFactor();
		}

		@SuppressWarnings("deprecation")
		public long _getPositionRandom(BlockState state, BlockPos pos) {

			return super.getPositionRandom(state, pos);
		}

		public void _onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit,
				Entity projectile) {

			super.onProjectileCollision(worldIn, state, hit, projectile);
		}

		public void _onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {

			super.onBlockHarvested(worldIn, pos, state, player);
		}

		public void _fillWithRain(World worldIn, BlockPos pos) {

			super.fillWithRain(worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public boolean _canDropFromExplosion(Explosion explosionIn) {

			return super.canDropFromExplosion(explosionIn);
		}

		@SuppressWarnings("deprecation")
		public boolean _hasComparatorInputOverride(BlockState state) {

			return super.hasComparatorInputOverride(state);
		}

		@SuppressWarnings("deprecation")
		public int _getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {

			return super.getComparatorInputOverride(blockState, worldIn, pos);
		}

		protected void _fillStateContainer(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {

			super.fillStateContainer(builder);
		}

		public StateContainer<Block, BlockState> _getStateContainer() {

			return super.getStateContainer();
		}

		public OffsetType _getOffsetType() {

			return super.getOffsetType();
		}

		@SuppressWarnings("deprecation")
		public Vec3d _getOffset(BlockState state, IBlockReader worldIn, BlockPos pos) {

			return super.getOffset(state, worldIn, pos);
		}

		@SuppressWarnings("deprecation")
		public SoundType _getSoundType(BlockState state) {

			return super.getSoundType(state);
		}

		public Item _asItem() {

			return super.asItem();
		}

		public boolean _isVariableOpacity() {

			return super.isVariableOpacity();
		}

		public String _toString() {

			return super.toString();
		}

		public void _addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
				ITooltipFlag flagIn) {

			super.addInformation(stack, worldIn, tooltip, flagIn);
		}

		public float _getSlipperiness(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {

			return super.getSlipperiness(state, world, pos, entity);
		}

		public ToolType _getHarvestTool(BlockState state) {

			return super.getHarvestTool(state);
		}

		public int _getHarvestLevel(BlockState state) {

			return super.getHarvestLevel(state);
		}

		public boolean _canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing,
				IPlantable plantable) {

			return super.canSustainPlant(state, world, pos, facing, plantable);
		}

		public boolean _ticksRandomly(BlockState state) {
			return super.ticksRandomly(state);
		}

	}

}
