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
    private KnowledgeBase KB;
    List<Point> path;
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
        int rnd = 0;
        if (this.path == null)
            this.path = new ArrayList<>();
        Point current = new Point(w.getPlayerX(), w.getPlayerY());

        /**
         * The current path has not yet reached the end
         */
        if (this.path.size() > 1) {
            rnd = beforeMove(current);
        } else {
            /**
             * reached end point, create a new path
             */
            Stack<Point> p = getPath();
            this.path.clear();
            int length = p.size();
            for (int i = 0; i < length; i++) {
                this.path.add(p.pop());
            }
            rnd = beforeMove(current);
        }
        return rnd;
    }

    private int beforeMove(Point current) {
        // If current point equal the start point
        // this.path.get(0) represent start point
        // this.path.get(1) represent end point
        if (current.equals(this.path.get(0))) {
            // if next step is Wumpus, shoot it
            if (this.path.get(1).equals(this.KB.Wumpus)) {
                adjustDirForShoot(this.path.get(0), this.path.get(1));
                // SHOOT
                System.out.println("****************Shoot Wumpus");
                w.doAction(World.A_SHOOT);
                this.KB.afterShoot(w);
            }
            rnd = getNum(current, this.path.get(1));
            this.path.remove(0);
        } else
            System.out.println("Error: current point is not equal the start point");
        return rnd;
    }

    private void adjustDirForShoot(Point start, Point end) {
        setDirToRight();
        // if Wumpus is in down
        if ((start.x == end.x) && (start.y == end.y + 1)) {
            w.doAction(World.A_TURN_RIGHT);
        }
        // up
        if ((start.x == end.x) && (start.y == end.y - 1)) {
            w.doAction(World.A_TURN_LEFT);
        }
        // left
        if ((start.x == end.x + 1) && (start.y == end.y)) {
            w.doAction(World.A_TURN_RIGHT);
            w.doAction(World.A_TURN_RIGHT);
        }
        // right
        if ((start.x == end.x - 1) && (start.y == end.y)) {

        }
    }

    /**
     * Set direction equal right
     */
    private void setDirToRight() {
        if (w.getDirection() == World.DIR_UP) {
            w.doAction(World.A_TURN_RIGHT);
        }
        if (w.getDirection() == World.DIR_DOWN) {
            w.doAction(World.A_TURN_LEFT);
        }
        if (w.getDirection() == World.DIR_LEFT) {
            w.doAction(World.A_TURN_LEFT);
            w.doAction(World.A_TURN_LEFT);
        }
    }

    public int getNum(Point current, Point nextPoint) {
        setDirToRight();
        int rnd = 0;
        // down
        if ((current.x == nextPoint.x) && (current.y == nextPoint.y + 1)) {
            rnd = 2;
        }
        // up
        if ((current.x == nextPoint.x) && (current.y == nextPoint.y - 1)) {
            rnd = 0;
        }
        // left
        if ((current.x == nextPoint.x + 1) && (current.y == nextPoint.y)) {
            rnd = 3;
        }
        // right
        if ((current.x == nextPoint.x - 1) && (current.y == nextPoint.y)) {
            rnd = 1;
        }
        return rnd;
    }

    public Stack<Point> getPath() {
        if (this.KB == null) {
            this.KB = new KnowledgeBase();
        }
        this.KB.createKB(this.w);
        this.KB.doReason();

        Node start = new Node(w.getPlayerX(), w.getPlayerY());  // get start Node
        Point point = KB.makeChoose();
        Node end = new Node(point.x, point.y); // get end node
        AStar aster = new AStar(this.w, 4, start, end); // init A* algorithm
        aster.start();  // start search
        return aster.pathOrder;
    }
}

class KnowledgeBase {
    public Vector<PreceptInfo> kb;
    public Vector<PreceptInfo> openNodeKb;

    /**
     * AND logic
     * e.g. S[1,1] = false; represent there are no Wumpus in [1, 2] and [2, 1]
     * W[1, 2] = false and W[2, 1] = false
     */
    List<Point> wumpusAndLogic = new ArrayList<>();
    List<Point> pitAndLogic = new ArrayList<>();
    /**
     * OR logic
     * e.g. S[1,2] = true; represent there are a Wumpus in [1, 2]
     */
    List<List<Point>> wumpusOrLogics = new ArrayList<>();
    List<List<Point>> pitOrLogics = new ArrayList<>();

