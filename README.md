# chess-game-and-engine

A chess game developed in Java. Utilizes object-oriented principles such as classes, inheritance, abstraction and others. Abstract classes are used for the Move (AttackingMove, CastleMove, etc.), Piece (Queen, Rook, Knight, etc.),  and Player (BlackPlayer, WhitePlayer, etc.) features.

I am also currently in the process of developing an engine for this game, which has been a challenging experience. The engine will use previous games in order to build a graph of possible moves and assign point-values of those moves. These values will be stored in an ordered graph. Then, the engine will use the Minimax algorithm to determine the best move for the next player. Assume there are 2 players MIN and MAX. As the name suggests, MIN wants to minimize the score and MAX wants to maximize the score.

So, calling ```minimax(L)```, where ```L``` is the current layer of move being analyzed, will return the following.
* If ```player(L) = MIN```, which means ```MIN``` is playing on layer ```L```, then return ```min(result(L,a))``` over all actions ```a```.
* If ```player(L) = MAX```, which means ```MAX``` is playing on layer ```L```, then return ```max(result(L,a))``` over all actions ```a```.
