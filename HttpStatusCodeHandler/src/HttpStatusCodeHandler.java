import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * B -> body
 * O -> output
 * @author pycs9
 *
 * @param <I>
 * @param <O>
 */
public class HttpStatusCodeHandler<I, O> {
	private Map<Integer, BiFunction<Integer, I, O>> handlers = new ConcurrentHashMap<>();
	public Map<Integer, BiFunction<Integer, I, O>> getHandlers() {
		return handlers;
	}
	
	//TODO shouldn't I receive a body too? or return a function that receives a body?
	/*
	 * How do I want this to be used?
	 * handle(code, body)?
	 * handle(code).apply(body)?
	 * handle(resp)?
	 * 
	 * Regardless, putHandler should be (int, (int, body) -> T)
	 */
	public O handle(int code, I body) {
		BiFunction<Integer, I, O> handler = getHandler(code);
		return handler.apply(code, body);
	}
	
	public O handle(int code) {
		return handle(code, null);
	}
	
	private BiFunction<Integer, I, O> getHandler(int code) {
		System.out.println("HttpStatusCodeHandler.getHandler() "  + code);
		return handlers.computeIfAbsent(code, 
				key -> getHandler(toGenericCode(key))); 
	}
	
	/**
	 * Axx -> A00 (i.e. 415 -> 400)
	 * 400 -> 500
	 * 100 -> 200
	 * 300 -> 200
	 * 200 -> throw HandlerUndefinedException
	 * 500 -> throw HandlerUndefinedException
	 * @param code
	 * @return
	 */
	private static int toGenericCode(int code) {
		if (code < 100 || code >= 600) {
			throw new HandlerUndefinedException(code);
		}
		int result;
		switch (code) {
			case 300:
			case 400:
				result = 500;
				break;
			case 100:
				result = 200;
				break;
			case 200:
			case 500:
				throw new HandlerUndefinedException(code);
			default:
				result = code / 100 * 100;
				break;
		}
		
		return result;
	}
	
	
}
