package server.command;

import java.io.IOException;
import server.Session;

public interface Command {
    void execute(String[] args, Session session) throws IOException;
}
