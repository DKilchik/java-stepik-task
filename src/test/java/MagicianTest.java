import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MagicianTest {

    Hero mag = new Magician();
    private final int FULL_HP_FIRST_LEVEL = 100;
    private final int EXP_FIRST_LEVEL = 0;
    private final int MANA_FIRST_LEVEL = 5000;
    private final int DAMAGE_FIRST_LEVEL = 40;

    @Test
    @DisplayName("Test health after Magician creating")
    public void health(){
        assertEquals(FULL_HP_FIRST_LEVEL,mag.hp);
    }

    @Test
    @DisplayName("Test mana after Magician creating")
    public void mana(){
        assertEquals(MANA_FIRST_LEVEL,mag.mana);
    }

    @Test
    @DisplayName("Test agility after Magician creating")
    public void agility(){
        assertEquals(30,mag.a);
    }
    @Test
    @DisplayName("Test strength after Magician creating")
    public void strength(){
        assertEquals(5,mag.s);
    }

    @Test
    @DisplayName("Test intelligence after Magician creating")
    public void intelligence(){
        assertEquals(300,mag.i);
    }

    @Test
    @DisplayName("Test damage after Magician creating")
    public void damage(){
        assertEquals(DAMAGE_FIRST_LEVEL,mag.damage);
    }

    @Test
    @DisplayName("Test experience after Magician creating")
    public void experience(){
        assertEquals(EXP_FIRST_LEVEL,mag.exp);
    }

    @Test
    @DisplayName("Magician nickName field")
    public void nickName(){
        mag.nickName = "R2D2";
        assertEquals("R2D2", mag.nickName);
    }

    @Test
    @DisplayName("Magician coordinates fields")
    public void coordinates(){
        mag.x = 100;
        mag.y = 50;
        assertEquals(100, mag.x);
        assertEquals(50, mag.y);
    }

    @Test
    @DisplayName("Magician arm attack")
    public void armAttack(){
        Enemy enemy =  new Enemy(DAMAGE_FIRST_LEVEL + 1,100,100);
        mag.attack(enemy);
        assertEquals(1,enemy.hp);
    }

    @Test
    @DisplayName("Magician cast attack")
    public void castAttack(){
        // Testing eduCast method and cast attack
        Magician magician = new Magician();
        Enemy enemy =  new Enemy(1001,100,100);
        Item cast = new Item(1,1,2);
        magician.items.add(cast);
        magician.eduCast(magician.items.get(0));
        magician.attack(enemy);
        assertEquals(1,enemy.hp);
    }

    @Test
    @DisplayName("Magician make final attack by arm")
    public void fatality(){
        //  В случае возможности убить врага с руки - маг бьет с руки!!! в Первую очередь
        Magician magician = new Magician();
        Enemy enemy =  new Enemy(DAMAGE_FIRST_LEVEL,100,100);
        Item cast = new Item(1,1,2);
        magician.items.add(cast);
        magician.eduCast(magician.items.get(0));
        magician.attack(enemy);
        assertEquals(0,enemy.hp);
    }

    @Test
    @DisplayName("Hero get experience of enemy after killing one")
    public void heroWin(){
        Enemy enemy = new Enemy(39,100,100);
        mag.attack(enemy);
        int expIncreased = mag.exp;
        assertTrue(expIncreased > EXP_FIRST_LEVEL);
        assertEquals(EXP_FIRST_LEVEL + enemy.exp, expIncreased);
    }

    @Test
    @DisplayName("Hero get skillUp after getting new level")
    public void magSkillUp(){
        Enemy enemy = new Enemy(DAMAGE_FIRST_LEVEL,100,500);
        mag.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 10, mag.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 30, mag.hp);
        assertEquals(MANA_FIRST_LEVEL + 1000, mag.mana);
        assertEquals(15, mag.s);
        assertEquals(40, mag.a);
        assertEquals(310, mag.i);

    }

    @DisplayName("Magician get skillUp with new level")
    @ParameterizedTest(name = "getting {0} exp")
    @ValueSource(ints = {500, 501, 999})
    public void newLevel(int exp){
        Enemy enemy = new Enemy(39,100, exp);
        mag.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 10, mag.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 30, mag.hp);
    }

    @DisplayName("Magician get skillUp with new level twice")
    @ParameterizedTest(name = "getting {0} exp")
    @ValueSource(ints = {1000, 1499})
    public void newLevelTwice(int exp){
        Enemy enemy = new Enemy(39,100, exp);
        mag.attack(enemy);
        assertEquals(DAMAGE_FIRST_LEVEL + 10 + 10, mag.damage);
        assertEquals(FULL_HP_FIRST_LEVEL + 30 + 30 , mag.hp);
    }

    @Test
    @DisplayName("Magician defence by Magic Shield")
    public void defenceMagicShield(){
        Enemy enemy = new Enemy(39,MANA_FIRST_LEVEL - 1, 100);
        mag.defense(enemy);
        assertEquals(1, mag.mana);
        assertEquals(FULL_HP_FIRST_LEVEL, mag.hp);
    }

    @Test
    @DisplayName("HP was damaged after mana was finished")
    public void defenceManaIsOver(){
        Enemy enemy = new Enemy(39,MANA_FIRST_LEVEL + FULL_HP_FIRST_LEVEL - 1, 100);
        mag.defense(enemy);
        assertEquals(0, mag.mana);
        assertEquals(1, mag.hp);
    }

    @Test
    @DisplayName("Test defence.Enemy kills the magician")
    public void defenceGameOver() throws Exception{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Enemy enemy = new Enemy(151,FULL_HP_FIRST_LEVEL + MANA_FIRST_LEVEL ,100);
        mag.defense(enemy);
        assertEquals("Ваш герой был убит", outContent.toString());
    }

    @Test
    @DisplayName("Dead enemy can't inflict damage")
    public void deadEnemy(){
        Enemy enemy = new Enemy(1,100,100);
        mag.attack(enemy);
        mag.defense(enemy);
        assertEquals(FULL_HP_FIRST_LEVEL,mag.hp);
    }
}