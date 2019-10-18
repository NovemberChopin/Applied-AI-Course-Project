package wumpusworld;


import java.util.*;


/**
 * Contains starting code for creating your own Wumpus World agent.
 * Currently the agent only make a random decision each turn.
 * 
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent
{
    Agent agent;
    private World w;
    int rnd;
    private ArrayList KB;
    /**
     * Creates a new instance of your solver agent.
     * 
     * @param world Current world state 
     */
    public MyAgent(World world)
    {
        w = world;   
    }
   
            
    /**
     * Asks your solver agent to execute an action.
     */

    public void doAction() {
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();
        
        
        //Basic action:
        //Grab Gold if we can.
        if (w.hasGlitter(cX, cY))
        {
            w.doAction(World.A_GRAB);
            return;
        }
        
        //Basic action:
        //We are in a pit. Climb up.
        if (w.isInPit())
        {
            w.doAction(World.A_CLIMB);
            return;
        }
        
        //Test the environment
        if (w.hasBreeze(cX, cY))
        {
            System.out.println("I am in a Breeze");
        }
        if (w.hasStench(cX, cY))
        {
            System.out.println("I am in a Stench");
        }
        if (w.hasPit(cX, cY))
        {
            System.out.println("I am in a Pit");
        }
        if (w.getDirection() == World.DIR_RIGHT)
        {
            System.out.println("I am facing Right");
        }
        if (w.getDirection() == World.DIR_LEFT)
        {
            System.out.println("I am facing Left");
        }
        if (w.getDirection() == World.DIR_UP)
        {
            System.out.println("I am facing Up");
        }
        if (w.getDirection() == World.DIR_DOWN)
        {
            System.out.println("I am facing Down");
        }
        
        //decide next move
        // rnd = decideRandomMove();
        rnd = getMoveNum();
        if (rnd==0) // UP
        {
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_MOVE);
        }
        
        if (rnd==1) // Right
        {
            w.doAction(World.A_MOVE);
        }
                
        if (rnd==2) // down
        {
            w.doAction(World.A_TURN_RIGHT);
            w.doAction(World.A_MOVE);
        }
                        
        if (rnd==3) // left
        {
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_MOVE);
        }
                
    }    
    
     /**
     * Genertes a random instruction for the Agent.
     */
    public int decideRandomMove()
    {
      return (int)(Math.random() * 4);
    }
    

    public int getMoveNum() {

        return decideRandomMove();
    }
}

class AStar {
    // maps model
    public final static int WumpusOrPit = 1;
    public final static int PATH = 2;
    public final static int DIRECT_VALUE = 10; // 横竖移动代价

    public int[][] maps;
    public int size;    // 4
    public Node start;
    public Node end;

    Queue<Node> openList = new PriorityQueue<>();
    List<Node> closeList = new ArrayList<>();

    /**
     * Constructor for AStar, create maps from "World"
     *
     * @Param: [world] Wumpus World class
     * @Param: [size] A* algorithm model
     * @Param: [start] start node
     * @Param: [end] end node
     */
    public AStar(World world, int size, Node start, Node end) {
        this.maps = initMapInfo(world, size);
        this.size = size;
        this.start = start;
        this.end = end;
    }

    // use to test A star algorithm, create maps by define
    public AStar(int[][] maps, int size, Node start, Node end) {
        this.maps = maps;
        this.size = size;
        this.start = start;
        this.end = end;
    }

