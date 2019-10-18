package wumpusworld;

import static org.junit.jupiter.api.Assertions.*;

class MyAgentTest {

    @org.junit.jupiter.api.Test
    void testAStar() {
        int[][] maps = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 1, 0, 0}
        };
        MapInfo info = new MapInfo(maps, 5, new Node(1, 4), new Node(4, 4));
        new AStar().start(info);

        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length; j++) {
                System.out.print(maps[i][j] + " ");
            }
            System.out.println();
        }
    }
}