package zlaire.emotion.tools;

public class myItem {
    int x;
    int y;
    int z;
    public myItem(double x, double y, double z){
        this.x=(int)x;
        this.y=(int)y;
        this.z=(int)z;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof myItem item)) return false;

        if (Double.compare(item.x, x) != 0) return false;
        if (Double.compare(item.y, y) != 0) return false;
        return Double.compare(item.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

