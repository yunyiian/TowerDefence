package WizardTD;

import WizardTD.subtiles.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.PathFinder.Node;
import java.util.List;

public class PathFinderTest {
    private PathFinder pathFinder;

    @BeforeEach
    public void setUp() {
        pathFinder = new PathFinder();
    }

    @Test
    public void testNodeConstructor() {
        Node node = pathFinder.new Node(1, 2);
        assertEquals(1, node.x);
        assertEquals(2, node.y);
        assertEquals(Float.POSITIVE_INFINITY, node.g);
        assertEquals(0, node.h);
        assertEquals(0, node.f);
        assertNull(node.parent);
    }

    @Test
    public void testNodeEquals() {
        Node node1 = pathFinder.new Node(1, 2);
        Node node2 = pathFinder.new Node(1, 2);
        Node node3 = pathFinder.new Node(2, 3);
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testNodeHashCode() {
        Node node1 = pathFinder.new Node(1, 2);
        Node node2 = pathFinder.new Node(1, 2);
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testHeuristic() {
        Node node1 = pathFinder.new Node(1, 2);
        Node node2 = pathFinder.new Node(3, 4);
        assertEquals(4, pathFinder.heuristic(node1, node2));
    }

    @Test
    public void testGetNeighbors() {
        Tile[][] tiles = new Tile[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tiles[i][j] = new GrassTile(null);
            }
        }
        tiles[2][1] = new PathTile(1, 2, null, null);
        tiles[2][2] = new PathTile(2, 2, null, null);
        tiles[2][3] = new PathTile(3, 2, null, null);
        Node node = pathFinder.new Node(2, 2);
        List<Node> neighbors = pathFinder.getNeighbors(node, tiles);
        assertEquals(3, neighbors.size());
    }

    // TODO: Add tests for findPath() method by simulating different scenarios.

}
