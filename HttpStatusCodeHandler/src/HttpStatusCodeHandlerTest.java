import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HttpStatusCodeHandlerTest {

	@Test
	void test() {
		HttpStatusCodeHandler<Object, String> handler = new HttpStatusCodeHandler<>();
		handler.getHandlers().put(200, (key, body) -> "Success: " + key);
		handler.getHandlers().put(500, (key, body) -> "Failure: " + key);
		
		System.out.println(handler.handle(415));
		assertEquals("Failure: 415", handler.handle(415));
		
		System.out.println(handler.handle(101));
		assertEquals("Success: 101", handler.handle(101));
		
		assertThrows(HandlerUndefinedException.class, () -> handler.handle(99));
		assertThrows(HandlerUndefinedException.class, () -> handler.handle(651));
	}

}
