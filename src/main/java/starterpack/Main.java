package starterpack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import starterpack.action.AttackAction;
import starterpack.action.BuyAction;
import starterpack.action.MoveAction;
import starterpack.action.UseAction;
import starterpack.game.GameState;
import starterpack.networking.Client;
import starterpack.networking.CommState;
import starterpack.strategy.Strategy;
import starterpack.strategy.StrategyConfig;


public class Main {
    enum Phase { USE, MOVE, ATTACK, BUY }

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    private static final int RETRY_MILIS = 1000;

    public static void main(String[] args) {

        if (System.getProperty("debug") != null && System.getProperty("debug").equals("true")) {
            Configurator.setLevel(LogManager.getLogger(Main.class).getName(), Level.DEBUG);
            Configurator.setLevel(LogManager.getLogger(Client.class).getName(), Level.DEBUG);
            Configurator.setLevel(LogManager.getLogger(Strategy.class).getName(), Level.DEBUG);
        } else {
            Configurator.setLevel(LogManager.getLogger(Main.class).getName(), Level.INFO);
            Configurator.setLevel(LogManager.getLogger(Client.class).getName(), Level.INFO);
            Configurator.setLevel(LogManager.getLogger(Strategy.class).getName(), Level.INFO);

        }

        LOGGER.info("Welcome to Mechmania 28 Java bot!");

        int playerIndex = -1;

        if (args.length >= 1) {

            try {
                playerIndex = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                LOGGER.debug("Invalid or empty player number. Please specify a valid player number.");
                return;
            }

            if (playerIndex < 0 || playerIndex >= Config.MAX_PLAYERS) {
                LOGGER.debug("Invalid or empty player number. Please specify a valid player number.");
                return;
            }
        } else {
            LOGGER.debug("Invalid or empty player number. Please specify a valid player number.");
        }

        Strategy strategy = StrategyConfig.getStrategy(playerIndex);

        Client client = new Client(Config.PORTS[playerIndex]);
        while (!client.isConnected()) {
            client.connect();
            if(!client.isConnected()) {
                try {
                    Thread.sleep(RETRY_MILIS);
                } catch (InterruptedException ignored) {
                }
            }
        }


        LOGGER.info("Connected to Engine. Setting up for game...");

        CommState commState = CommState.START;
        while (commState != CommState.END) {
            switch (commState) {
                case START:
                    LOGGER.info("Waiting for wake...");
                    while (!client.read().equals("wake")) continue;
                    commState = CommState.NUM_ASSIGN;
                    break;
                case NUM_ASSIGN:
                    String read = client.read();
                    playerIndex = Integer.parseInt(read);
                    LOGGER.info("Received player index: " + read);
                    commState = CommState.CLASS_REPORT;
                    break;
                case CLASS_REPORT:

                    client.write(
                            strategy.strategyInitialize(playerIndex));
                    commState = CommState.IN_GAME;
                    break;
                case IN_GAME:
                    commState = CommState.END;
                    break;
            }
        }

        LOGGER.info("Finished setup. Running game...");

        String gameStateString;
        Phase phase = Phase.USE;
        while (true) {

            gameStateString = client.read();

            if (gameStateString.equals("fin")) {
                break;
            }

            GameState gameState = Client.parseMessageAsGameState(gameStateString);

            ObjectMapper objectMapper = new ObjectMapper();
            String resultString = "";
            switch (phase) {
                case USE:
                    LOGGER.info("Turn: " + gameState.getTurn());
                    try {
                        resultString = objectMapper.writeValueAsString(new UseAction(playerIndex,
                                strategy.useActionDecision(gameState, playerIndex)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        client.disconnect();
                        e.printStackTrace();
                    }

                    phase = Phase.MOVE;
                    break;
                case MOVE:
                    try {
                        resultString = objectMapper.writeValueAsString(new MoveAction(playerIndex,
                                strategy.moveActionDecision(gameState, playerIndex)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        client.disconnect();
                        e.printStackTrace();
                    }
                    phase = Phase.ATTACK;
                    break;
                case ATTACK:
                    try {
                        resultString = objectMapper.writeValueAsString(new AttackAction(playerIndex,
                                strategy.attackActionDecision(gameState, playerIndex)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        client.disconnect();
                        e.printStackTrace();
                    }
                    phase = Phase.BUY;
                    break;
                case BUY:
                    try {
                        resultString = objectMapper.writeValueAsString(new BuyAction(playerIndex,
                                strategy.buyActionDecision(gameState, playerIndex)));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        client.disconnect();
                        e.printStackTrace();
                    }
                    phase = Phase.USE;
                    break;
            }

            client.write(resultString);


        }

        client.disconnect();
        LOGGER.info("Completed! Check your output at Engine\\gamelogs.");
    }
}
