# Hold the Line

A two-player, turn-based tower-defence game developed in Java for the Software Development 2 university module. Players build defences and send attacking units towards their opponent's base while managing their money across rounds.

## Gameplay

- **Shared battlefield:** a symmetrical, tile-based map with two paths connecting the players' bases.
- **Preparation:** each player takes a turn purchasing and placing towers and queuing attacking mobs.
- **Execution:** after both turns, queued mobs move along their paths while towers attack enemies within range.
- **Round progression:** the next round begins once all active mobs have been defeated or reached a base.
- **Economy:** players spend money on units and defences, with income and kill bounties supporting later rounds.
- **Win condition:** reduce the opposing base's health to zero while protecting your own.

Mobs have attributes such as health, speed, damage and cost. Towers have damage, attack range and purchase costs.

## Technology and architecture

The game uses **Java**, **Swing**, **JUnit** and **Git**. Its code separates game entities, state management and the desktop interface:

| Area | Main responsibilities |
| --- | --- |
| `src/entities/` | Abstract `Entity` base class and `Mob` / `Tower` models |
| `src/model/` | Players, game state, turn progression, income and purchasing |
| `src/view/` | Swing window, map rendering and game controller |
| `src/assets/` | Textures and sprites loaded through an asset cache |
| `src/tests/` | Unit and gameplay integration tests |

## Team contributions

| Contributor | Documented work |
| --- | --- |
| **Anton Zahrai** | Game rules; `Entity`, `Mob` and `Tower` models; entity tests using TDD; feature-branch integration |
| **Davyd Levytskyi** | Player and game-state models; turn and round management; economy and purchase logic; associated tests |
| **Polina Batanova** | Swing interface, map rendering, asset loading and animations; controller interactions; map and gameplay tests |

## Development and testing

The team used separate feature branches and an integration branch to combine the game systems. Development included TDD cycles with failing tests followed by implementation.

The documented tests cover:

- Entity coordinates, names and statistics; damage and health boundaries; tower range checks.
- Player money, purchase validation, mob queues and round income.
- Map dimensions, base alignment and paths.
- Turn transitions, tower placement, mob movement and damage to bases.

Integration work included resolving resource-path and sprite-rendering issues. Anton also recovered a teammate's model implementation after template files were accidentally retained during a merge.
