# Java-Starterpack
Thank you for coming out to MechMania 28!

This repository contains source code of the Java Starterpack for MechMania 28. You may view or edit any file in this repository.

Make sure you have read [Wiki](https://github.com/MechMania-28/Wiki) for information on the game mechanisms.

## Get Started
Your job is to create a strategy for your bot on where, how and who it should fight. 

To get started, create a class under the strategy package and implement the `Strategy` interface. We will also provide an example strategy named `StarterStrategy` that will do nothing but being an example.

For each phase in the game, you will be given a `GameState` and your `playerIndex` as input, and you need to submit a decision by returning a value to the corresponding `..ActionDecision` methods. After you have done writing down your logic, register your strategy by editing the return values in `StrategyConfig.java`. You could create different strategy implementations and register them for different players.

You'll primarily need to look at the classes within the `starterpack.game` package to know what are the objects that assembles into in a `GameState` that you will receive from the engine. 

We have provided some useful stuff in the `starterpack.util` package. `Utility` includes some mathematical and random functions and `Logger` contains a logging object that we recommend using for printing and debugging. Try `Logger.LOGGER.info("your_message")`. It does the same job as `System.out.println` while keeping the output tidy, and prints out more information.

Finally, compile your bot by using `./gradlew build`, `./gradlew.bat build` or `sh ./gradlew` and you will find the executable jar under `build/libs`. Run any number of bots using

`java -jar path/to/jar <player_number>` or `./start-4-java-bots.bat` (this is a development byproduct so sorry mac users), which run 4 copies of your bot

alongside [Engine](https://github.com/MechMania-28/Engine) to test them out, and use `mm push` (TODO) to submit to the tournament!

Again, make sure you are familiar with the mechanisms of the game and the Strategy class. If you have any questions, do not hesitate to contact us through Discord or in person with any questions!

Good luck!

### Note
You can enable debug output with option `-Ddebug=true`, which will make the bot print out every message it sends to the engine.
