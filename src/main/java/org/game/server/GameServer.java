package org.game.server;

import org.game.common.protocol.MessageParser;
import org.game.common.protocol.enums.MessageType;
import org.game.server.repositories.QuestionRepo;
import org.game.server.repositories.TeamRepo;
import org.game.server.repositories.UserRepo;
import org.game.server.services.AuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    int port = 55555;
    List<ClientHandler> clients = new ArrayList<>();

    public void start() {
        try {
            System.out.println("Server is starting...");


            // TODO: connect with the lookup server and fetch questions data
//            SocketClient socketClient = new SocketClient("localhost", 4444);
//            String questions = socketClient.sendRequest(MessageParser.generate(MessageType.GET_QUESTIONS.name()));
//            String users = socketClient.sendRequest(MessageParser.generate(MessageType.GET_USERS.name()));
//            socketClient.close();

            UserRepo userRepo = new UserRepo();
            QuestionRepo questionRepo = new QuestionRepo();
            questionRepo.load("");
            TeamRepo teamRepo = new TeamRepo();


            AuthService authService = new AuthService(userRepo);




            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept(); // blocks until someone connects
                ClientHandler clientHandler = new ClientHandler(
                        socket,
                        authService,
                        questionRepo,
                        teamRepo
                );
                clients.add(clientHandler);
                Thread t = new Thread(clientHandler);
                t.start();
            }

        } catch (Exception e) {
            System.out.println("Error starting server");
            e.printStackTrace();
        }
    }
}