    public Point Wumpus = null;
    public List<Point> Pits = null;

    public KnowledgeBase() {
        kb = new Vector<PreceptInfo>();
        openNodeKb = new Vector<PreceptInfo>();
    }


    public void createKB(World w) {
        PreceptInfo current = new PreceptInfo(w);
        // If have gold
        if (current.hasGlitter == 1) {
            w.doAction(World.A_GRAB);
            return;
        }
        // Remove node from openNodeKb
        this.openNodeKb.removeIf(pi -> pi.point.equals(current.point));

        if (current.hasWumpus == 0) {
            if (!this.wumpusAndLogic.contains(current.point))
                this.wumpusAndLogic.add(current.point);
        }
        if (current.hasPit == 0) {
            if (!this.pitAndLogic.contains(current.point))
                this.pitAndLogic.add(current.point);
        }
        // add current logic information and node information
        addWumpusLogicInfo(current);
        addPitLogicInfo(current);
        kb.add(current);

        // add neighbor node logic information
        for (Point point : current.getAllNeighborPoint(current.point)) {
            PreceptInfo pi_temp = null;
            if (!isPointInKB(this.kb, point) && !isPointInKB(this.openNodeKb, point)) {
                pi_temp = new PreceptInfo(point);
                if (current.hasStench == 1) {
                    pi_temp.isSafe = 0;
                } else {
                    pi_temp.hasWumpus = 0;
                }

                if (current.hasBreeze == 1) {
                    pi_temp.isSafe = 0;
                } else {
                    pi_temp.hasPit = 0;
                }

                // if current node don't have stench and breeze, the neighbor node don't have Wumpus and pit
                if (current.hasStench == 0 && current.hasBreeze == 0) {
                    pi_temp.isSafe = 1;
                }
                pi_temp.hasVisited = 0;

                this.openNodeKb.add(pi_temp);
            }
        }

    }

