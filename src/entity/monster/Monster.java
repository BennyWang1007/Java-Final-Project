package entity.monster;

import java.awt.Graphics;
import java.util.ArrayList;

import entity.*;
import main.Game;

class DamageReceive {
    public int damage;
    public int time; // frames last
    public DamageReceive(int damage, int time) {
        this.damage = damage;
        this.time = time;
    }
}

public abstract class Monster extends Entity {
    protected String name;
    public int id = 0;
    protected int hp;
    protected int maxHp;
    protected int defense;
    protected float speed; // pixels per second
    protected float speedPerFrame;

    public int attack;
    public int exp;
    
    protected Player player;
    protected ArrayList<DamageReceive> damageReceived = new ArrayList<DamageReceive>();
    protected DropItem[] dropItems;
    protected float[] dropRates;

    public Monster(Game game, String name, int x, int y, int hp, int attack, float speed, int exp, Player player) {
        super(game, x, y, 50, 50);
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.speed = speed;
        this.speedPerFrame = speed / Game.FPS;
        this.exp = exp;
        this.player = player;
    }

    public Monster(Game game, String name, int x, int y, int hp, int attack, float speed, int exp, double strength, Player player) {
        this(game, name, x, y, (int)(hp * strength), (int)(attack * strength), (int)(speed * Math.min(4, strength / 5 + 1)), (int)(exp * strength), player);
    }

    public void setId(int id) { this.id = id; }

    public boolean isDead() { return hp <= 0; }

    public void dropItems() {
        for (int i = 0; i < dropItems.length; i++) {
            if (Math.random() < dropRates[i]) {
                dropItems[i].x = x;
                dropItems[i].y = y;
                // randomize the drop item position
                dropItems[i].x += (int)(Math.random() * 20 - 10);
                dropItems[i].y += (int)(Math.random() * 20 - 10);
                game.addDropItem(dropItems[i]);
            }
        }
    }

    public void damage(int damage) {
        hp -= damage;
        damageReceived.add(new DamageReceive(damage, Game.FPS)); // show damage received for 3 seconds
    }

    public void drawDamageReceived(Graphics g) {
        int screenX = game.translateToScreenX(x);
        int screenY = game.translateToScreenY(y);
        // show damage received at right-up of the health bar
        int cx = (int)Math.round(screenX + width/2.0 - 15);
        int cy = (int)Math.round(screenY - height/2.0 - 15);
        g.setColor(java.awt.Color.RED);
        g.setFont(g.getFont().deriveFont(10.0f));
        for (int i = 0; i < damageReceived.size(); i++) {
            float alpha = (float)damageReceived.get(i).time / Game.FPS;
            cy -= (int)(10 * alpha);
            g.setColor(new java.awt.Color(255, 0, 0, (int)(255 * alpha)));
            g.drawString(Integer.toString(damageReceived.get(i).damage), cx, cy - 10 * i);
        }
        damageReceived.removeIf(d -> d.time-- <= 0);
    }

    abstract public void update();
    abstract public void draw(Graphics g);
}
