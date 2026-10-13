/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client;

import java.util.List;
import java.util.Set;
import net.neoforged.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class MixinGate
implements IMixinConfigPlugin {
    private static final String MINECRAFT = "1.21.1";
    private static final List<String> MIXINS = List.of("CameraMixin", "FontTextureMixin", "GameRendererMixin", "GlyphFilterMixin", "GuiMixin", "KeyboardHandlerMixin", "KeyboardInputMixin", "LevelRendererMixin", "MouseHandlerMixin", "MouseTurnMixin", "PlayerModelPoseMixin", "SoundBufferLibraryMixin", "SoundEngineMixin");

    public List<String> getMixins() {
        boolean bl = MixinEnvironment.getCurrentEnvironment().getSide() == MixinEnvironment.Side.CLIENT;
        return bl && MINECRAFT.equals(MixinGate.running()) ? MIXINS : List.of();
    }

    private static String running() {
        try {
            return FMLLoader.versionInfo().mcVersion();
        }
        catch (LinkageError linkageError) {
            return "";
        }
    }

    public void onLoad(String string) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String string, String string2) {
        return true;
    }

    public void acceptTargets(Set<String> set, Set<String> set2) {
    }

    public void preApply(String string, ClassNode classNode, String string2, IMixinInfo iMixinInfo) {
    }

    public void postApply(String string, ClassNode classNode, String string2, IMixinInfo iMixinInfo) {
    }
}

