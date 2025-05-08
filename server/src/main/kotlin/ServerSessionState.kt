import io.ktor.websocket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

object ServerSessionState {
    val carSessionMap = ConcurrentHashMap<String, WebSocketSession>()
}
