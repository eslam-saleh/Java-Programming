package GUI;

import javafx.beans.property.SimpleIntegerProperty;

public class ShipInfo {
    private final SimpleIntegerProperty shipSize;
    private final SimpleIntegerProperty shipCount;

    ShipInfo(Integer size, Integer count) {
        shipSize = new SimpleIntegerProperty(size);
        shipCount = new SimpleIntegerProperty(count);
    }

    public int getShipSize() {
        return shipSize.get();
    }

    public SimpleIntegerProperty shipSizeProperty() {
        return shipSize;
    }

    public void setShipSize(int shipSize) {
        this.shipSize.set(shipSize);
    }

    public int getShipCount() {
        return shipCount.get();
    }

    public SimpleIntegerProperty shipCountProperty() {
        return shipCount;
    }

    public void setShipCount(int shipCount) {
        this.shipCount.set(shipCount);
    }
}
