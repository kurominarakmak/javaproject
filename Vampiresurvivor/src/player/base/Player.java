package player.base;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import audio.NameSE;
import audio.SE;
import entity.projectile.Projectile;
import item.armor.Suit;
import item.base.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logic.CollisionHandler;
import logic.EnemyManager;
import map.Map1Collide;
import map.MapObstacle;
import scene.selector.item.ItemSelectorStage;
import javafx.scene.input.KeyCode;


public abstract class Player {
	public static Player instance ;
	private static double MAX_ARMOR;
    protected double x;
	protected double y;
	protected String name;
	protected String desc;
	protected double armor;
	protected double damage;
	protected double speed;
	protected double health;
	protected double maxHealth;
	protected double extraDmg;
	protected int LEVEL = 1;
	protected ArrayList <Item> backpack = new ArrayList<Item>();
	protected int exp;
	protected int LEVEL_UP_EXP = 50;
    public static boolean isLevelup = false;
    
    
    protected Player(double x, double y,double maxHealth,double speed,double armor) {
        this.x = x;
        this.y = y;
        setExtraDmg(0);
        setExp(0);
        setMaxHealth(maxHealth);
        setHealth(maxHealth);
        setArmor(armor);
        setSpeed(speed);
        Player.instance = this;
    }
    
   

	//ใช้ set เก็บ enum Keycode(ปุ่มที่กดมา) จะได้รู้ว่ากดตัวไหนอยู่บ้างกันแน่ 
    public void update(Set<KeyCode> keys, int gameWidth, int gameHeight, int borderSize) {
        move(keys, gameWidth, gameHeight, borderSize);
        
        
    }

    public void move(Set<KeyCode> keys, int gameWidth, int gameHeight, int borderSize) {
        double dx = 0, dy = 0;
        if (keys.contains(KeyCode.W)) dy -= speed;
        if (keys.contains(KeyCode.S)) dy += speed;
        if (keys.contains(KeyCode.A)) dx -= speed;
        if (keys.contains(KeyCode.D)) dx += speed;

        double newX = x + dx;
        double newY = y + dy;

        if (CollisionHandler.canMove(newX, newY, MapObstacle.getMapObstacle())) {
            x = clamp(newX, borderSize, gameWidth - borderSize);
            y = clamp(newY, borderSize, gameHeight - borderSize);
        }
    }

  //ไว้เช็ค ไม่ให้เดินเกินขอบ
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
    
    public void enforceMapBoundaries(int gameWidth, int gameHeight, int borderSize) {
        // Assuming player is drawn as a 20x20 oval centered at (x,y)
        double playerRadius = 10;
        
        // Enforce absolute boundaries regardless of who moved the player
        this.x = Math.max(borderSize + playerRadius, Math.min(this.x, gameWidth - borderSize - playerRadius));
        this.y = Math.max(borderSize + playerRadius, Math.min(this.y, gameHeight - borderSize - playerRadius));
    }
    
    public void gainExp(int amount) {
        exp += amount;
        if (exp >= LEVEL_UP_EXP) {
        	levelUp();
        }
        
        SE.playSE(NameSE.SND_GETEXP);
//        System.out.println("EXP Gained: " + amount + " | Total EXP: " + exp);
    }
    
    public void levelUp( ) {
        exp = exp - LEVEL_UP_EXP;
        isLevelup = true;
        LEVEL++;
        System.out.println("Level UP!! your level is " + LEVEL);
        LEVEL_UP_EXP = getExpToNextLevel();
        
    
    }
    
    public int getExpToNextLevel() {
        return (int) (LEVEL_UP_EXP * 1.2); // EXP scaling formula
    }

    public void takeDamage(double damage) {
    	double armor = getArmor();
    	armor = armor-damage;
    	setArmor(armor);
    	if(armor <0) {
    		setHealth(getHealth() + armor);
    	}
    	
        if (getHealth() < 0) {setHealth(0);}
        
        SE.playSE(NameSE.SND_HIT3);
    }

