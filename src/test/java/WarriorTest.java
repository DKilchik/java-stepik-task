import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class WarriorTest {

    Hero warrior = new Warrior();
    private final int DAMAGE_FIRST_LEVEL = 150;
    private final int EXP_FIRST_LEVEL = 0;
    private final int FULL_HP_FIRST_LEVEL = 500;

    @Test
    @DisplayName("Test health after hero creating")
    public void health(){
        assertEquals(FULL_HP_FIRST_LEVEL,warrior.hp);
    }

    @Test
    @DisplayName("Test mana after hero creating")
    public void mana(){
        assertEquals(10,warrior.mana);
    }

    @Test
    @DisplayName("Test agility after hero creating")
    public void agility(){
        assertEquals(50,warrior.a);
    }
    @Test
    @DisplayName("Test strength after hero creating")
    public void strength(){
        assertEquals(100,warrior.s);
    }

    @Test
    @DisplayName("Test intelligence after hero creating")
    public void intelligence(){
        assertEquals(1,warrior.i);
    }

    @Test
    @DisplayName("Test damage after hero creating")
    public void damage(){
        assertEquals(DAMAGE_FIRST_LEVEL,warrior.damage);
    }

    @Test
    @DisplayName("Test experience after hero creating")
    public void experience(){
        assertEquals(EXP_FIRST_LEVEL,warrior.exp);
    }

    @Test
    @DisplayName("Test default attack")
    public void attackDefault(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL + 1,100,100);
        warrior.attack(enemy);
        assertEquals(1,enemy.hp);
    }

    @Disabled
    @Test
    @DisplayName("Test attack and kill the enemy")
    public void attackKillEnemy(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL,100,100);
        warrior.attack(enemy);
        assertFalse(enemy.isAlive);
    }

    @Test
    @DisplayName("Hero get experience of enemy after killing one")
    public void heroWin(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL,100,100);
        warrior.attack(enemy);
        int expIncreased = warrior.exp;
        assertTrue(expIncreased > EXP_FIRST_LEVEL);
        assertEquals(EXP_FIRST_LEVEL + enemy.exp, expIncreased);
    }

    @Test
    @DisplayName("Hero get skillUp after getting new level")
    public void heroSkillUp(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL,100,500);
        warrior.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 20, warrior.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 200, warrior.hp);
        assertEquals(20, warrior.mana);
        assertEquals(110, warrior.s);
        assertEquals(60, warrior.a);
        assertEquals(11, warrior.i);

    }
    @DisplayName("Hero get skillUp with new level")
    @ParameterizedTest(name = "getting {0} exp")
    @ValueSource(ints = {500, 501, 999})
    public void newLevel(int exp){
        Enemy enemy = new Enemy(149,100, exp);
        warrior.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 20, warrior.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 200, warrior.hp);
    }

    @DisplayName("Hero get skillUp with new level twice")
    @ParameterizedTest(name = "getting {0} exp")
    @ValueSource(ints = {1000, 1499})
    public void newLevelTwice(int exp){
        Enemy enemy = new Enemy(149,100, exp);
        warrior.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 20 + 20, warrior.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 200 + 200, warrior.hp);
    }

    @Test
    @DisplayName("Test default defence")
    public void defenceDefault(){
        Enemy enemy = new Enemy(100,FULL_HP_FIRST_LEVEL - 1,100);
        warrior.defense(enemy);
        assertEquals(1,warrior.hp);
    }

    @Test
    @DisplayName("Test defence.Enemy kills the hero")
    public void defenceGameOver() throws Exception{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Enemy enemy = new Enemy(151,FULL_HP_FIRST_LEVEL,100);
        warrior.defense(enemy);
        assertEquals("Ваш герой был убит", outContent.toString());
    }

    @DisplayName("Test hero's hp limit")
    @ParameterizedTest(name = "getting damage {0}")
    @ValueSource(ints = {500,501,1000})
    public void hpLimit(int damage) throws Exception{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Enemy enemy = new Enemy(151,damage,100);
        warrior.defense(enemy);
        assertEquals("Ваш герой был убит", outContent.toString());
    }

    @Test
    @DisplayName("Dead enemy can't inflict damage")
    public void deadEnemy(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL,100,100);
        warrior.attack(enemy);
        warrior.defense(enemy);
        assertEquals(FULL_HP_FIRST_LEVEL,warrior.hp);
    }

    @Test
    @DisplayName("Increase damage after getting item type=1")
    public void itemType1(){
        Item item =  new Item(0,0,1);
        warrior.items.add(item);
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL + 100 + 1,100,100);
        warrior.attack(enemy);
        assertEquals(1,enemy.hp);
    }

    @Test
    @DisplayName("Warrior nickName field")
    public void nickName(){
        warrior.nickName = "R2D2";
        assertEquals("R2D2", warrior.nickName);
    }

    @Test
    @DisplayName("Warrior coordinates fields")
    public void coordinates(){
        warrior.x = 100;
        warrior.y = 50;
        assertEquals(100, warrior.x);
        assertEquals(50, warrior.y);
    }

}