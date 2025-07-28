package dev.rebby.yumecraft.util;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.config.YumeCraftConfigModel;
import dev.rebby.yumecraft.tag.ModStructureTags;
import io.wispforest.owo.config.ConfigSynchronizer;
import io.wispforest.owo.config.Option;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiling.jfr.event.ServerTickTimeEvent;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptionsRegistryHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DimensionalTeleportationHandler {

    public static final int TELEPORTATION_SUCCESSFUL = 0;
    public static final int FAILED_TELEPORTATION = 1;
    public static final int TELEPORTED_WITHIN_WORLD = 2;

    private static List<DimensionalTeleportationHandler> handlers;

    private State state;
    private final ServerPlayerEntity user;

    public DimensionalTeleportationHandler(ServerPlayerEntity user, ServerWorld world) {
        this.user = user;
        this.state = findWorldState(world);
    }

    public static void init(){
        handlers = new ArrayList<>();

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity player) {
                handlers.removeIf(handler -> handler.user.equals(entity));
                handlers.add(new DimensionalTeleportationHandler((player), world));
            }
        });

//        ServerWorldEvents.LOAD.register((server, world) -> {
//            List<ServerPlayerEntity> players = world.getPlayers();
//            for (int i = 0; i < players.size(); i++) {
//                boolean add = true;
//
//                for (int j = 0; j < handlers.size(); j++) {
//                    if (handlers.get(j).user.equals(players.get(i))) {
//                        add = false;
//                        break;
//                    }
//                }
//
//                if (add) {
//                    handlers.add(new DimensionalTeleportationHandler(players.get(i)));
//                }
//            }
//        });

//        ServerTickEvents.START_SERVER_TICK.register((server) -> {
//            List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
//            for (int i = 0; i < players.size(); i++) {
//
//                handlers.add(new DimensionalTeleportationHandler(players.get(i)));
//            }
//        });

        EntitySleepEvents.START_SLEEPING.register((entity, pos) -> {
            if (entity.getWorld().isClient) {
                return;
            }

            YumeCraft.LOGGER.info("Finding sleeping players in size {}", handlers.size());

            for (DimensionalTeleportationHandler handler : handlers) {
                if (handler.user.equals(entity)) {
                    YumeCraft.LOGGER.info("Found sleeping player!");

                    Map<Identifier, Integer> map = YumeCraftConfigModel.getDefaultSleepingTeleportation();

                    Map<Option.Key, ?> options = ConfigSynchronizer.getClientOptions(handler.user, YumeCraft.CONFIG);
                    if (options != null && options.get(YumeCraft.CONFIG.keys.sleepingTeleportation) != null) {
                        map = (Map<Identifier, Integer>) options.get(YumeCraft.CONFIG.keys.sleepingTeleportation);
                    }

                    Random random = entity.getRandom();
                    int bound = 0;

                    for (Integer i : map.values()) {
                        bound += i;
                    }

                    if (bound == 0) {
                        bound = 1;
                    }

                    bound = random.nextInt(bound);

                    Identifier dim = Identifier.ofVanilla("empty");

                    for (Identifier id : map.keySet()) {
                        bound -= map.get(id);
                        dim = id;
                        if (bound < 0){
                            break;
                        }
                    }

                    if (dim.equals(Identifier.ofVanilla("empty"))) {
                        return;
                    }

                    handler.state = handler.state.loadDimension(dim);
                    YumeCraft.LOGGER.info("Loaded Dimension");
                    return;

                }
            }
        });

        EntitySleepEvents.STOP_SLEEPING.register((entity, pos) ->{
            if (entity.getWorld().isClient) {
                return;
            }

            YumeCraft.LOGGER.info("Finding waking players in size {}", handlers.size());
            for (int i = 0; i < handlers.size(); i++) {
                DimensionalTeleportationHandler handler = handlers.get(i);
                if (handler.user.equals(entity)) {
                    YumeCraft.LOGGER.info("Found waking player!");
                    handler.state = handler.state.teleportToDimension();
                    YumeCraft.LOGGER.info("Teleported to new Dimension");
                    return;

                }
            }
        });