    public boolean isDead() {
        return getHealth() <= 0;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(x - 10, y - 10, 20, 20);
        drawHealthBar(gc);
    }
//health bar
    protected void drawHealthBar(GraphicsContext gc) {
        double barWidth = 40;
        double barHeight = 5;
        double healthPercentage = getHealth() / getMaxHealth();
        
        double armorPercentage = getArmor() / getMaxHealth();

        gc.setFill(Color.GRAY);
        gc.fillRect(x - barWidth / 2, y -50, barWidth, barHeight);
        gc.setFill(Color.RED);
        gc.fillRect(x - barWidth / 2, y + -50, barWidth * healthPercentage, barHeight);
        gc.setFill(Color.BROWN);
        gc.fillRect(x - barWidth / 2, y + -50, barWidth * armorPercentage, barHeight);
    }
    ///////////////////////////////////////////////////////////////////
   public void upgradeItems(Item item) {
	   switch (item.getItemName()) {
	case "Suit" : {
		double newArmor = getArmor()+item.getEffect();
		setArmor(newArmor);
		break;
	}
	case "CouchPotato" : {
		double newHealth = getHealth()+item.getEffect();
		setHealth(newHealth);
		break;
	}
	case "LuckyBox" : {
		double newHealth = getHealth()+item.getEffect();
		setHealth(newHealth);
		break;
		}
	case "OraSword" : {
		double newAttk = getExtraDmg()+item.getEffect();
		setExtraDmg(newAttk);
		break;
	}
	case "Envil" : {
		double newAttk = getExtraDmg()+item.getEffect();
		setExtraDmg(newAttk);
		break;
	}
	case "Robot Head" : {
		double newSpeed = getSpeed()+item.getEffect();
		setSpeed(newSpeed);
		break;
	}
	case "Heart":{
		double newHealth = getHealth()+item.getEffect();
		setHealth(newHealth);
		break;
	}
	case "Full Charge": {
		double newAttk = getExtraDmg()+item.getEffect();
		setExtraDmg(newAttk);
		break;
	}
	
	default:
		throw new IllegalArgumentException("Unexpected item: " + item.getItemName());
	}
   }
    
    

    public ArrayList<Item> getBackpack() {
		return backpack;
	}
	
	public boolean isWear(Item item) {
		
		if (backpack.contains(item)) {
			return true;
		}else {
			return false;
		}
	}
    public double getX() { return x; }
    public double getY() { return y; }
    
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getDamage() {
		return damage+ getExtraDmg();
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}



	public double getHealth() { return health; }
    
    public void setHealth(double health) {
    	
    	this.health = Math.max(0, Math.min(maxHealth, health));
   
    }   
   
    public double getExtraDmg() {
		return extraDmg;
	}

	public void setExtraDmg(double extraDmg) {
		this.extraDmg = extraDmg;
	}
    
	public double getMaxHealth() {return maxHealth;}

	public void setMaxHealth(double maxHealth) {this.maxHealth = maxHealth;}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getArmor() {
		return armor;
	}

	public void setArmor(double armor) {
		this.armor = armor>= maxHealth ? maxHealth: armor;
		if (armor <=0) {
			this.armor = 0;
		}
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public abstract List<Projectile> getProjectiles();

	public abstract void update(Set<KeyCode> keys, int gameWidth, int gameHeight, int borderSize,
			EnemyManager enemyManager, double deltaTime, long now);

	public int getLevel() {
		return LEVEL;
	}
	
	public static double getMAX_ARMOR() {
		return MAX_ARMOR;
	}

	public static void setMAX_ARMOR(double mAX_ARMOR) {
		MAX_ARMOR = mAX_ARMOR;
	}



	public static Player getInstance() {
		return instance;
	}

	public abstract Image getIdleFrame();
	
	
}


