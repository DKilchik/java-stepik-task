import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroTest {
    Hero hero = new Hero() {
        @Override
        public void levelUp() {
        }

        @Override
        public void attack(Enemy enemy) {
        }

        @Override
        public void defense(Enemy enemy) {
        }
    };

    @Test
    @DisplayName("Chance to get item is 50 %")
    public void openItem(){
        for (int i = 0; i < 10000000; i++){
            Item item = new Item(1,1,1);
            hero.openItem(item);
        }
        // 52% > actual chance > 48%
        assertTrue(hero.items.size() > 4800000);
        assertTrue(hero.items.size() < 5200000);
    }

}