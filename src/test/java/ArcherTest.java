import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ArcherTest {

    Hero archer = new Archer();
    private final int DAMAGE_FIRST_LEVEL = 200;
    private final int EXP_FIRST_LEVEL = 0;
    private final int FULL_HP_FIRST_LEVEL = 200;

    @Test
    @DisplayName("Test health after archer creating")
    public void health(){
        assertEquals(FULL_HP_FIRST_LEVEL,archer.hp);
    }

    @Test
    @DisplayName("Test mana after archer creating")
    public void mana(){
        assertEquals(50,archer.mana);
    }

    @Test
    @DisplayName("Test agility after archer creating")
    public void agility(){
        assertEquals(150,archer.a);
    }
    @Test
    @DisplayName("Test strength after archer creating")
    public void strength(){
        assertEquals(20,archer.s);
    }

    @Test
    @DisplayName("Test intelligence after archer creating")
    public void intelligence(){
        assertEquals(30,archer.i);
    }

    @Test
    @DisplayName("Test damage after archer creating")
    public void damage(){
        assertEquals(DAMAGE_FIRST_LEVEL,archer.damage);
    }

    @Test
    @DisplayName("Test experience after archer creating")
    public void experience(){
        assertEquals(EXP_FIRST_LEVEL,archer.exp);
    }

    @Test
    @DisplayName("Archer nickName field")
    public void nickName(){
        archer.nickName = "R2D2";
        assertEquals("R2D2", archer.nickName);
    }

    @Test
    @DisplayName("Archer coordinates fields")
    public void coordinates(){
        archer.x = 100;
        archer.y = 50;
        assertEquals(100, archer.x);
        assertEquals(50, archer.y);
    }

    @Test
    @DisplayName("Archer open Item")
    public void openItem(){
        for (int i=0; i < 100; i++){
            Item item = new Item(1,1,1);
            archer.openItem(item);
        }
        assertEquals(100, archer.items.size());
    }

    @Test
    @DisplayName("Test default archer attack")
    public void attackDefault(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL + 1,100,100);
        archer.attack(enemy);
        assertEquals(1,enemy.hp);
    }

    @Disabled
    @Test
    @DisplayName("Test attack and kill the enemy")
    public void attackKillEnemy(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL,100,100);
        archer.attack(enemy);
        assertFalse(enemy.isAlive);
    }

    @Test
    @DisplayName("Hero get experience of enemy after killing one")
    public void heroWin(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL,100,100);
        archer.attack(enemy);
        int expIncreased = archer.exp;
        assertTrue(expIncreased > EXP_FIRST_LEVEL);
        assertEquals(EXP_FIRST_LEVEL + enemy.exp, expIncreased);
    }

    @Test
    @DisplayName("Hero get skillUp after getting new level")
    public void heroSkillUp(){
        Enemy enemy = new Enemy(149,100,500);
        archer.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 50, archer.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 50, archer.hp);
        assertEquals(60, archer.mana);
        assertEquals(30, archer.s);
        assertEquals(180, archer.a);
        assertEquals(40, archer.i);

    }

    @DisplayName("Archer get skillUp with new level")
    @ParameterizedTest(name = "getting {0} exp")
    @ValueSource(ints = {500, 501, 999})
    public void newLevel(int exp){
        Enemy enemy = new Enemy(149,100, exp);
        archer.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 50, archer.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 50, archer.hp);
    }

    @DisplayName("Archer get skillUp with new level twice")
    @ParameterizedTest(name = "getting {0} exp")
    @ValueSource(ints = {1000, 1499})
    public void newLevelTwice(int exp){
        Enemy enemy = new Enemy(149,100, exp);
        archer.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 50 + 50, archer.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 50 + 50, archer.hp);
    }

    @Test
    @DisplayName("Test percent of possibility of archer defence without getting damage")
    public void defenceLuckyChance(){
        Enemy enemy = new Enemy(100,1, 1);
        int gotDamagedAttacks = 0;
        for (int i = 0; i < 1000000 ; i++){
            archer.defense(enemy);
            if (archer.hp < FULL_HP_FIRST_LEVEL){
                gotDamagedAttacks++;
                archer.hp = FULL_HP_FIRST_LEVEL;
            }
        }
        // assertion is equal to 72% > actual result > 68 %
        assertTrue(720000 > gotDamagedAttacks);
        assertTrue(680000 < gotDamagedAttacks);
    }

    @Test
    @DisplayName("Test archer default defence")
    public void defenceDefault(){
        int fullHP = archer.hp;
        Enemy enemy = new Enemy(151,1,100);
        for (int i=0;i<4;i++){
            archer.defense(enemy);
        }
        assertTrue(FULL_HP_FIRST_LEVEL > archer.hp);

    }

    @Test
    @DisplayName("Test defence.Enemy kills the archer")
    public void defenceGameOver() throws Exception{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Enemy enemy = new Enemy(FULL_HP_FIRST_LEVEL,1000,100);
        for (int i=0; i < 4; i++){
            archer.defense(enemy);
        }
        assertTrue(outContent.toString().contains("Ваш герой был убит"));
    }

    @Test
    @DisplayName("Dead enemy can't inflict damage")
    public void deadEnemy(){
        Enemy enemy = new Enemy(10,100,100);
        archer.attack(enemy);
        for (int i=0; i < 4; i++){
            archer.defense(enemy);
        }
        assertEquals(FULL_HP_FIRST_LEVEL,archer.hp);
    }
}
