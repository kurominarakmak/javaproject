package gameLoader;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import item.base.Item;
import item.healtItem.CouchPotato;
import item.healtItem.LuckyBox;
import item.healtItem.Heart;
import item.weapon.OraSword;
import item.weapon.Envil;
import scene.ShopScene;
import item.accessory.FullCharge;
import item.accessory.RoboHead;
import item.armor.Suit;
import item.base.*;
public class GameLoader {
    private static final String SAVE_FILE = "res/gamesave/data"; // File location

    // Save coins to a file
    public static void saveGame(int coins,List<Item> items) {       
    	Properties props = new Properties();
        props.setProperty("playerCoins", String.valueOf(coins));
        
        if (items != null) {
            for (Item item : items) {
                props.setProperty("item_" + item.getName(), String.valueOf(item.getLevel()));
            }
        }
        
        try (FileOutputStream out = new FileOutputStream(SAVE_FILE)) {
            props.store(out, "Shop Data");
//            System.out.println("💾 Coins Saved: " + coins);
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("❌ Error saving coins!");
        }
    }

    // Load coins from a file
    public static int loadCoins() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(SAVE_FILE)) {
//        	System.out.println("💾 Found file");
            props.load(in);
            return Integer.parseInt(props.getProperty("playerCoins", "10000")); // Default 10,000 coins
        } catch (IOException e) {
//            System.out.println("⚠ No save file found. Starting with default coins.");
            return 10000; // Default if no save found
        }
    }
    
//    level Items
    public static List<Item> loadItemLevels() {
    	List<Item> itemList = new ArrayList<Item>();
    	itemList.add(new RoboHead());
        itemList.add(new FullCharge());
        itemList.add(new Suit());
        itemList.add(new CouchPotato());
        itemList.add(new LuckyBox());
        itemList.add(new Heart());
        itemList.add(new OraSword());
        itemList.add(new Envil());
        
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(SAVE_FILE)) {
            props.load(in);
            for (Item item : itemList) {
                String levelStr = props.getProperty("item_" + item.getName(), "0"); // Default level 0
                Integer level = Integer.parseInt(levelStr);
                item.setLevel(level);
                
                if (level != 0) {
                	
                	item.setEffect(item.getLevel()*item.getStatPerLevel());
                	
                }
                
                
              
            }
//            System.out.println("🔄 Loaded item levels.");
            return itemList;
        } catch (IOException e) {
//            System.out.println("⚠ No save file found. Items start at default levels.");
            return itemList;
        }
       
		
    }
}