//        EntitySleepEvents.ALLOW_RESETTING_TIME.register(player -> {
//            if (player.getWorld().isClient) {
//                return true;
//            }
//
//            System.out.println("Finding players in size " + handlers.size());
//            for (int i = 0; i < handlers.size(); i++) {
//                DimensionalTeleportationHandler handler = handlers.get(i);
//                if (handler.user.equals(player)) {
//                    System.out.println("Found player!");
//                    System.out.println(handler.state.teleportToDimension(ModDimensions.POINT_NEMO));
//                    System.out.println("New Dimension");
//                    return true;
//
//                }
//            }
//
//            return true;
//        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            for (int i = 0; i < handlers.size(); i++) {
                DimensionalTeleportationHandler handler = handlers.get(i);
                if (handler.user.equals(player)) {
                    handler.state = handler.state.enterDimension();
                    return;
                }
            }
        });

//        ServerTickEvents.START_WORLD_TICK.register(serverWorld->{
//            for (int i = 0; i < handlers.size(); i++) {
//                DimensionalTeleportationHandler handler = handlers.get(i);
//                if (serverWorld.getPlayers().contains(handler.user)) {
//                    handler.state.teleportedIntoDimension(5, handler.user.getYaw(), handler.user.getPitch());
//                }
//            }
//        });




    }

    private static BlockPos getFlattestGroundFromTop(ServerWorld world, BlockPos pos, int distance, int c){
        int min = getMaxCurveFromTop(world, pos, c);
        BlockPos minPos = pos;

        for (int x = -distance; x <= distance; x++) {
            for (int z = -distance; z <= distance; z++) {
                BlockPos valuePos = pos.add(x, 0, z);
                int value = getMaxCurveFromTop(world, valuePos, c);
                //System.out.print(value + " ");
                if ((x != 0 || z != 0) && min > value) {
                    min = value;
                    minPos = valuePos;
                }
            }
            //System.out.println();
        }

        return getHightestBlockPos(world, minPos);
    }

    private static int getMaxCurveFromTop(ServerWorld world, BlockPos pos, int r) {

        int max = getHightestBlockPos(world, pos).getY();
        int min = getHightestBlockPos(world, pos).getY();

        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                if (x != 0 || z != 0) {
                    int value = getHightestBlockPos(world, pos.add(x, 0, z)).getY();

                    max = Math.max(max, value);
                    min = Math.min(min, value);
                }
            }
        }

        return max - min;
    }

    private static BlockPos getHightestBlockPos(ServerWorld world, BlockPos pos) {
        return new BlockPos(pos.getX(), world.getTopY(Heightmap.Type.WORLD_SURFACE,
                pos.getX(), pos.getZ()), pos.getZ());
    }

    private State findWorldState(World world) {
        return findWorldState(world.getRegistryKey().getValue());
    }

    private State findWorldState(Identifier dimId) {
        YumeCraft.LOGGER.info("Finding world state...");
        String namespace = dimId.getNamespace();
        String path = dimId.getPath();
        YumeCraft.LOGGER.info("{}:{}", namespace, path);

        if (namespace.equals("minecraft") && path.equals("overworld")) {
            YumeCraft.LOGGER.info("OverworldState");
            return new OverworldState();
        }

        return new UnknownDimensionState();
    }

    private State findWorldState() {
        return findWorldState(user.getWorld());
    }

    private abstract class State{

        public abstract State teleportToDimension();

        public abstract State loadDimension(Identifier dimId);

        public State enterDimension() {
            return findWorldState();
        }
    }

    private class OverworldState extends State {
        private OverworldState() {

        }

        @Override
        public State teleportToDimension() {
            return this;
        }

        @Override
        public State loadDimension(Identifier dimId) {
            State newState;

            if (dimId.equals(ModDimensions.POINT_NEMO)) {
                newState = new TeleportToSetTopState(new BlockPos(0,0,0), 12, dimId);
            }
            else if (dimId.equals(ModDimensions.VERDANT_TEMPLE)) {
                newState = new TeleportToSetTopState(user.getBlockPos(), 8, dimId);
            }
            else if (dimId.equals(ModDimensions.INFINITE_MALL)) {
                newState = new InfiniteMallTeleporterState();
            }
            else if (!dimId.equals(Identifier.ofVanilla("overworld"))) {
                newState = new TeleportToSetTopState(user.getBlockPos(), 4, dimId);
            }
            else {
                return this;
            }

            YumeCraft.LOGGER.info("Loading Dimension");

            return newState.loadDimension(dimId);
        }
    }

    private class UnknownDimensionState extends State{

        private UnknownDimensionState(){}


        @Override
        public State teleportToDimension() {
            return this;
        }

        @Override
        public State loadDimension(Identifier dimId) {
            return this;
        }
    }

    private class InfiniteMallTeleporterState extends State{

        @Override
        public State teleportToDimension() {
            return this;
        }

        @Override
        public State loadDimension(Identifier dimId) {
            MinecraftServer server = ((ServerWorld)user.getWorld()).getServer();
            RegistryKey<World> dimKey = RegistryKey.of(RegistryKeys.WORLD, dimId);
            ServerWorld world = server.getWorld(dimKey);

            if (world == null) {
                return findWorldState();
            }

            BlockPos pos = user.getBlockPos();

            BlockPos nearestMall = world.locateStructure(ModStructureTags.INFINITE_MALL_MALL, pos, 6, false);

            if (nearestMall != null){
                Vec3d distance = pos.add(-nearestMall.getX(), -pos.getY(), -nearestMall.getZ()).toCenterPos().normalize().multiply(8*16);
                pos = nearestMall.add((int)(distance.x), (int)(distance.y), (int)(distance.z));
            }

            State newState = new TeleportToSetTopState(pos, 5, dimId);

            return newState.loadDimension(dimId);
        }
    }

    private class TeleportToSetTopState extends State {

        private final BlockPos setPos;
        private final int distance;
        private final Identifier dimId;

        private TeleportToSetTopState(BlockPos setPos, int distance, Identifier dimId) {
            this.setPos = setPos;
            this.distance = distance;
            this.dimId = dimId;
        }

        @Override
        public State teleportToDimension() {

            MinecraftServer server = ((ServerWorld)user.getWorld()).getServer();
            RegistryKey<World> dimKey = RegistryKey.of(RegistryKeys.WORLD, dimId);
            ServerWorld worldTo = server.getWorld(dimKey);

            YumeCraft.LOGGER.info("trying to teleport...");

            if (worldTo == null || user.getWorld().getRegistryKey().equals(dimKey)) {
                return findWorldState();
            }

            Vec3d teleportedLoc = getFlattestGroundFromTop(worldTo, setPos, distance, 2)
                    .add(0,1,0).toCenterPos();

            YumeCraft.LOGGER.info("Flattest Point: {}", teleportedLoc.y);

            TeleportTarget target = new TeleportTarget(worldTo,
                    teleportedLoc,
                    user.getVelocity(),
                    user.getYaw(),
                    user.getPitch(),
                    TeleportTarget.NO_OP);

            user.teleportTo(target);


            return findWorldState(worldTo);
        }

        @Override
        public State loadDimension(Identifier dimId) {

            YumeCraft.LOGGER.info("Trying to load {}...", dimId);

            MinecraftServer server = ((ServerWorld)user.getWorld()).getServer();
            RegistryKey<World> dimKey = RegistryKey.of(RegistryKeys.WORLD, dimId);
            ServerWorld world = server.getWorld(dimKey);

            if (world == null || user.getWorld().getRegistryKey().equals(dimKey)) {
                return findWorldState();
            }

            YumeCraft.LOGGER.info("Can load {}!", dimId);

            world.getChunkManager().addTicket(ChunkTicketType.PORTAL, new ChunkPos(setPos), distance, setPos);

            return this;
        }
    }

}
