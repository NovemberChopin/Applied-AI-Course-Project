# WumpusWorld
**We reached D level**
### 1. Member
| Name       | Work         | Contribution        |
|:-----------:| :-------------:|:-------------:|
| Jinshi Liu | Logical reasoning  | 33.3% | 
| Yuzhu Chen | A star algorithm  | 33.3% | 
| Zhuangshu Qing | A star algorithm  | 33.3% | 

### 2. Logical reasoning
Game implementation process:
- Get current environment information and save it to the knowledge base
- Generated about Wumpus and Pit
- Logical reasoningChoose the next step to go
- Use A* algorithm to get the optimal path

**Begin game:(Take map one as an example)**

Knowledge base: 
- `Kb` Representing the point that has passed
- `openNodeKb` Represents all the coordinates that may be taken next
- `WumpusAndLogic` Represents all points where Wumpus is impossible exist
- `WumpusOrLogics` Represents all points that may have Wumpus
- `pitAndLogic` Represents all points where Pit is impossible exist
- `pitOrLogics` Represents all points that may have Pit

| Point | W    | S   | P   | B   | G   |isSafe|
|:---:| :---:|:---:|:---:|:---:|:---:|:---:|
|[1, 1]|  0  | 0  |  0   |  0  | 0   |  1  |
|[1, 2]|  0  | ?  |  0   |  ?  | ?   |  1  |
|[2, 1]|  0  | ?  |  0   |  ?  | ?   |  1  |
```json
"wumpusAndLogic": {[1, 1], [1, 2], [2, 1]}
"pitAndLogic": {[1, 1], [1, 2], [2, 1]}
```
**Move to [1, 2]:**

Knowledge base

| Point| W    | S   | P  | B   | G   |isSafe|
|:---:| :---:|:---:|:---:|:---:|:---:|:---:|
|[1, 1]|  0  | 0  |  0   |  0  | 0   |  1  |
|[1, 2]|  0  | 1  |  0   |  0  | 0   |  1  |
|[2, 1]|  0  | ?  |  0   |  ?  | ?   |  1  |
|[1, 3]|  ?  | ?  |  0   |  ?  | ?   |  ?  |
|[2, 2]|  ?  | ?  |  0   |  ?  | ?   |  ?  |
```json
"wumpusAndLogic": {[1, 1], [1, 2], [2, 1], }
"pitAndLogic": {[1, 1], [1, 2], [2, 1], [1, 3], [2, 2]}
"wumpusOrLogics": {
  {[1, 1], [1, 3], [2, 2]},
}
"pitOrLogics": {
  {},
}
```
**Move to [2, 1]:**

|Point| W    | S   | P   | B   | G   |isSafe|
|:---:| :---:|:---:|:---:|:---:|:---:|:---:|
|[1, 1]|  0  | 0  |  0   |  0  | 0   |  1  |
|[1, 2]|  0  | 1  |  0   |  0  | 0   |  1  |
|[2, 1]|  0  | 1  |  0   |  0  | 0   |  1  |
|[1, 3]|  ?  | ?  |  0   |  ?  | ?   |  ?  |
|[2, 2]|  ?  | ?  |  0   |  ?  | ?   |  ?  |
|[3, 1]|  ?  | ?  |  0   |  ?  | ?   |  ?  |
```json
"wumpusAndLogic": {[1, 1], [1, 2], [2, 1], }
"pitAndLogic": {[1, 1], [1, 2], [2, 1], [1, 3], [2, 2], [3, 1]}
"wumpusOrLogics": {
  {[1, 1], [1, 3], [2, 2]},
  {[1, 1], [3, 1], [2, 2]},
}
"pitOrLogics": {
  {},
}
```

### 3. A star algorithm

G: The horizontal and vertical movement distance is 10
H: Calculation method: Calculate heuristics using the Manhattan method

class:
- Point: Represent coordinate
- Node: Node data structure in A* algorithm
- AStar: A* algorithm
