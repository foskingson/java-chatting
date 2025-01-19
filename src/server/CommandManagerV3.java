package server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.command.ChangeCommand;
import server.command.Command;
import server.command.ExitCommand;
import server.command.JoinCommand;
import server.command.MessageCommand;
import server.command.UsersCommand;

public class CommandManagerV3 implements CommandManager{

    public static final String DELIMITER = "\\|";
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManagerV3(SessionManager sessionManager) {
        commands.put("/join", new JoinCommand(sessionManager));
        commands.put("/message", new MessageCommand(sessionManager));
        commands.put("/change", new ChangeCommand(sessionManager));
        commands.put("/users", new UsersCommand(sessionManager));
        commands.put("/exit", new ExitCommand());
    }

    @Override
    public void execute(String totalMessage, Session session) throws IOException {
        String[] args = totalMessage.split(DELIMITER);
        String key = args[0];

        Command command = commands.get(key);
        if (command == null){
            session.send("처리할 수 없는 명령어 입니다: " + totalMessage);
            return;
        }
        command.execute(args,session);
    }
}
