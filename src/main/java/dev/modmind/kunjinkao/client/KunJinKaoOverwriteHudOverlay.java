package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.KunJinKaoTheme;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public final class KunJinKaoOverwriteHudOverlay {

    private static final int OVERWRITE_MAX_TICKS = 40;
    private static final int PHASE_START_TICKS = 20;

    private KunJinKaoOverwriteHudOverlay() {}

    public static void render(GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (!KunJinKaoClientOverwriteEffects.isOverwriteActive()) return;
        float intensity = Math.min(1.0F, KunJinKaoClientOverwriteEffects.getMaxRemainingTicks() / (float) OVERWRITE_MAX_TICKS);
        int xpLeft = screenWidth / 2 - 91;
        int xpTop = screenHeight - 32 + 3;
        guiGraphics.fill(xpLeft, xpTop, xpLeft + 182, xpTop + 5, 0xCC000000);
        int band = (int) (24 + 40 * intensity);
        int arm = (int) (8 + 28 * intensity);
        int gray = 0x99C8C8C8;
        guiGraphics.fill(screenWidth / 2 - arm, 0, screenWidth / 2 + arm, band, gray);
        guiGraphics.fill(screenWidth / 2 - arm, screenHeight - band, screenWidth / 2 + arm, screenHeight, gray);
        guiGraphics.fill(0, screenHeight / 2 - arm, band, screenHeight / 2 + arm, gray);
        guiGraphics.fill(screenWidth - band, screenHeight / 2 - arm, screenWidth, screenHeight / 2 + arm, gray);
        renderAllPhases(guiGraphics, screenWidth, screenHeight);
        renderResidueMarkers(guiGraphics, screenWidth, screenHeight);
        renderEndMessage(guiGraphics, screenWidth, screenHeight);
        renderFadeOutFeedback(guiGraphics, screenWidth, screenHeight);
    }

    private static void renderAllPhases(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        for (int entityId : KunJinKaoClientOverwriteEffects.getActiveEntityIds()) {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.level != null ? mc.level.getEntity(entityId) : null;
            int phase = KunJinKaoClientOverwriteEffects.getCurrentPhase(entityId);
            int remaining = KunJinKaoClientOverwriteEffects.getRemainingTicks(entityId);
            int theme = KunJinKaoClientOverwriteEffects.getEntityTheme(entityId);
            switch (phase) {
                case 0 -> renderPhaseOne(guiGraphics, entity, screenWidth, screenHeight, remaining, theme);
                case 1 -> renderPhaseTwo(guiGraphics, entity, entityId, remaining, screenWidth, screenHeight, theme);
                case 2 -> renderPhaseThree(guiGraphics, entity, screenWidth, screenHeight, theme);
                default -> {}
            }
        }
    }

    private static void renderPhaseOne(GuiGraphics guiGraphics, Entity entity, int screenWidth, int screenHeight, int remaining, int theme) {
        if (entity == null) return;
        Minecraft mc = Minecraft.getInstance();
        KunJinKaoTheme.ThemeEntry entry = KunJinKaoTheme.get(theme);
        String text = resolveName(entry.stageOneText(), entity);
        String[] lines = text.split("\n", -1);
        int elapsed = Math.max(0, OVERWRITE_MAX_TICKS - remaining);
        int totalChars = 0;
        for (String line : lines) totalChars += line.length();
        int charsToShow = (int) (totalChars * Math.min(1.0F, elapsed / (float) PHASE_START_TICKS));
        int terminalX = 10, terminalY = 10, terminalWidth = 360, terminalHeight = 24 + lines.length * 12;
        guiGraphics.fill(terminalX, terminalY, terminalX + terminalWidth, terminalY + terminalHeight, 0x99000000);
        int charOffset = 0;
        for (int i = 0; i < lines.length; i++) {
            int visible = Math.max(0, Math.min(lines[i].length(), charsToShow - charOffset));
            guiGraphics.drawString(mc.font, lines[i].substring(0, visible), terminalX + 10, terminalY + 8 + i * 12, entry.phase1Color());
            charOffset += lines[i].length();
        }
        double[] screenPos = projectToScreen(mc, entity.getX(), entity.getY() + entity.getEyeHeight() + 0.45D, entity.getZ(), screenWidth, screenHeight);
        if (screenPos != null) guiGraphics.drawString(mc.font, "_", (int)(screenPos[0] - 3), (int)(screenPos[1] - 4), 0xFFFFFFFF);
    }

    private static void renderPhaseTwo(GuiGraphics guiGraphics, Entity entity, int entityId, int remaining, int screenWidth, int screenHeight, int theme) {
        Minecraft mc = Minecraft.getInstance();
        KunJinKaoTheme.ThemeEntry entry = KunJinKaoTheme.get(theme);
        int phaseTwoElapsed = Math.max(0, PHASE_START_TICKS - remaining);
        int linesToShow = Math.max(1, KunJinKaoTheme.stageTwoLinesVisible(theme, Math.min(40, phaseTwoElapsed * 2)));
        int windowWidth = 300, windowHeight = Math.max(64, 30 + linesToShow * 18);
        double anchorX = screenWidth / 2.0D, anchorY = screenHeight / 2.0D;
        if (entity != null) {
            double[] screenPos = projectToScreen(mc, entity.getX(), entity.getY() + entity.getEyeHeight() + 0.35D, entity.getZ(), screenWidth, screenHeight);
            if (screenPos != null) { anchorX = screenPos[0]; anchorY = screenPos[1]; }
        }
        int windowX = Math.max(4, Math.min(screenWidth - windowWidth - 4, (int)(anchorX - windowWidth / 2.0D)));
        int windowY = Math.max(4, Math.min(screenHeight - windowHeight - 4, (int)(anchorY - windowHeight - 16)));
        guiGraphics.fill(windowX, windowY, windowX + windowWidth, windowY + windowHeight, 0x99000000);
        for (int i = 0; i < linesToShow; i++) guiGraphics.drawString(mc.font, resolveName(entry.stageTwoLogs()[i], entity), windowX + 10, windowY + 8 + i * 18, entry.phase2Color());
        KunJinKaoClientOverwriteEffects.markKeyboardLinePlayed(entityId, linesToShow - 1);
    }

    private static void renderPhaseThree(GuiGraphics guiGraphics, Entity entity, int screenWidth, int screenHeight, int theme) {
        Minecraft mc = Minecraft.getInstance();
        KunJinKaoTheme.ThemeEntry entry = KunJinKaoTheme.get(theme);
        String raw = resolveName(entry.stageThreeText(), entity);
        String[] lines = raw.split("\n", -1);
        float scale = 1.3F;
        double anchorX = screenWidth / 2.0D, anchorY = screenHeight / 2.0D - 80;
        if (entity != null) {
            double[] screenPos = projectToScreen(mc, entity.getX(), entity.getY() + entity.getEyeHeight() + 0.6D, entity.getZ(), screenWidth, screenHeight);
            if (screenPos != null) { anchorX = screenPos[0]; anchorY = screenPos[1] - 44; }
        }
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.isEmpty()) continue;
            int color = line.startsWith(">>>") ? 0xFFFF0000 : entry.phase3Color();
            int textWidth = mc.font.width(line);
            int textX = (int)(anchorX - textWidth * scale / 2.0D);
            int textY = (int)(anchorY - lines.length * 18.0D * scale / 2.0D + i * 18.0D * scale);
            textX = Math.max(4, Math.min(screenWidth - (int)(textWidth * scale) - 4, textX));
            textY = Math.max(4, Math.min(screenHeight - 20, textY));
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(textX, textY, 0);
            guiGraphics.pose().scale(scale, scale, 1.0F);
            guiGraphics.drawString(mc.font, line, -1, 0, 0x000000);
            guiGraphics.drawString(mc.font, line, 1, 0, 0x000000);
            guiGraphics.drawString(mc.font, line, 0, -1, 0x000000);
            guiGraphics.drawString(mc.font, line, 0, 1, 0x000000);
            guiGraphics.drawString(mc.font, line, 0, 0, color);
            guiGraphics.pose().popPose();
        }
    }

    private static void renderResidueMarkers(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        Map<Integer, KunJinKaoClientOverwriteEffects.ResidueMarker> markers = KunJinKaoClientOverwriteEffects.getResidueMarkers();
        for (KunJinKaoClientOverwriteEffects.ResidueMarker marker : markers.values()) {
            double[] screenPos = projectToScreen(mc, marker.x(), marker.y(), marker.z(), screenWidth, screenHeight);
            if (screenPos == null) continue;
            int x = (int) screenPos[0], y = (int) screenPos[1];
            int theme = marker.theme();
            float alpha = 1.0F - marker.ageTicks() / (float) KunJinKaoClientOverwriteEffects.getResidueTicks();
            int color = KunJinKaoTheme.get(theme).phase3Color() | ((int)(alpha * 255) << 24);
            guiGraphics.drawString(mc.font, "?", x - 3, y - 8, color);
        }
    }

    private static void renderEndMessage(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        if (KunJinKaoClientOverwriteEffects.getEndMessageTicks() <= 0) return;
        Minecraft mc = Minecraft.getInstance();
        float alpha = KunJinKaoClientOverwriteEffects.getEndMessageTicks() / (float) KunJinKaoClientOverwriteEffects.END_MESSAGE_TICKS;
        int color = (int)(alpha * 255) << 24 | 0x000000;
        String message = String.format("> 执行完毕。共清除 1 个对象：%s", KunJinKaoClientOverwriteEffects.getLastEndName());
        int textX = screenWidth / 2 - mc.font.width(message) / 2;
        int textY = screenHeight / 2 + 30;
        guiGraphics.drawString(mc.font, message, textX, textY, color | 0xFF000000);
    }

    private static void renderFadeOutFeedback(GuiGraphics guiGraphics, int screenWidth, int screenHeight) {
        int fadeOutTicks = KunJinKaoClientOverwriteEffects.getFadeOutTicks();
        if (fadeOutTicks <= 0) return;
        Minecraft mc = Minecraft.getInstance();
        float alpha = fadeOutTicks <= 5 ? fadeOutTicks / 5.0F : 1.0F - (fadeOutTicks - 5) / 15.0F;
        String text = "> 目标已清除";
        int textX = screenWidth / 2 - mc.font.width(text) / 2;
        int textY = screenHeight * 3 / 5;
        int color = (int)(alpha * 0xAA) << 24 | 0xAAAAAA;
        guiGraphics.drawString(mc.font, text, textX, textY, color);
    }

    private static double[] projectToScreen(Minecraft mc, double x, double y, double z, int screenWidth, int screenHeight) {
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 vec3 = camera.getPosition();
        double dx = x - vec3.x, dy = y - vec3.y, dz = z - vec3.z;
        double dotX = (double)camera.getUpVector().x() * dx + (double)camera.getUpVector().y() * dy + (double)camera.getUpVector().z() * dz;
        // 1.21.1 的 Camera 只有 getLeftVector();右向量取其相反方向
        double dotY = (double)(-camera.getLeftVector().x()) * dx
                    + (double)(-camera.getLeftVector().y()) * dy
                    + (double)(-camera.getLeftVector().z()) * dz;
        double dotZ = (double)camera.getLookVector().x() * dx + (double)camera.getLookVector().y() * dy + (double)camera.getLookVector().z() * dz;
        if (dotZ < 0.05D) return null;
        double fov = Math.toRadians(70.0D);
        double fovX = 2.0D * Math.atan(Math.tan(fov / 2.0D) * (double)screenWidth / (double)mc.getWindow().getGuiScaledHeight());
        double fovY = 2.0D * Math.atan(Math.tan(fov / 2.0D) * (double)screenHeight / (double)mc.getWindow().getGuiScaledWidth());
        double posX = dotX / dotZ;
        double posY = dotY / dotZ;
        double positiveZ = dotZ > 0.0D ? dotZ : -dotZ;
        double guiScaleX = screenWidth / mc.getWindow().getGuiScaledWidth();
        double guiScaleY = screenHeight / mc.getWindow().getGuiScaledHeight();
        double screenX = screenWidth / 2.0D + posX * screenWidth / Math.tan(Math.toRadians(fovX / 2.0D)) / positiveZ;
        double screenY = screenHeight / 2.0D - posY * screenHeight / Math.tan(Math.toRadians(fovY / 2.0D)) / positiveZ;
        return new double[]{screenX, screenY};
    }

    private static String resolveName(String template, Entity entity) {
        String name = entity != null ? entity.getDisplayName().getString() : "目标";
        return template.replace("{name}", name);
    }
}
