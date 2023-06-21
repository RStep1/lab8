package data;

public class TableRowVehicle {
    private long id;
    private long key;
    private String name;
    private float xCoordinate;
    private double yCoordinate;
    private int enginePower;
    private long distanceTravelled;
    private String vehicleType;
    private String fuelType;
    private String creationDate;
    private String owner;

    public TableRowVehicle(long id, long key, String name, float xCoordinate, double yCoordinate, int enginePower,
                            long distanceTravelled, String vehicleType, String fuelType, String creationDate, String owner) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.enginePower = enginePower;
        this.distanceTravelled = distanceTravelled;
        this.vehicleType = vehicleType;
        this.fuelType = fuelType;
        this.creationDate = creationDate;
        this.owner = owner;                             
    }

    public long getId() {
        return id;
    }

    public long getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public float getXCoordinate() {
        return xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }
    
    public String getCreationDate() {
        return creationDate;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public long getDistanceTravelled() {
        return distanceTravelled;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getOwner() {
        return owner;
    }


    @Override
    public String toString() {
        return String.format("""
           id:                  %s
           key:                 %s
           name:                %s
           x:                   %s
           y:                   %s
           enginePower:         %s
           distanceTravelled:   %s
           vehicleType:         %s
           fuelType:            %s
           creationDate:        %s
           owner:               %s
        """, id, key, name, xCoordinate, yCoordinate, enginePower, distanceTravelled,
            vehicleType, fuelType, creationDate, owner);
    }
}  
