package weapons;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.*;

import entity.*;
import entity.monster.Monster;
import main.Game;
import utils.ImageTools;

public abstract class Weapon extends Entity{
    protected Player player;
    protected int attack;
    protected HashMap<Integer, Integer> attackCooldowns;
    protected BufferedImage image;
    protected BufferedImage[][] animationImages; // for animation
    protected BufferedImage originalImage;
    protected float cooldownTime; // in seconds
    protected int level = 1;
    protected final int maxLevel = 5;

    public Weapon(Game game, int width, int height, int attack, Player player) {
        super(game, 0, 0, width, height);
        this.attack = attack;
        this.player = player;
        // loadAnimation();
        attackCooldowns = new HashMap<>();
    }

    protected void readImage(String imageName) {
        originalImage = ImageTools.scaleImage(ImageTools.readImage(imageName), width, height);
        image = originalImage;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        originalImage = ImageTools.scaleImage(image, width, height);
    }

    public void decreaseCooldowns() {
        // cooldowns-- and remove cooldowns <= 0
        Iterator<Integer> iterator = attackCooldowns.keySet().iterator();
        while (iterator.hasNext()) {
            int id = iterator.next();
            attackCooldowns.put(id, attackCooldowns.get(id) - 1);
            if (attackCooldowns.get(id) <= 0) {
                iterator.remove();
            }
        }
    }

    public abstract void update();
    public abstract void attackOn(Monster monster);
    public abstract void levelUp();
    public abstract void draw(Graphics g);
    public abstract void loadAnimation();
}
