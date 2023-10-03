package WizardTD;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MonsterTest {

    @Test
    public void testReduceHealth() {
        Board board = new Board();
        Monster monster = new Monster(board, null, 1.0f, 0, 100.0f, 1.0f, 10.0f, "type", null);
        monster.reduceHealth(25.0f);
        assertEquals(75.0f, monster.getCurrentHp());
    }

    @Test
    public void testMonsterSpeed() {
        Board board = new Board();
        Monster monster = new Monster(board, null, 1.5f, 0, 100.0f, 1.0f, 10.0f, "type", null);
        assertEquals(1.5f, monster.getSpeed());
    }

    // Additional tests can be added here...
}

