package zlaire.emotion.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class States {
    static float Health;
    static int ArmorValue;
    static int FoodLevel;
    static Map<String, Integer> InventoryMap=new HashMap<>();
    static float oldHealth;
    static int oldArmorValue;
    static int oldFoodLevel;
    static Map<String, Integer> oldInventoryMap=new HashMap<>();
    public States(float h, int av, int fl, Map<String, Integer> inv){
        Health=h;
        ArmorValue=av;
        FoodLevel=fl;
        InventoryMap.putAll(inv);
    }


    public static void change (float h, int av, int fl, Map<String, Integer> inv){
        if(!compareVariables(h, av, fl, inv)){
            oldHealth=Health;
            oldArmorValue=ArmorValue;
            oldFoodLevel=FoodLevel;
            oldInventoryMap.clear();
            oldInventoryMap.putAll(InventoryMap);
            Health=h;
            ArmorValue=av;
            FoodLevel=fl;
            InventoryMap.clear();
            InventoryMap.putAll(inv);
        }
    }
    public static void forceChange (float h, int av, int fl, Map<String, Integer> inv){
        oldHealth=Health;
        oldArmorValue=ArmorValue;
        oldFoodLevel=FoodLevel;
        oldInventoryMap.clear();
        oldInventoryMap.putAll(InventoryMap);
        Health=h;
        ArmorValue=av;
        FoodLevel=fl;
        InventoryMap.clear();
        InventoryMap.putAll(inv);
    }
    public static boolean compareVariables(float h, int av, int fl, Map<String, Integer> inv) {
        boolean isEqual = true;

        if (h != oldHealth) {
            isEqual = false;
        }

        if (av != oldArmorValue) {
            isEqual = false;
        }

        if (fl != oldFoodLevel) {
            isEqual = false;
        }

        if (!inv.equals(oldInventoryMap)) {
            System.out.println("unequal");
            isEqual = false;
        }

        return isEqual;
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

    public static float getOldHealth() {
        return oldHealth;
    }

    public static int getOldArmorValue() {
        return oldArmorValue;
    }

    public static int getOldFoodLevel() {
        return oldFoodLevel;
    }

    public static Map<String, Integer> getOldInventoryMap() {
        return oldInventoryMap;
    }
}
