package zlaire.emotion.player;

import java.util.HashMap;
import java.util.Map;

public class beforeChestStates {
    static float Health;
    static int ArmorValue;
    static int FoodLevel;
    static Map<String, Integer> InventoryMap=new HashMap<>();

    public beforeChestStates(){
        Health=0;
        ArmorValue=0;
        FoodLevel=0;
    }
    public static void change(float h, int av, int fl, Map<String, Integer> inv){
        Health=h;
        ArmorValue=av;
        FoodLevel=fl;
        InventoryMap.clear();
        InventoryMap.putAll(inv);
    }

    public static float getHealth() {
        return Health;
    }

    public static int getArmorValue() {
        return ArmorValue;
    }

    public static int getFoodLevel() {
        return FoodLevel;
    }

    public static Map<String, Integer> getInventoryMap() {
        return InventoryMap;
    }

}