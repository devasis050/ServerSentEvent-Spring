package com.example.SseSpring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class PublisherController {
	
	List<SseEmitter> emitters = new ArrayList<>();
	
	@RequestMapping("/subscribe")
	public SseEmitter subscribe()
	{
		SseEmitter emitter = new SseEmitter();
		emitter.onCompletion(() -> {
			System.out.println("emiter complete");
			emitters.remove(emitter);
		});
		emitter.onError(t -> {System.out.println("Error:"); t.printStackTrace();}); 
		
		emitters.add(emitter);
		return emitter;
	}
	
	@RequestMapping("/publish/{msg}")
	@ResponseBody
	public String publish(@PathVariable String msg)
	{
		emitters.forEach(emitter-> {
			try {
				emitter.send(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return "Msg published to all subscriber";
	}
	
	@RequestMapping("/close")
	public String closeAllConnection()
	{
		return "All connection closed";
	}

}
