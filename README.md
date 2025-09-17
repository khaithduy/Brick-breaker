# Brick Breaker Game

A classic Brick Breaker game implemented in Java using Swing.

## Game Features

- Classic brick breaking gameplay
- Paddle control with arrow keys
- Score tracking
- Game over and restart functionality
- Clean Java Swing GUI

## How to Run

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Terminal/Command Prompt

### Build and Run

```bash
# Clone the repository
git clone <your-repo-url>
cd Brick-breaker-game

# Compile the game
javac -d build/ src/main/java/brick/game/*.java

# Run the game
java -cp build brick.game.MyApp
```

## How to Play

- **←/→ Arrow Keys**: Move paddle left/right
- **Enter**: Restart game when game over
- **Goal**: Break all bricks to win!
- **Score**: +5 points per brick

## Project Structure

```
src/main/java/brick/game/
├── MyApp.java        # Main application entry point
├── GamePlay.java     # Game logic and rendering
└── MapGenerator.java # Brick map generation
```

**Enjoy the game!**