    private boolean isPointInKB(Vector<PreceptInfo> pis, Point point) {
        for (PreceptInfo pi : pis) {
            if (pi.point.equals(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get all Wumpus logic for current node
     */
    private void addWumpusLogicInfo(PreceptInfo pi) {
        List<Point> wupusOr = new ArrayList<>();

        List<Point> points = pi.getAllNeighborPoint(pi.point);

        if (pi.hasStench == 1) {
            for (Point point : points)
                if (!wupusOr.contains(point)) {
                    wupusOr.add(point);
                }
            if (wupusOr.size() > 0) {
                this.wumpusOrLogics.add(wupusOr);
            }
        } else {
            for (Point point : points)
                if (!this.wumpusAndLogic.contains(point)) {
                    this.wumpusAndLogic.add(point);
                }
        }


    }

    /**
     * get all Pit logic for current node
     */
    private void addPitLogicInfo(PreceptInfo pi) {
        List<Point> pitOr = new ArrayList<>();
        List<Point> points = pi.getAllNeighborPoint(pi.point);

        if (pi.hasBreeze == 1) {
            for (Point point : points) {
                if (!pitOr.contains(point)) {
                    pitOr.add(point);
                }
            }
            if (pitOr.size() > 0)
                this.pitOrLogics.add(pitOr);
        } else {
            for (Point point : points)
                if (!this.pitAndLogic.contains(point)) {
                    this.pitAndLogic.add(point);
                }
        }
    }

    /**
     * do reason from knowledge base
     */
    public void doReason() {
        // If there are not sure coordinate of Wumpus, reasoning
        if (this.wumpusOrLogics.size() > 0) {
            for (List<Point> wumpusLogic : this.wumpusOrLogics) {
                for (Point point : this.wumpusAndLogic) {
                    if (wumpusLogic.contains(point)){
                        // If point is unvisited, update information
                        if (isPointInKB(this.openNodeKb, point))
                            updataOpenNodeKb(this.openNodeKb, point, "Wumpus");
                        wumpusLogic.remove(point);
                    }
                }
                // If inferring the location of a Wumpus
                if (wumpusLogic.size() == 1) {
                    for (PreceptInfo pi : this.openNodeKb)
                        if (pi.point.equals(wumpusLogic.get(0))) {
                            pi.hasWumpus = 1;
                        }

                    // Save Wumpus's coordinate
                    if (this.Wumpus == null)
                        this.Wumpus = new Point(wumpusLogic.get(0).x, wumpusLogic.get(0).y);
                }
            }
            updateOrLogics(this.wumpusOrLogics);
        }

        // if exist two wumpusOrLogic contain a same point
        if (this.wumpusOrLogics.size() == 2) {
            for (PreceptInfo pi :this.openNodeKb) {
                if (this.wumpusOrLogics.get(0).contains(pi.point)
                        && this.wumpusOrLogics.get(1).contains(pi.point)) {
                    // Wumpus must in this point
                    pi.hasWumpus = 1;
                    pi.isSafe = 0;
                    if (this.Wumpus == null) {
                        this.Wumpus = new Point(pi.point.x, pi.point.y);
                    }
                    this.wumpusOrLogics.clear();
                    break;
                }
            }
            // Set hasWumpus = 0 for neighbor point except wumpus's point
            for (PreceptInfo pi : this.openNodeKb) {
                if (pi.hasWumpus == 2) {
                    pi.hasWumpus = 0;
                    if (pi.hasWumpus == 0 && pi.hasPit == 0)
                        pi.isSafe = 1;
                }
            }
        }


        // If there are not sure coordinate of Pit, reasoning
        if (this.pitOrLogics.size() > 0) {
            for (List<Point> pitLogic : this.pitOrLogics) {
                for (Point point : this.pitAndLogic) {
                    if (pitLogic.contains(point)) {
                        if (isPointInKB(this.openNodeKb, point))
                            updataOpenNodeKb(this.openNodeKb, point, "Pit");
                        pitLogic.remove(point);
                    }
                }
                // If inferring the location of a Pit
                if (pitLogic.size() == 1) {
                    for (PreceptInfo pi : this.openNodeKb)
                        if (pi.point.equals(pitLogic.get(0))) {
                            pi.hasPit = 1;
                            pi.isSafe = 0;
                            if (this.Pits == null)
                                this.Pits = new ArrayList<>();
                            this.Pits.add(pi.point);
                        }
                }
            }

            updateOrLogics(this.pitOrLogics);
        }

        // If exist two pitOrLogic contain a same point
//        while (this.pitOrLogics.size() >= 2) {
//            int len = this.pitOrLogics.size();
//            for (PreceptInfo pi : this.openNodeKb) {
//                int i = 0;
//                for (int j = 0; j < len; j++) {
//                    if (this.pitOrLogics.get(j).contains(pi.point)) {
//                        i++;
//                    }
//                }
//                // Determine the location of a PI
//                if (i >= 2) {
//                    pi.hasPit = 1;
//                    pi.isSafe = 0;
//                    if (this.Pits == null)
//                        this.Pits = new ArrayList<>();
//                    this.Pits.add(pi.point);
//
//                    this.pitOrLogics.removeIf(logic -> logic.contains(pi.point));
//                }
//            }
//        }
    }

    public void updateOrLogics(List<List<Point>> logics) {
        logics.removeIf(logic -> logic.size() == 1);
    }

    private void updataOpenNodeKb(Vector<PreceptInfo> openList, Point point, String type) {
        for (PreceptInfo pi : openList) {
            if (pi.point.equals(point)) {
                if (type == "Wumpus")
                    pi.hasWumpus = 0;
                if (type == "Pit")
                    pi.hasPit = 0;
            }
            if (pi.hasWumpus == 0 && pi.hasPit == 0)
                pi.isSafe = 1;
        }

    }
    private boolean sucKillWumpus(World w, int x, int y) {

        // If current point has no stench, success
        if (!w.hasStench(x, y))
            return true;
        // If current point has stench, kill failure
        return false;
    }
    public void afterShoot(World w) {
        int x = w.getPlayerX();
        int y = w.getPlayerY();
        // check kill Wumpus or not
        if (sucKillWumpus(w, x, y)) {
            // success,
            for (PreceptInfo pi : this.openNodeKb) {
                pi.hasWumpus = 0;
                if (pi.hasWumpus == 0 && pi.hasPit == 0)
                    pi.isSafe = 1;
            }
            for (PreceptInfo pi : this.kb) {
                pi.hasStench = 0;
                if (pi.hasWumpus == 0 && pi.hasPit == 0)
                    pi.isSafe = 1;
            }

            // delete all wumpus logic, and add again
            this.wumpusAndLogic.clear();
            this.wumpusOrLogics.clear();
            for (PreceptInfo pi : this.kb) {
                if (pi.point.equals(new Point(x, y))) {
                    addWumpusLogicInfo(pi);
                    break;
                }
            }
            this.Wumpus = null;
        } else {
            // failure
            this.Wumpus = null;
        }
    }
    /**
     * Find the node we want to access
     * @return A point as end node for A* algorithm
     */
    public Point makeChoose() {
        if (this.openNodeKb.size() > 0) {
            for (PreceptInfo pi : this.openNodeKb) {
                if (pi.isSafe == 1)
                    return pi.point;
            }
        }
        if (this.Wumpus != null) {
            // If no safe point and have Wumpus's coordinate, shoot it
            return this.Wumpus;
        } else if (this.kb.size() == 1 && this.openNodeKb.size() == 2 && this.kb.get(0).hasStench == 1){
            // condition 4
            Point point = this.openNodeKb.get(0).point;
            this.Wumpus = new Point(point.x, point.y);
            return point;
        }

        // Select the coordinates of the PIT not found
        if (this.Pits != null)
            for (PreceptInfo pi : this.openNodeKb) {
                if (!this.Pits.contains(pi.point))
                    return pi.point;
            }
        // If have no safe node and don't know where is Wumpus
        // and PIT exists in all open coordinates, I have no idea and choose the first element
        return this.openNodeKb.firstElement().point;
    }
}

class PreceptInfo {
    public Point point;    // player's coordinate
    /**
     * 2: not set 1: true 0: false
     */
    public int hasWumpus = 2;
    public int hasStench = 2;
    public int hasPit = 2;
    public int hasBreeze = 2;
    public int hasGlitter = 2;
    public int hasVisited = 2;
    public int isSafe = 2;

    public PreceptInfo(World world) {
        int x = world.getPlayerX();
        int y = world.getPlayerY();
        this.point = new Point(x, y);
        this.hasWumpus = world.hasWumpus(x, y) == true ? 1: 0;
        this.hasStench = world.hasStench(x, y) == true ? 1: 0;
        this.hasPit = world.hasPit(x, y) == true ? 1: 0;
        this.hasBreeze = world.hasBreeze(x, y) == true ? 1: 0;
        this.hasGlitter = world.hasGlitter(x, y) == true ? 1: 0;
        this.hasVisited = world.isVisited(x, y) == true ? 1: 0;
        setSafe();
    }

    public PreceptInfo(Point point) {
        this.point = point;
    }

    public void setSafe() {
        if (this.hasWumpus == 1 || this.hasPit == 1) {
            isSafe = 0;
        } else isSafe = 1;
    }


    public List<Point> getAllNeighborPoint(Point point) {

        List<Point> neighborPoints = new ArrayList<>();
        Point point_temp = null;
        // up
        point_temp = getNeighborPoint(point, point.x, point.y - 1);
        if ( point_temp!= null)
            neighborPoints.add(point_temp);
        // down
        point_temp = getNeighborPoint(point, point.x, point.y + 1);
        if ( point_temp!= null)
            neighborPoints.add(point_temp);
        // left
        point_temp = getNeighborPoint(point, point.x - 1, point.y);
        if ( point_temp!= null)
            neighborPoints.add(point_temp);
        // right
        point_temp = getNeighborPoint(point, point.x + 1, point.y);
        if ( point_temp!= null)
            neighborPoints.add(point_temp);

        return neighborPoints;
    }
    public Point getNeighborPoint(Point point, int x, int y) {
        if (isPointInMaps(x, y)) {
            return new Point(x, y);
        }
        return null;
    }
    public boolean isPointInMaps(int x, int y) {
        if (x < 1 || x > 4 || y < 1 || y > 4) {
            return false;
        } else {
            return true;
        }
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

    public Stack<Point> pathOrder = new Stack<>();

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
        this.maps = initMapInfo(world, size, end);
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
    private int[][] initMapInfo(World world, int size, Node end) {
        int[][] maps = new int[size+1][size+1];
        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length; j++) {
                if (world.hasPit(i, j) || world.isUnknown(i, j)) {
                    maps[j][i] = 1;
                } else maps[j][i] = 0;
                if (end.point.y == j && end.point.x == i)
                    maps[j][i] = 0;
            }
        }
//        System.out.println("************");
//        for (int i = 1; i < maps.length; i++) {
//            for (int j = 1; j < maps[i].length; j++) {
//                System.out.print(maps[j][i] + " ");
//            }
//            System.out.println();
//        }
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
        while (end != null) {
            Point point = end.point;
            this.pathOrder.push(point);
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
    public Point() {
        this.x = 1;
        this.y = 1;
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