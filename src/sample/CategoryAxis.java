package sample;

import javafx.scene.chart.Axis;

import java.util.List;

public class CategoryAxis extends Axis<String> {
    @Override
    protected Object autoRange(double length) {
        return null;
    }

    @Override
    protected void setRange(Object range, boolean animate) {

    }

    @Override
    protected Object getRange() {
        return null;
    }

    @Override
    public double getZeroPosition() {
        return 0;
    }

    @Override
    public double getDisplayPosition(String value) {
        return 0;
    }

    @Override
    public String getValueForDisplay(double displayPosition) {
        return null;
    }

    @Override
    public boolean isValueOnAxis(String value) {
        return false;
    }

    @Override
    public double toNumericValue(String value) {
        return 0;
    }

    @Override
    public String toRealValue(double value) {
        return null;
    }

    @Override
    protected List<String> calculateTickValues(double length, Object range) {
        return null;
    }

    @Override
    protected String getTickMarkLabel(String value) {
        return null;
    }
}
