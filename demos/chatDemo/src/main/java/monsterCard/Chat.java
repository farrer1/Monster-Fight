package monsterCard;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.javalin.Javalin;
import io.javalin.websocket.WsSession;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import static j2html.TagCreator.*;


public class Chat {
    private static Map<WsSession, String> userUsernameMap = new ConcurrentHashMap<>();
    private static int nextUserNumber = 1;




    public static void main(String[] args) {

        Javalin.create()
                .enableStaticFiles("/public")
                .ws("/chat", ws -> {
                    ws.onConnect(session -> {
                        String username = "User" + nextUserNumber++;
                        userUsernameMap.put(session, username);
                        broadcastMessage("Server", (username + " joined the chat"));
                    });

                    ws.onClose((session, status, message) -> {
                        String username = userUsernameMap.get(session);
                        userUsernameMap.remove(session);
                        broadcastMessage("Server", (username + " left the chat"));
                    });

                    ws.onMessage((session, message) -> {
                        broadcastMessage(userUsernameMap.get(session), message);
                    });
                })
                .start(7070);



    }




    private static void broadcastMessage(String sender, String message) {
        System.out.println("in broadcastMessage function");
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            session.send(
                    new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values()).toString());

        });
    }



    private static String createHtmlMessageFromSender(String sender, String message) {

        System.out.println("in createHtmlMessageFromSender function");
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)

        ).render();
    }

}







