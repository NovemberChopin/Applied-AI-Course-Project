package wumpusworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

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
        AStar a = new AStar(maps, 4, new Node(1, 4), new Node(4, 4));
        a.start();
        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length; j++) {
                System.out.print(maps[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println(a.pathOrder.pop().x);
    }
    @org.junit.jupiter.api.Test
    void testStack() {
        Stack<Point> path = new Stack<>();
        path.push(new Point(1, 1));
        path.push(new Point(1, 2));
        path.push(new Point(1, 3));

        System.out.println(path.size());
        System.out.println(path.pop().y);
        System.out.println(path.pop().y);
        System.out.println(path.pop().y);
    }

    @org.junit.jupiter.api.Test
    void testVector() {
        Vector<Point> points = new Vector<>();
        points.add(new Point(1, 1));
        points.add(new Point(1, 2));
        points.remove(new Point(1, 1));
        points.add(new Point(2, 2));
        for (Point p : points) {
            System.out.println(p.x + " " + p.y);
        }
    }

    @org.junit.jupiter.api.Test
    void testList() {
        List<Point> points = new ArrayList<>();
        points.add(null);
        points.add(null);
        points.add(null);
        System.out.println(points.size());
    }

    @org.junit.jupiter.api.Test
    void testUpdateOrLogics() {
        List<List<Point>> logics = new ArrayList<>();
        List<Point> list1 = new ArrayList<>();
        list1.add(new Point(1, 1));
        list1.add(new Point(1, 2));
        list1.add(new Point(1, 3));
        logics.add(list1);

        List<Point> list2 = new ArrayList<>();
        list2.add(new Point(2, 1));
        logics.add(list2);

        List<Point> list3 = new ArrayList<>();
        list3.add(new Point(3, 1));
        list3.add(new Point(3, 2));
        logics.add(list3);

        KnowledgeBase KB = new KnowledgeBase();
        KB.updateOrLogics(logics);

        System.out.println(logics.size());

    }
}