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
import net.minecraft.item.DyeColor;
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
		for(QuickBlock block : QuickBlock.getAllBlocks()) {
			RegistryObject<Block> ro = BLOCKS.register(block.getSafeRegistryName(), QuickBlockBuilder.build(block));
		    QuickItemRegistry.ITEMS.register(block.getSafeRegistryName(), () -> new BlockItem(ro.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
		    //BLOCKS.register(bus);
		}
		BLOCKS.register(bus);
	}
		
	public static class QuickBlockBuilder extends Block {
		
		private final QuickBlock quickBlock;

		public static Supplier<QuickBlockBuilder> build(QuickBlock block) {
			//TODO: Override Materials / DyeColor in QuickBlock
			Properties p = Block.Properties.create(Material.EARTH, DyeColor.GRAY);
			//Block.Properties
			return () -> new QuickBlockBuilder(block, p);
		}

		public QuickBlockBuilder(QuickBlock block, Properties p) {
			super(p);
			if(block == null) {
				throw new NullPointerException("Block must be non-null");
			}
			System.out.println("Creating QuickBlockBulder(" + block.blockName + ")");
			this.quickBlock = block;
			this.quickBlock.getSafeRegistryName();
			this.quickBlock.setBlock(this);
		}

		@Override
		public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
			// TODO Auto-generated method stub
			return this.quickBlock.canEntitySpawn(state, worldIn, pos, type);
		}

		@Override
		public boolean isAir(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.isAir(state);
		}

		@Override
		public int getLightValue(BlockState state) {
			if(this.quickBlock == null) {
				return this._getLightValue(state);
			}
			return this.quickBlock.getLightValue(state);
		}

		@Override
		public Material getMaterial(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.getMaterial(state);
		}

		@Override
		public MaterialColor getMaterialColor(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getMaterialColor(state, worldIn, pos);
		}

		@Override
		public void updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags) {
			// TODO Auto-generated method stub
			this.quickBlock.updateNeighbors(stateIn, worldIn, pos, flags);
		}

		@Override
		public boolean isIn(Tag<Block> tagIn) {
			// TODO Auto-generated method stub
			return this.quickBlock.isIn(tagIn);
		}

		@Override
		public void updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags) {
			// TODO Auto-generated method stub
			this.quickBlock.updateDiagonalNeighbors(state, worldIn, pos, flags);
		}

		@Override
		public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState,
				IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
			// TODO Auto-generated method stub
			return this.quickBlock.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}

		@Override
		public BlockState rotate(BlockState state, Rotation rot) {
			// TODO Auto-generated method stub
			return this.quickBlock.rotate(state, rot);
		}

		@Override
		public BlockState mirror(BlockState state, Mirror mirrorIn) {
			// TODO Auto-generated method stub
			return this.quickBlock.mirror(state, mirrorIn);
		}

		@Override
		public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.isNormalCube(state, worldIn, pos);
		}

		@Override
		public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.causesSuffocation(state, worldIn, pos);
		}

		@Override
		public boolean isViewBlocking(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.isViewBlocking(state, worldIn, pos);
		}

		@Override
		public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
			// TODO Auto-generated method stub
			return this.quickBlock.allowsMovement(state, worldIn, pos, type);
		}

		@Override
		public BlockRenderType getRenderType(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.getRenderType(state);
		}

		@Override
		public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
			// TODO Auto-generated method stub
			return this.quickBlock.isReplaceable(state, useContext);
		}

		@Override
		public boolean isReplaceable(BlockState p_225541_1_, Fluid p_225541_2_) {
			// TODO Auto-generated method stub
			return this.quickBlock.isReplaceable(p_225541_1_, p_225541_2_);
		}

		@Override
		public float getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getBlockHardness(blockState, worldIn, pos);
		}

		@Override
		public boolean ticksRandomly(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.ticksRandomly(state);
		}

		@Override
		public boolean hasTileEntity() {
			// TODO Auto-generated method stub
			return this.quickBlock.hasTileEntity();
		}

		@Override
		public boolean needsPostProcessing(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.needsPostProcessing(state, worldIn, pos);
		}

		@Override
		public boolean isEmissiveRendering(BlockState p_225543_1_) {
			// TODO Auto-generated method stub
			return this.quickBlock.isEmissiveRendering(p_225543_1_);
		}

		@Override
		public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
			// TODO Auto-generated method stub
			return this.quickBlock.isSideInvisible(state, adjacentBlockState, side);
		}

		@Override
		public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
			// TODO Auto-generated method stub
			return this.quickBlock.getShape(state, worldIn, pos, context);
		}

		@Override
		public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
				ISelectionContext context) {
			// TODO Auto-generated method stub
			return this.quickBlock.getCollisionShape(state, worldIn, pos, context);
		}

		@Override
		public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getRenderShape(state, worldIn, pos);
		}

		@Override
		public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getRaytraceShape(state, worldIn, pos);
		}

		@Override
		public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.propagatesSkylightDown(state, reader, pos);
		}

		@Override
		public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getOpacity(state, worldIn, pos);
		}

		@Override
		public boolean isTransparent(BlockState state) {
			if(this.quickBlock == null) {
				return this._isTransparent(state);
			}
			return this.quickBlock.isTransparent(state);
		}

		@Override
		public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
			// TODO Auto-generated method stub
			this.quickBlock.randomTick(state, worldIn, pos, random);
		}

		@Override
		public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
			// TODO Auto-generated method stub
			this.quickBlock.tick(state, worldIn, pos, rand);
		}

		@Override
		public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
			// TODO Auto-generated method stub
			this.quickBlock.animateTick(stateIn, worldIn, pos, rand);
		}

		@Override
		public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
			// TODO Auto-generated method stub
			this.quickBlock.onPlayerDestroy(worldIn, pos, state);
		}

		@Override
		public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
				boolean isMoving) {
			// TODO Auto-generated method stub
			this.quickBlock.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}

		@Override
		public int tickRate(IWorldReader worldIn) {
			// TODO Auto-generated method stub
			return this.quickBlock.tickRate(worldIn);
		}

		@Override
		public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getContainer(state, worldIn, pos);
		}

		@Override
		public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
			// TODO Auto-generated method stub
			this.quickBlock.onBlockAdded(state, worldIn, pos, oldState, isMoving);
		}

		@Override
		public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
			// TODO Auto-generated method stub
			this.quickBlock.onReplaced(state, worldIn, pos, newState, isMoving);
		}

		@Override
		public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn,
				BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
		}

		@Override
		public void spawnAdditionalDrops(BlockState state, World worldIn, BlockPos pos, ItemStack stack) {
			// TODO Auto-generated method stub
			this.quickBlock.spawnAdditionalDrops(state, worldIn, pos, stack);
		}

		@Override
		public ResourceLocation getLootTable() {
			// TODO Auto-generated method stub
			return this.quickBlock.getLootTable();
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, Builder builder) {
			// TODO Auto-generated method stub
			return this.quickBlock.getDrops(state, builder);
		}

		@Override
		public void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {
			// TODO Auto-generated method stub
			this.quickBlock.dropXpOnBlockBreak(worldIn, pos, amount);
		}

		@Override
		public float getExplosionResistance() {
			// TODO Auto-generated method stub
			return this.quickBlock.getExplosionResistance();
		}

		@Override
		public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
			// TODO Auto-generated method stub
			this.quickBlock.onExplosionDestroy(worldIn, pos, explosionIn);
		}

		@Override
		public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.isValidPosition(state, worldIn, pos);
		}

		@Override
		public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
				Hand handIn, BlockRayTraceResult hit) {
			// TODO Auto-generated method stub
			return this.quickBlock.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}

		@Override
		public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
			// TODO Auto-generated method stub
			this.quickBlock.onEntityWalk(worldIn, pos, entityIn);
		}

		@Override
		public BlockState getStateForPlacement(BlockItemUseContext context) {
			// TODO Auto-generated method stub
			return this.quickBlock.getStateForPlacement(context);
		}

		@Override
		public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
			// TODO Auto-generated method stub
			this.quickBlock.onBlockClicked(state, worldIn, pos, player);
		}

		@Override
		public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
			// TODO Auto-generated method stub
			return this.quickBlock.getWeakPower(blockState, blockAccess, pos, side);
		}

		@Override
		public boolean canProvidePower(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.canProvidePower(state);
		}

		@Override
		public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
			// TODO Auto-generated method stub
			this.quickBlock.onEntityCollision(state, worldIn, pos, entityIn);
		}

		@Override
		public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
			// TODO Auto-generated method stub
			return this.quickBlock.getStrongPower(blockState, blockAccess, pos, side);
		}

		@Override
		public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te,
				ItemStack stack) {
			// TODO Auto-generated method stub
			this.quickBlock.harvestBlock(worldIn, player, pos, state, te, stack);
		}

		@Override
		public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
				ItemStack stack) {
			// TODO Auto-generated method stub
			this.quickBlock.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}

		@Override
		public boolean canSpawnInBlock() {
			// TODO Auto-generated method stub
			return this.quickBlock.canSpawnInBlock();
		}

		@Override
		public ITextComponent getNameTextComponent() {
			// TODO Auto-generated method stub
			return this.quickBlock.getNameTextComponent();
		}

		@Override
		public String getTranslationKey() {
			// TODO Auto-generated method stub
			return this.quickBlock.getTranslationKey();
		}

		@Override
		public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
			// TODO Auto-generated method stub
			return this.quickBlock.eventReceived(state, worldIn, pos, id, param);
		}

		@Override
		public PushReaction getPushReaction(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.getPushReaction(state);
		}

		@Override
		public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getAmbientOcclusionLightValue(state, worldIn, pos);
		}

		@Override
		public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
			// TODO Auto-generated method stub
			this.quickBlock.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		}

		@Override
		public void onLanded(IBlockReader worldIn, Entity entityIn) {
			// TODO Auto-generated method stub
			this.quickBlock.onLanded(worldIn, entityIn);
		}

		@Override
		public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.getItem(worldIn, pos, state);
		}

		@Override
		public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
			// TODO Auto-generated method stub
			this.quickBlock.fillItemGroup(group, items);
		}

		@Override
		public IFluidState getFluidState(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.getFluidState(state);
		}

		@Override
		public float getSlipperiness() {
			// TODO Auto-generated method stub
			return this.quickBlock.getSlipperiness();
		}

		@Override
		public float getSpeedFactor() {
			// TODO Auto-generated method stub
			return this.quickBlock.getSpeedFactor();
		}

		@Override
		public float getJumpFactor() {
			// TODO Auto-generated method stub
			return this.quickBlock.getJumpFactor();
		}

		@Override
		public long getPositionRandom(BlockState state, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getPositionRandom(state, pos);
		}

		@Override
		public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {
			// TODO Auto-generated method stub
			this.quickBlock.onProjectileCollision(worldIn, state, hit, projectile);
		}

		@Override
		public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
			// TODO Auto-generated method stub
			this.quickBlock.onBlockHarvested(worldIn, pos, state, player);
		}

		@Override
		public void fillWithRain(World worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			this.quickBlock.fillWithRain(worldIn, pos);
		}

		@Override
		public boolean canDropFromExplosion(Explosion explosionIn) {
			// TODO Auto-generated method stub
			return this.quickBlock.canDropFromExplosion(explosionIn);
		}

		@Override
		public boolean hasComparatorInputOverride(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.hasComparatorInputOverride(state);
		}

		@Override
		public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getComparatorInputOverride(blockState, worldIn, pos);
		}

		@Override
		protected void fillStateContainer(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {
			// TODO Auto-generated method stub
			if(this.quickBlock == null) {
				this._fillStateContainer(builder);
				return;
			}
			this.quickBlock.fillStateContainer(builder);
		}

		@Override
		public StateContainer<Block, BlockState> getStateContainer() {
			// TODO Auto-generated method stub
			return this.quickBlock.getStateContainer();
		}

		@Override
		public OffsetType getOffsetType() {
			// TODO Auto-generated method stub
			return this.quickBlock.getOffsetType();
		}

		@Override
		public Vec3d getOffset(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return this.quickBlock.getOffset(state, worldIn, pos);
		}

		@Override
		public SoundType getSoundType(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.getSoundType(state);
		}

		@Override
		public Item asItem() {
			// TODO Auto-generated method stub
			return this.quickBlock.asItem();
		}

		@Override
		public boolean isVariableOpacity() {
			// TODO Auto-generated method stub
			return this.quickBlock.isVariableOpacity();
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.quickBlock.toString();
		}

		@Override
		public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
				ITooltipFlag flagIn) {
			// TODO Auto-generated method stub
			this.quickBlock.addInformation(stack, worldIn, tooltip, flagIn);
		}

		@Override
		public float getSlipperiness(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
			// TODO Auto-generated method stub
			return this.quickBlock.getSlipperiness(state, world, pos, entity);
		}

		@Override
		public ToolType getHarvestTool(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.getHarvestTool(state);
		}

		@Override
		public int getHarvestLevel(BlockState state) {
			// TODO Auto-generated method stub
			return this.quickBlock.getHarvestLevel(state);
		}

		@Override
		public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing,
				IPlantable plantable) {
			// TODO Auto-generated method stub
			return this.quickBlock.canSustainPlant(state, world, pos, facing, plantable);
		}

		
		// Delegate Methods Pt. 2
		
		
		public boolean _canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type) {
			// TODO Auto-generated method stub
			return super.canEntitySpawn(state, worldIn, pos, type);
		}

		
		public boolean _isAir(BlockState state) {
			// TODO Auto-generated method stub
			return super.isAir(state);
		}

		
		public int _getLightValue(BlockState state) {
			// TODO Auto-generated method stub
			return super.getLightValue(state);
		}

		
		public Material _getMaterial(BlockState state) {
			// TODO Auto-generated method stub
			return super.getMaterial(state);
		}

		
		public MaterialColor _getMaterialColor(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getMaterialColor(state, worldIn, pos);
		}

		
		public void _updateNeighbors(BlockState stateIn, IWorld worldIn, BlockPos pos, int flags) {
			// TODO Auto-generated method stub
			super.updateNeighbors(stateIn, worldIn, pos, flags);
		}

		
		public boolean _isIn(Tag<Block> tagIn) {
			// TODO Auto-generated method stub
			return super.isIn(tagIn);
		}

		
		public void _updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags) {
			// TODO Auto-generated method stub
			super.updateDiagonalNeighbors(state, worldIn, pos, flags);
		}

		
		public BlockState _updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState,
				IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
			// TODO Auto-generated method stub
			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}

		
		public BlockState _rotate(BlockState state, Rotation rot) {
			// TODO Auto-generated method stub
			return super.rotate(state, rot);
		}

		
		public BlockState _mirror(BlockState state, Mirror mirrorIn) {
			// TODO Auto-generated method stub
			return super.mirror(state, mirrorIn);
		}

		
		public boolean _isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.isNormalCube(state, worldIn, pos);
		}

		
		public boolean _causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.causesSuffocation(state, worldIn, pos);
		}

		
		public boolean _isViewBlocking(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.isViewBlocking(state, worldIn, pos);
		}

		
		public boolean _allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
			// TODO Auto-generated method stub
			return super.allowsMovement(state, worldIn, pos, type);
		}

		
		public BlockRenderType _getRenderType(BlockState state) {
			// TODO Auto-generated method stub
			return super.getRenderType(state);
		}

		
		public boolean _isReplaceable(BlockState state, BlockItemUseContext useContext) {
			// TODO Auto-generated method stub
			return super.isReplaceable(state, useContext);
		}

		
		public boolean _isReplaceable(BlockState p_225541_1_, Fluid p_225541_2_) {
			// TODO Auto-generated method stub
			return super.isReplaceable(p_225541_1_, p_225541_2_);
		}

		
		public float _getBlockHardness(BlockState blockState, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getBlockHardness(blockState, worldIn, pos);
		}

		
		public boolean _ticksRandomly(BlockState state) {
			// TODO Auto-generated method stub
			return super.ticksRandomly(state);
		}

		
		public boolean _hasTileEntity() {
			// TODO Auto-generated method stub
			return super.hasTileEntity();
		}

		
		public boolean _needsPostProcessing(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.needsPostProcessing(state, worldIn, pos);
		}

		
		public boolean _isEmissiveRendering(BlockState p_225543_1_) {
			// TODO Auto-generated method stub
			return super.isEmissiveRendering(p_225543_1_);
		}

		
		public boolean _isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
			// TODO Auto-generated method stub
			return super.isSideInvisible(state, adjacentBlockState, side);
		}

		
		public VoxelShape _getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
			// TODO Auto-generated method stub
			return super.getShape(state, worldIn, pos, context);
		}

		
		public VoxelShape _getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
				ISelectionContext context) {
			// TODO Auto-generated method stub
			return super.getCollisionShape(state, worldIn, pos, context);
		}

		
		public VoxelShape _getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getRenderShape(state, worldIn, pos);
		}

		
		public VoxelShape _getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getRaytraceShape(state, worldIn, pos);
		}

		
		public boolean _propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.propagatesSkylightDown(state, reader, pos);
		}

		
		public int _getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getOpacity(state, worldIn, pos);
		}

		
		public boolean _isTransparent(BlockState state) {
			// TODO Auto-generated method stub
			return super.isTransparent(state);
		}

		
		public void _randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
			// TODO Auto-generated method stub
			super.randomTick(state, worldIn, pos, random);
		}

		
		public void _tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
			// TODO Auto-generated method stub
			super.tick(state, worldIn, pos, rand);
		}

		
		public void _animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
			// TODO Auto-generated method stub
			super.animateTick(stateIn, worldIn, pos, rand);
		}

		
		public void _onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
			// TODO Auto-generated method stub
			super.onPlayerDestroy(worldIn, pos, state);
		}

		
		public void _neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
				boolean isMoving) {
			// TODO Auto-generated method stub
			super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		}

		
		public int _tickRate(IWorldReader worldIn) {
			// TODO Auto-generated method stub
			return super.tickRate(worldIn);
		}

		
		public INamedContainerProvider _getContainer(BlockState state, World worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getContainer(state, worldIn, pos);
		}

		
		public void _onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
			// TODO Auto-generated method stub
			super.onBlockAdded(state, worldIn, pos, oldState, isMoving);
		}

		
		public void _onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
			// TODO Auto-generated method stub
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}

		
		public float _getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn,
				BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
		}

		
		public void _spawnAdditionalDrops(BlockState state, World worldIn, BlockPos pos, ItemStack stack) {
			// TODO Auto-generated method stub
			super.spawnAdditionalDrops(state, worldIn, pos, stack);
		}

		
		public ResourceLocation _getLootTable() {
			// TODO Auto-generated method stub
			return super.getLootTable();
		}

		
		public List<ItemStack> _getDrops(BlockState state, Builder builder) {
			// TODO Auto-generated method stub
			return super.getDrops(state, builder);
		}

		
		public void _dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {
			// TODO Auto-generated method stub
			super.dropXpOnBlockBreak(worldIn, pos, amount);
		}

		
		public float _getExplosionResistance() {
			// TODO Auto-generated method stub
			return super.getExplosionResistance();
		}

		
		public void _onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
			// TODO Auto-generated method stub
			super.onExplosionDestroy(worldIn, pos, explosionIn);
		}

		
		public boolean _isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.isValidPosition(state, worldIn, pos);
		}

		
		public ActionResultType _onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
				Hand handIn, BlockRayTraceResult hit) {
			// TODO Auto-generated method stub
			return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}

		
		public void _onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
			// TODO Auto-generated method stub
			super.onEntityWalk(worldIn, pos, entityIn);
		}

		
		public BlockState _getStateForPlacement(BlockItemUseContext context) {
			// TODO Auto-generated method stub
			return super.getStateForPlacement(context);
		}

		
		public void _onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
			// TODO Auto-generated method stub
			super.onBlockClicked(state, worldIn, pos, player);
		}

		
		public int _getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
			// TODO Auto-generated method stub
			return super.getWeakPower(blockState, blockAccess, pos, side);
		}

		
		public boolean _canProvidePower(BlockState state) {
			// TODO Auto-generated method stub
			return super.canProvidePower(state);
		}

		
		public void _onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
			// TODO Auto-generated method stub
			super.onEntityCollision(state, worldIn, pos, entityIn);
		}

		
		public int _getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
			// TODO Auto-generated method stub
			return super.getStrongPower(blockState, blockAccess, pos, side);
		}

		
		public void _harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te,
				ItemStack stack) {
			// TODO Auto-generated method stub
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		}

		
		public void _onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer,
				ItemStack stack) {
			// TODO Auto-generated method stub
			super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		}

		
		public boolean _canSpawnInBlock() {
			// TODO Auto-generated method stub
			return super.canSpawnInBlock();
		}

		
		public ITextComponent _getNameTextComponent() {
			// TODO Auto-generated method stub
			return super.getNameTextComponent();
		}

		
		public String _getTranslationKey() {
			// TODO Auto-generated method stub
			return super.getTranslationKey();
		}

		
		public boolean _eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
			// TODO Auto-generated method stub
			return super.eventReceived(state, worldIn, pos, id, param);
		}

		
		public PushReaction _getPushReaction(BlockState state) {
			// TODO Auto-generated method stub
			return super.getPushReaction(state);
		}

		
		public float _getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getAmbientOcclusionLightValue(state, worldIn, pos);
		}

		
		public void _onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
			// TODO Auto-generated method stub
			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		}

		
		public void _onLanded(IBlockReader worldIn, Entity entityIn) {
			// TODO Auto-generated method stub
			super.onLanded(worldIn, entityIn);
		}

		
		public ItemStack _getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
			// TODO Auto-generated method stub
			return super.getItem(worldIn, pos, state);
		}

		
		public void _fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
			// TODO Auto-generated method stub
			super.fillItemGroup(group, items);
		}

		
		public IFluidState _getFluidState(BlockState state) {
			// TODO Auto-generated method stub
			return super.getFluidState(state);
		}

		
		public float _getSlipperiness() {
			// TODO Auto-generated method stub
			return super.getSlipperiness();
		}

		
		public float _getSpeedFactor() {
			// TODO Auto-generated method stub
			return super.getSpeedFactor();
		}

		
		public float _getJumpFactor() {
			// TODO Auto-generated method stub
			return super.getJumpFactor();
		}

		
		public long _getPositionRandom(BlockState state, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getPositionRandom(state, pos);
		}

		
		public void _onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {
			// TODO Auto-generated method stub
			super.onProjectileCollision(worldIn, state, hit, projectile);
		}

		
		public void _onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
			// TODO Auto-generated method stub
			super.onBlockHarvested(worldIn, pos, state, player);
		}

		
		public void _fillWithRain(World worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			super.fillWithRain(worldIn, pos);
		}

		
		public boolean _canDropFromExplosion(Explosion explosionIn) {
			// TODO Auto-generated method stub
			return super.canDropFromExplosion(explosionIn);
		}

		
		public boolean _hasComparatorInputOverride(BlockState state) {
			// TODO Auto-generated method stub
			return super.hasComparatorInputOverride(state);
		}

		
		public int _getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getComparatorInputOverride(blockState, worldIn, pos);
		}

		
		protected void _fillStateContainer(net.minecraft.state.StateContainer.Builder<Block, BlockState> builder) {
			// TODO Auto-generated method stub
			super.fillStateContainer(builder);
		}

		
		public StateContainer<Block, BlockState> _getStateContainer() {
			// TODO Auto-generated method stub
			return super.getStateContainer();
		}

		
		public OffsetType _getOffsetType() {
			// TODO Auto-generated method stub
			return super.getOffsetType();
		}

		
		public Vec3d _getOffset(BlockState state, IBlockReader worldIn, BlockPos pos) {
			// TODO Auto-generated method stub
			return super.getOffset(state, worldIn, pos);
		}

		
		public SoundType _getSoundType(BlockState state) {
			// TODO Auto-generated method stub
			return super.getSoundType(state);
		}

		
		public Item _asItem() {
			// TODO Auto-generated method stub
			return super.asItem();
		}

		
		public boolean _isVariableOpacity() {
			// TODO Auto-generated method stub
			return super.isVariableOpacity();
		}

		
		public String _toString() {
			// TODO Auto-generated method stub
			return super.toString();
		}

		
		public void _addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
				ITooltipFlag flagIn) {
			// TODO Auto-generated method stub
			super.addInformation(stack, worldIn, tooltip, flagIn);
		}

		
		public float _getSlipperiness(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
			// TODO Auto-generated method stub
			return super.getSlipperiness(state, world, pos, entity);
		}

		
		public ToolType _getHarvestTool(BlockState state) {
			// TODO Auto-generated method stub
			return super.getHarvestTool(state);
		}

		
		public int _getHarvestLevel(BlockState state) {
			// TODO Auto-generated method stub
			return super.getHarvestLevel(state);
		}

		
		public boolean _canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing,
				IPlantable plantable) {
			// TODO Auto-generated method stub
			return super.canSustainPlant(state, world, pos, facing, plantable);
		}
		
		
		
	}

	
}