    /**
     * Init maps for A* algorithm form World
     * @return maps
     */
    private int[][] initMapInfo(World world, int size) {
        int[][] maps = new int[size+1][size+1];
        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length; j++) {
                if (world.isUnknown(i, j) || world.hasPit(i, j) || world.hasWumpus(i, j)) {
                    maps[i][j] = 1;
                } else maps[i][j] = 0;
            }
        }
        return maps;
    }
    /**
     * begin search algorithm
     *
     */
    public void start() {
        // clean
        openList.clear();
        closeList.clear();

        openList.add(start);
        moveNodes();
    }

    /**
     * move current node
     */
    private void moveNodes() {
        while (!openList.isEmpty()) {
            if (isPointInClose(end.point)) { // if search end
                // TODO make along the path
                drawPath(maps, end);
                break;
            }
            Node current = openList.poll();
            closeList.add(current);
            addAllNeighborNodeToOpenList(current);
        }
    }

    /**
     * Draw path
     */
    private void drawPath(int[][] maps, Node end) {
        if(end == null || maps==null) return;
        System.out.println("All cost：" + end.value_G);
        while (end != null)
        {
            Point point = end.point;
            maps[point.y][point.x] = PATH;
            end = end.parent;
        }
    }

    /**
     * Determine if the point are in the closeList
     */
    private boolean isPointInClose(Point point) {
        return point!=null&&isPointInClose(point.x, point.y);
    }

    /**
     * Determine if the point are in the closeList
     */
    private boolean isPointInClose(int x, int y) {
        if (closeList.isEmpty()) return false;
        for (Node node : closeList) {
            if (node.point.x == x && node.point.y == y)
                return true;
        }
        return false;
    }

    /**
     * add all neighbor node to open list
     */
    private void addAllNeighborNodeToOpenList(Node current) {
        int x = current.point.x;
        int y = current.point.y;
        // up
        addNeighborNodeToOpenList(current, x, y - 1, DIRECT_VALUE);
        // down
        addNeighborNodeToOpenList(current, x, y + 1, DIRECT_VALUE);
        // left
        addNeighborNodeToOpenList(current, x - 1, y, DIRECT_VALUE);
        // right
        addNeighborNodeToOpenList(current, x + 1, y, DIRECT_VALUE);
    }

    /**
     * add one neighbor node to open list
     */
    private void addNeighborNodeToOpenList(Node current, int x, int y, int value) {
        if (canAddNodeToOpenList(x, y)) {
            Node end = this.end;
            Point point = new Point(x, y);
            int value_G = current.value_G + value;
            Node child = findNodeInOpenList(point);

            if (child == null) { // if child is a new node for open list
                int value_H = calcuValue_H(end.point, point);
                if(isEndNode(end.point, point)) {
                    child = end;
                    child.parent = current;
                    child.value_G = value_G;
                    child.value_H = value_H;
                } else {
                    child = new Node(point, current, value_G, value_H);
                }
                openList.add(child);

            } else if (child.value_G > value_G) {
                child.value_G = value_G;
                child.parent = current;
                openList.add(child);
            }
        }
    }

    /**
     * find Node from openList
     * @return if node exist in openlist return node, other return null
     */
    private Node findNodeInOpenList(Point point) {
        if (point == null || openList.isEmpty())
            return null;
        for (Node node : openList) {
            if (node.point.equals(point)) {
                return node;
            }
        }
        return null;
    }

    /**
     * judge point is end node or not
     */
    private boolean isEndNode(Point end, Point point) {
        return point != null && end.equals(point);
    }

    /**
     * Determine if the node can be add in the Open list
     */
    private boolean canAddNodeToOpenList(int x, int y) {
        // Is in the map
        if (x < 0 || x >this.size || y < 0 || y > this.size) // size == 4
            return false;
        // Is there any obstacle?
        // TODO why is maps[y][x]?
        if (this.maps[y][x] == WumpusOrPit)
            return false;
        // Is in close list?
        if(isPointInClose(x, y))
            return false;
        return true;
    }

    /**
     * calculate value H,
     * Add the difference between the horizontal and vertical coordinates
     */
    private int calcuValue_H(Point end, Point point) {
        return Math.abs(end.x - point.x) + Math.abs(end.y - point.y);
    }
}

class Node implements Comparable<Node> {
    public Point point; // 坐标
    public Node parent; // 父结点
    public int value_G; // G：是个准确的值，是起点到当前结点的代价
    public int value_H; // H：是个估值，当前结点到目的结点的估计代价

    public Node(int x, int y) {
        this.point = new Point(x, y);
    }

    public Node(Point point, Node parent, int g, int h) {
        this.point = point;
        this.parent = parent;
        value_G = g;
        value_H = h;
    }

    @Override
    public int compareTo(Node o) {
        if (o == null) return -1;
        if (value_G + value_H > o.value_G + o.value_H)
            return 1;
        else if (value_G + value_H < o.value_G + o.value_H) return -1;
        return 0;
    }
}

class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Point) {
            Point c = (Point) obj;
            return x == c.x && y == c.y;
        }
        return false;
    }
}