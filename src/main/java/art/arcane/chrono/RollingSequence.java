/*
 * Amulet is an extension api for Java
 * Copyright (c) 2022 Arcane Arts
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package art.arcane.chrono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RollingSequence extends Average {
    private double median;
    private double max;
    private double min;
    private boolean dirtyMedian;
    private int dirtyExtremes;
    private boolean precision;

    public RollingSequence(int size) {
        super(size);
        median = 0;
        min = 0;
        max = 0;
        setPrecision(false);
    }

    public double addLast(int amt) {
        double f = 0;

        for (int i = 0; i < Math.min(values.length, amt); i++) {
            f += values[i];
        }

        return f;
    }

    public boolean isPrecision() {
        return precision;
    }

    public void setPrecision(boolean p) {
        this.precision = p;
    }

    public double getMin() {
        if (dirtyExtremes > (isPrecision() ? 0 : values.length)) {
            resetExtremes();
        }

        return min;
    }

    public double getMax() {
        if (dirtyExtremes > (isPrecision() ? 0 : values.length)) {
            resetExtremes();
        }

        return max;
    }

    public double getMedian() {
        if (dirtyMedian) {
            recalculateMedian();
        }

        return median;
    }

    private void recalculateMedian() {
        List<Double> d = new ArrayList<>();

        for(double i : values) {
            d.add(i);
        }

        d.sort(Comparator.comparing(Double::doubleValue));
        median = d.get(d.size() % 2 == 0 ? (d.size() / 2) : ((d.size() / 2) + 1));
        dirtyMedian = false;
    }

    public void resetExtremes() {
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;

        for (double i : values) {
            max = Math.max(max, i);
            min = Math.min(min, i);
        }

        dirtyExtremes = 0;
    }

    public void put(double i) {
        super.put(i);
        dirtyMedian = true;
        dirtyExtremes++;
        max = Math.max(max, i);
        min = Math.min(min, i);
    }
}