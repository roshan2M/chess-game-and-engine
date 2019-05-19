# chess-game-and-engine

A chess game developed in Java. Utilizes object-oriented principles such as classes, inheritance, abstraction and others. Abstract classes are used for the Move (AttackingMove, CastleMove, etc.), Piece (Queen, Rook, Knight, etc.),  and Player (BlackPlayer, WhitePlayer, etc.) features.

I am also currently in the process of developing an engine for this game, which has been a challenging experience. The engine will use previous games in order to build a tree of possible moves and assign point-values to those moves. Then, the engine will use the Minimax algorithm to determine the best move for the next player. Assume there are 2 players MIN and MAX. As the name suggests, MIN wants to minimize the score and MAX wants to maximize the score.

So, calling ```minimax(L)```, where ```L``` is the current layer of the move tree being analyzed, will return the following.
* If ```player(L) = MIN```, which means ```MIN``` is playing on layer ```L```, then return ```min(result(L,a))``` over all actions ```a```, where ```result(L,a)``` is the next layer of nodes analyzed from applying action ```a``` on ```L```.
* If ```player(L) = MAX```, which means ```MAX``` is playing on layer ```L```, then return ```max(result(L,a))``` over all actions ```a```.

Some optimizations will include ordering the tree (e.g. from lowest values on left to highest on right) to make searching for the lowest/highest possible 'score' an easy task. However, the algorithm must choose values that lie between the lower score assigned to the ```MAX``` player (who is trying to increase his score) and the higher score assigned to the ```MIN``` player (who is trying to decrease his score). This means entire subtrees will be quickly removed, considerably improving time efficiency. This process is called Alpha-Beta pruning.

