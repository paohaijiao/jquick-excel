/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) [2025-2099] Martin (goudingcheng@gmail.com)
 */
package com.github.paohaijiao.theme;

/**
 * RGB 颜色值。
 * 用于主题中精确的自定义颜色，配合 {@code XSSFWorkbook} 的 {@code XSSFColor} 使用，
 * 相比 {@code IndexedColors} 能呈现更丰富的视觉效果。
 *
 * @author Martin
 * @version 1.0.0
 * @date 2026/8/7
 */
public final class JRgbColor {

    private final int r;
    private final int g;
    private final int b;

    public JRgbColor(int r, int g, int b) {
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
    }

    public static JRgbColor of(int r, int g, int b) {
        return new JRgbColor(r, g, b);
    }

    /**
     * 通过十六进制字符串构造，例如 {@code JRgbColor.of("1F4E79")} 或 {@code JRgbColor.of("#1F4E79")}。
     */
    public static JRgbColor of(String hex) {
        String h = hex.startsWith("#") ? hex.substring(1) : hex;
        if (h.length() != 6) {
            throw new IllegalArgumentException("invalid hex color: " + hex);
        }
        int r = Integer.parseInt(h.substring(0, 2), 16);
        int g = Integer.parseInt(h.substring(2, 4), 16);
        int b = Integer.parseInt(h.substring(4, 6), 16);
        return new JRgbColor(r, g, b);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public byte[] toBytes() {
        return new byte[]{(byte) r, (byte) g, (byte) b};
    }

    private static int clamp(int v) {
        return v < 0 ? 0 : (v > 255 ? 255 : v);
    }
}
