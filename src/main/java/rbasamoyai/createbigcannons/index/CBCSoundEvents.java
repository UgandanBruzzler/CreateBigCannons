package rbasamoyai.createbigcannons.index;

import static com.simibubi.create.AllSoundEvents.SoundEntry;
import static com.simibubi.create.AllSoundEvents.SoundEntryBuilder;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCSoundEvents {

	public static final Map<ResourceLocation, SoundEntry> ALL = new HashMap<>();

	public static final SoundEntry
		AUTOCANNON_ROUND_FLYBY = create("autocannon_round_flyby").subtitle("Autocannon round flies by")
			.category(SoundSource.NEUTRAL)
			.build(),

		FIRE_BIG_CANNON = create("fire_big_cannon").subtitle("Big cannon fires")
			.category(SoundSource.BLOCKS)
			.build(),

		FIRE_AUTOCANNON = create("fire_autocannon").subtitle("Autocannon fires")
			.category(SoundSource.BLOCKS)
			.build(),

		FIRE_MACHINE_GUN = create("fire_machine_gun").subtitle("Machine gun fires")
			.category(SoundSource.BLOCKS)
			.build(),

		FIRE_DROP_MORTAR = create("fire_drop_mortar").subtitle("Drop mortar fires")
			.addVariant("fire_drop_mortar1")
			.category(SoundSource.BLOCKS)
			.build(),

		FLAK_ROUND_EXPLOSION = create("flak_round_explosion").subtitle("Flak round explodes")
			.category(SoundSource.NEUTRAL)
			.build(),

		FLUID_SHELL_EXPLOSION = create("fluid_shell_explosion").subtitle("Fluid shell explodes")
			.category(SoundSource.NEUTRAL)
			.build(),

		HOT_PROJECTILE_SPLASH = create("hot_projectile_splash").subtitle("Projectile splashes")
			.playExisting(SoundEvents.BUCKET_EMPTY)
			.build(),

		LAVA_FLUID_RELEASE = create("lava_fluid_release").noSubtitle()
			.playExisting(SoundEvents.FIRECHARGE_USE)
			.category(SoundSource.NEUTRAL)
			.build(),

		MACHINE_GUN_ROUND_FLYBY = create("machine_gun_round_flyby").subtitle("Machine gun round flies by")
			.category(SoundSource.NEUTRAL)
			.build(),

		MORTAR_STONE_EXPLODE = create("mortar_stone_explode").subtitle("Mortar stone explodes")
			.addVariant("mortar_stone_explode1")
			.addVariant("mortar_stone_explode2")
			.category(SoundSource.NEUTRAL)
			.build(),

		PLACE_AUTOCANNON_AMMO_CONTAINER = create("place_autocannon_ammo_container").noSubtitle()
			.playExisting(SoundEvents.ARMOR_EQUIP_IRON, 0.25f, 1.0f)
			.category(SoundSource.BLOCKS)
			.build(),

		POTION_FLUID_RELEASE = create("potion_fluid_release").noSubtitle()
			.playExisting(SoundEvents.GENERIC_EXTINGUISH_FIRE)
			.category(SoundSource.NEUTRAL)
			.build(),

		PROJECTILE_IMPACT = create("projectile_impact").subtitle("Projectile impacts")
			.playExisting(MORTAR_STONE_EXPLODE::getMainEvent, 1, 1)
			.category(SoundSource.BLOCKS)
			.build(),

		PROJECTILE_SPLASH = create("projectile_splash").subtitle("Projectile splashes")
			.addVariant("projectile_splash1")
			.category(SoundSource.BLOCKS)
			.build(),

		SHELL_EXPLOSION = create("shell_explosion").subtitle("Artillery shell explodes")
			.addVariant("shell_explosion1")
			.addVariant("shell_explosion2")
			.category(SoundSource.BLOCKS)
			.build(),

		SHELL_FLYING = create("shell_flying").subtitle("Incoming artillery shell")
			.category(SoundSource.NEUTRAL)
			.attenuationDistance(256)
			.build(),

		SHRAPNEL_SHELL_EXPLOSION = create("shrapnel_shell_explosion").subtitle("Shrapnel shell explodes")
			.category(SoundSource.NEUTRAL)
			.build(),

		SMOKE_SHELL_DETONATE = create("smoke_shell_detonate").subtitle("Smoke shell bursts")
			.playExisting(SoundEvents.GENERIC_EXTINGUISH_FIRE)
			.category(SoundSource.NEUTRAL)
			.build(),

		WATER_FLUID_RELEASE = create("water_fluid_release").noSubtitle()
			.playExisting(SoundEvents.BUCKET_EMPTY)
			.category(SoundSource.NEUTRAL)
			.build(),

		WOOD_SPLINTERS = create("wood_splinters").noSubtitle()
			.addVariant("wood_splinters1")
			.addVariant("wood_splinters2")
			.addVariant("wood_splinters3")
			.category(SoundSource.BLOCKS)
			.build();

	private static SoundEntryBuilder create(String id) {
		return new CBCSoundEntryBuilder(CreateBigCannons.resource(id));
	}

	public static void prepare() {
		for (SoundEntry entry : ALL.values())
			entry.prepare();
	}

	public static void register(Consumer<SoundEntry> consumer) {
		for (SoundEntry entry : ALL.values())
			consumer.accept(entry);
	}

	public static void registerLangEntries() {
		for (SoundEntry entry : ALL.values()) {
			if (entry.hasSubtitle())
				CreateBigCannons.REGISTRATE.addRawLang(entry.getSubtitleKey(), entry.getSubtitle());
		}
	}

	public static class CBCSoundEntryBuilder extends SoundEntryBuilder {
		public CBCSoundEntryBuilder(ResourceLocation id) {
			super(id);
		}

		@Override
		public SoundEntryBuilder addVariant(String name) {
			return this.addVariant(CreateBigCannons.resource(name));
		}

		@Override
		public SoundEntry build() {
			SoundEntry entry = super.build();
			ALL.put(entry.getId(), entry);
			return entry;
		}
	}

	public static SoundEntryProvider provider(PackOutput output) {
		return new SoundEntryProvider(output);
	}

	public static class SoundEntryProvider implements DataProvider {
		private PackOutput output;

		public SoundEntryProvider(PackOutput output) {
			this.output = output;
		}

		@Override
		public CompletableFuture<?> run(CachedOutput cache) {
			Path path = this.output.getOutputFolder().resolve("assets/" + CreateBigCannons.MOD_ID);
			JsonObject json = new JsonObject();
			ALL.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> entry.getValue().write(json));
			return DataProvider.saveStable(cache, json, path.resolve("sounds.json"));
		}

		@Override public String getName() { return "Create Big Cannons custom sounds"; }
	}

}
